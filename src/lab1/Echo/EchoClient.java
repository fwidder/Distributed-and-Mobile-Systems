package lab1.Echo;

/**
   A class that represents a client that repeatedly sends any keyboard
   input to an EchoServer until the user enters DONE
   @see EchoServer.java
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner; // Java 1.5 equivalent of cs1.Keyboard

public class EchoClient
{
   public static final String HOST_NAME = "localhost";
   public static final int HOST_PORT = 8888; // host port number
   public static final String DONE = "done"; // terminates echo

   public EchoClient()
   {
   }
   
   public void startClient()
   {  Socket socket = null;
      @SuppressWarnings("resource")
	Scanner keyboardInput = new Scanner(System.in);
      try
      {  socket = new Socket(HOST_NAME, HOST_PORT);
      }
      catch (IOException e)
      {  System.err.println("Client could not make connection: " + e);
         System.exit(-1);
      }
      PrintWriter pw = null; // output stream to server
      BufferedReader br = null; // input stream from server
      try
      {  // create an autoflush output stream for the socket
         pw = new PrintWriter(socket.getOutputStream(), true);
         // create a buffered input stream for this socket
         br = new BufferedReader(new InputStreamReader(
            socket.getInputStream()));
         // send text to server and display reply until DONE entered
         System.out.println("Enter text or " + DONE + " to exit");
         String clientRequest;
         do
         {  // start communication by having client getting user
            // input and send it to server
            clientRequest = keyboardInput.nextLine();
            pw.println(clientRequest);  // println flushes itself
            // then get server response and display it
            String serverResponse = br.readLine(); // blocking
            System.out.println("Response: " + serverResponse);
         }
         while (!DONE.equalsIgnoreCase(clientRequest.trim()));
      }
      catch (IOException e)
      {  System.err.println("Client error: " + e);
      }
      finally
      {  try
         {  if (pw != null) pw.close();
            if (br != null) br.close();
            if (socket != null) socket.close();
         }
         catch (IOException e)
         {  System.err.println("Failed to close streams: " + e);
         }
      }
   }
   
   public static void main(String[] args)
   {  EchoClient client = new EchoClient();
      client.startClient();
   }
}
