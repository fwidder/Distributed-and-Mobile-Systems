package lab1.Echo;

/**
   A class that represents a server that continually echos any TCP
   message back to the client until it receives DONE
   @author Andrew Ensor
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class EchoServer {
    // inner class that represents a single echo connection
    private class EchoConnection implements Runnable {
	private Socket socket; // socket for client/server communication

	public EchoConnection(Socket socket) {
	    this.socket = socket;
	}

	@Override
	public void run() {
	    PrintWriter pw = null; // output stream to client
	    BufferedReader br = null; // input stream from client
	    try { // create an autoflush output stream for the socket
		pw = new PrintWriter(socket.getOutputStream(), true);
		// create a buffered input stream for this socket
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// echo client messages until DONE is received
		String clientRequest;
		do { // start communication by waiting for client request
		    clientRequest = br.readLine(); // blocking
		    System.out.println("Received line: " + clientRequest);
		    // then prepare a suitable server response
		    String serverResponse = clientRequest;
		    pw.println(serverResponse); // println flushes itself
		} while (clientRequest != null && !EchoServer.DONE.equalsIgnoreCase(clientRequest.trim()));
		System.out.println("Closing connection with " + socket.getInetAddress());
	    } catch (IOException e) {
		System.err.println("Server error: " + e);
	    } finally {
		try {
		    if (pw != null) {
			pw.close();
		    }
		    if (br != null) {
			br.close();
		    }
		    if (socket != null) {
			socket.close();
		    }
		} catch (IOException e) {
		    System.err.println("Failed to close streams: " + e);
		}
	    }
	}
    }

    public static int PORT = 8888; // some unused port number
    public static String DONE = "done"; // terminates echo

    // driver main method to test the class
    // note that it doesn't call requestStop so main does not exit
    public static void main(String[] args) {
	EchoServer server = new EchoServer();
	server.startServer();
    }

    private boolean stopRequested;

    public EchoServer() {
	stopRequested = false;
    }

    // stops server AFTER the next client connection has been made
    // or timeout is reached
    public void requestStop() {
	stopRequested = true;
    }

    // start the server if not already started and repeatedly listen
    // for client connections until stop requested
    public void startServer() {
	stopRequested = false;
	ServerSocket serverSocket = null;
	try {
	    serverSocket = new ServerSocket(EchoServer.PORT);
	    serverSocket.setSoTimeout(2000); // timeout for accept
	    System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + EchoServer.PORT);
	} catch (IOException e) {
	    System.err.println("Server can't listen on port: " + e);
	    System.exit(-1);
	}
	while (!stopRequested) { // block until the next client requests a connection
				 // or else the server socket timeout is reached
	    try {
		Socket socket = serverSocket.accept();
		System.out.println("Connection made with " + socket.getInetAddress());
		// start an echo with this connection
		EchoConnection echo = new EchoConnection(socket);
		Thread thread = new Thread(echo);
		thread.start();
	    } catch (SocketTimeoutException e) { // ignore and try again
	    } catch (IOException e) {
		System.err.println("Can't accept client connection: " + e);
		stopRequested = true;
	    }
	}
	try {
	    serverSocket.close();
	} catch (IOException e) { // ignore
	}
	System.out.println("Server finishing");
    }
}
