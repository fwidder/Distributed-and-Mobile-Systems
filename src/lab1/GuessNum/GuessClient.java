package lab1.GuessNum;

/**
   A class that represents a client in a number guessing game
   @see GuessServer.java
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner; // Java 1.5 equivalent of cs1.Keyboard

public class GuessClient {
    public static String HOST_NAME = "localhost";
    public static int HOST_PORT = 7777; // host port number

    public static void main(String[] args) {
	GuessClient client = new GuessClient();
	client.startClient();
    }

    public GuessClient() {
    }

    public void startClient() {
	Socket socket = null;
	@SuppressWarnings("resource")
	Scanner keyboardInput = new Scanner(System.in);
	try {
	    socket = new Socket(GuessClient.HOST_NAME, GuessClient.HOST_PORT);
	} catch (IOException e) {
	    System.err.println("Client could not make connection: " + e);
	    System.exit(-1);
	}
	PrintWriter pw; // output stream to server
	BufferedReader br; // input stream from server
	try { // create an autoflush output stream for the socket
	    pw = new PrintWriter(socket.getOutputStream(), true);
	    // create a buffered input stream for this socket
	    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    // play the game until value is correctly guessed
	    boolean finished = false;
	    do {
		String serverResponse = br.readLine();
		System.out.println(serverResponse);
		if (serverResponse.toLowerCase().indexOf("correct") == 0) {
		    finished = true;
		} else { // get user input and sent it to server
		    pw.println(keyboardInput.nextLine());
		}
	    } while (!finished);
	    pw.close();
	    br.close();
	    socket.close();
	} catch (IOException e) {
	    System.err.println("Client error with game: " + e);
	}

    }
}
