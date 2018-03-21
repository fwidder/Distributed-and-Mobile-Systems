package lab1.Animation;

/**
   A class that receives blocks of animation images via UDP datagrams
   and displays them in a panel. Each datagram consist of a header
   with three int values: the pixel row and the pixel col of top left
   corner of block and the size of the square block (also in pixels)
   followed by the int ARGB values for each pixel in the block
   Note that this receiver does not check whether each image block
   can fit in the BufferedImage in the panel (can generate exception)
   @author Andrew Ensor
*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author Florian Widder
 * @author Student ID 18999061
 *
 */
@SuppressWarnings("serial")
public class AnimationReceiver extends JPanel {
	/**
	 * 
	 */
	public static final int PORT = 8889; // host port number
	private final int PANEL_WIDTH = 300, PANEL_HEIGHT = 400;
	private BufferedImage animatedImage;
	private boolean stopRequested;

	/**
	 * 
	 */
	public AnimationReceiver() {
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setBackground(Color.WHITE);
		// prepare the animated image
		animatedImage = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		stopRequested = false;
	}

	/**
	 * 
	 */
	public void receive() {
		try {
			System.out.println("Receiver started at " + InetAddress.getLocalHost() + " on port " + PORT);
		} catch (UnknownHostException e) {
			System.err.println("Can't determine local host: " + e);
		}
		// prepare a timer to repaint the panel every 50ms
		Timer timer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		timer.start();
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(PORT);
		} catch (SocketException e) {
			System.err.println("Unable to create socket: " + e);
			stopRequested = true;
		}
		// preallocate temporary arrays large enough for an image that
		// would fit entirely within panel
		byte[] buffer = new byte[4 * (PANEL_WIDTH * PANEL_HEIGHT + 3)];
		int[] pixels = new int[PANEL_WIDTH * PANEL_HEIGHT];
		DatagramPacket receiveDatagram = new DatagramPacket(buffer, buffer.length);
		int last_num = -1, lostFrame = 0;
		while (!stopRequested) {
			try {
				socket.receive(receiveDatagram);
				// retrieve the int data from the buffer
				DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));
				int num = dis.readInt();
				if (num != last_num + 1) {
					lostFrame++;
					System.out.println(
							"got Frame: " + num + ", wanted Frame:" + (last_num + 1) + ", lost Frames:" + lostFrame);
				}
				if (num <= last_num)
					return;
				else
					last_num = num;
				if (num >= 10000)
					last_num = -1;
				int x = dis.readInt();
				int y = dis.readInt();
				int blockSize = dis.readInt();
				int pixelBytes = receiveDatagram.getLength() - 3;
				for (int i = 0; i < pixelBytes / 4; i++)
					pixels[i] = dis.readInt();
				synchronized (this) {
					animatedImage.setRGB(x, y, blockSize, blockSize, pixels, 0, blockSize);
				}
			} catch (IOException e) {
				System.err.println("IO Exception: " + e);
			}
			Thread.yield(); // give other threads a chance
		}
		if (socket != null)
			socket.close();
		timer.stop();
	}

	public void requestStop() {
		stopRequested = true;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		synchronized (this) {
			g.drawImage(animatedImage, 0, 0, Color.WHITE, this);
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Animation Receiver via UDP");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AnimationReceiver animationPanel = new AnimationReceiver();
		frame.getContentPane().add(animationPanel);
		frame.pack();
		// position the frame in the middle of the screen
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenDimension = tk.getScreenSize();
		Dimension frameDimension = frame.getSize();
		frame.setLocation((screenDimension.width - frameDimension.width) / 2,
				(screenDimension.height - frameDimension.height) / 2);
		frame.setVisible(true);
		// start receiving the image blocks
		animationPanel.receive();
	}
}
