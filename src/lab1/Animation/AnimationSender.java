package lab1.Animation;

/**
   A class that sends blocks of an animated image via UDP datagrams
   to the host
   @see AnimationReceiver.java
*/
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Timer;

public class AnimationSender {
	public static final String HOST_NAME = "localhost";
	public static final int HOST_PORT = 8889; // host port number
	private int numImages; // number of frames in animation
	private final String FILE_NAME = "HomerHelp.gif";// animated image
	private final int BLOCK_SIZE = 10; // should divide width and height
	private BufferedImage[] bufferedImages;
	private int currentFrame; // current frame (image) used in animation
	private boolean stopRequested;

	public AnimationSender() { // obtain the images as BufferedImages from the animated gif
		BufferedInputStream bis = new BufferedInputStream(AnimationSender.class.getResourceAsStream(FILE_NAME));
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(bis);
			ImageReader reader = ImageIO.getImageReaders(iis).next();
			reader.setInput(iis, false);
			numImages = reader.getNumImages(true);
			bufferedImages = new BufferedImage[numImages];
			for (int i = 0; i < numImages; i++)
				bufferedImages[i] = reader.read(i);
			System.out.println(numImages + " images loaded");
		} catch (IOException e) {
			System.out.println("Unable to read file: " + e);
		}
		currentFrame = 0;
		stopRequested = false;
	}

	private synchronized void swapFrame() {
		currentFrame++;
		if (currentFrame >= numImages)
			currentFrame = 0;
	}

	public void send() { // prepare a timer to swap between the images every 150ms
		Timer timer = new Timer(150, new ActionListener() {
			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				swapFrame();
			}
		});
		timer.start();
		// repeatedly send one block of current image as a datagram
		int x = 0, y = 0; // top left corner of block to send
		DatagramSocket socket = null;
		InetAddress hostAddress = null;
		try {
			socket = new DatagramSocket();
			hostAddress = InetAddress.getByName(HOST_NAME);
		} catch (SocketException e) {
			System.err.println("Unable to create socket: " + e);
			stopRequested = true;
		} catch (UnknownHostException e) {
			System.err.println("Unknown host: " + e);
			stopRequested = true;
		}
		// preallocate temporary array and stream
		int[] pixels = new int[BLOCK_SIZE * BLOCK_SIZE];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		System.out.print("Sending");
		int num = 0;
		while (!stopRequested) { // puts image pixels for a block into imageBlock
			synchronized (this) { // move to next block
				x += BLOCK_SIZE;
				if (x > bufferedImages[currentFrame].getWidth() - BLOCK_SIZE) {
					x = 0;
					y += BLOCK_SIZE;
				}
				if (y > bufferedImages[currentFrame].getHeight() - BLOCK_SIZE) {
					y = 0;
					System.out.print(".");
				}
				bufferedImages[currentFrame].getRGB(x, y, BLOCK_SIZE, BLOCK_SIZE, pixels, 0, BLOCK_SIZE);
			}
			// convert x, y, BLOCK_SIZE, pixels into byte stream
			// to send as one datagram
			try {
				dos.writeInt(num);
				num++;
				if (num > 10000)
					num = 0;
				dos.writeInt(x);
				dos.writeInt(y);
				dos.writeInt(BLOCK_SIZE);
				for (int i = 0; i < pixels.length; i++)
					dos.writeInt(pixels[i]);
				dos.flush();
				byte[] data = baos.toByteArray();
				baos.reset();
				// send the byte array as a datagram
				DatagramPacket sendDatagram = new DatagramPacket(data, data.length, hostAddress, HOST_PORT);
				socket.send(sendDatagram);
			} catch (IOException e) {
				System.err.println("IOException: " + e);
			}
			Thread.yield(); // give other threads a chance
		}
		System.out.println();
		if (socket != null)
			socket.close();
		timer.stop();
	}

	public void requestStop() {
		stopRequested = true;
	}

	public static void main(String[] args) {
		AnimationSender sender = new AnimationSender();
		sender.send();
	}
}
