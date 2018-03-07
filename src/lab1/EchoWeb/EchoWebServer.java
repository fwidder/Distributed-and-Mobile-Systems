package lab1.EchoWeb;

/**
   A class that represents a server that echos an HTTP request
   back to the client as an HTTP response with the request
   prepared as an HTML page
   @author Andrew Ensor
*/
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class EchoWebServer
{
   private boolean stopRequested;
   public static final int PORT = 8080; // some unused port number
   
   public EchoWebServer()
   {  stopRequested = false;
   }
   
   // start the server if not already started and repeatedly listen
   // for client connections until stop requested
   public void startServer()
   {  stopRequested = false;
      ServerSocket serverSocket = null;
      try
      {  serverSocket = new ServerSocket(PORT);
         serverSocket.setSoTimeout(2000); // timeout for accept
         System.out.println("Server started at "
            + InetAddress.getLocalHost() + " on port " + PORT);
      }
      catch (IOException e)
      {  System.err.println("Server can't listen on port: " + e);
         System.exit(-1);
      }
      while (!stopRequested)
      {  // block until the next client requests a connection
         // or else the server socket timeout is reached
         try
         {  Socket socket = serverSocket.accept();
            System.out.println("Connection made with "
               + socket.getInetAddress());
            // start an echo with this connection
            EchoWebConnection echo = new EchoWebConnection(socket);
            Thread thread = new Thread(echo);
            thread.start();
         }
         catch (SocketTimeoutException e)
         {  // ignore and try again
         }
         catch (IOException e)
         {  System.err.println("Can't accept client connection: " + e);
            stopRequested = true;
         }
      }
      try
      {  serverSocket.close();
      }
      catch (IOException e)
      {  // ignore
      }
      System.out.println("Server finishing");
   }
   
   // stops server AFTER the next client connection has been made
   // or timeout is reached
   public void requestStop()
   {  stopRequested = true;
   }
   
   // driver main method to test the class
   // note that it doesn't call requestStop so main does not exit
   public static void main(String[] args)
   {  EchoWebServer server = new EchoWebServer();
      server.startServer();
   }
   
   // inner class that represents a single echo HTTP connection
   private class EchoWebConnection implements Runnable
   {
      private Socket socket; // socket for client/server communication
      private final char QUOTE = '"';
      
      public EchoWebConnection(Socket socket)
      {  this.socket = socket;
      }
      
      public void run()
      {  PrintWriter pw = null; // output stream to client
         BufferedReader br = null; // input stream from client
         try
         {  // create an autoflush output stream for the socket
            pw = new PrintWriter(socket.getOutputStream(), true);
            // create a buffered input stream for this socket
            br = new BufferedReader(new InputStreamReader(
               socket.getInputStream()));
            // collect the request in a list
            List<String> clientRequest = new ArrayList<String>();
            String clientRequestLine = br.readLine();
            boolean moreToRead = (clientRequestLine != null);
            // check whether the request starts with POST
            boolean postRequest = moreToRead &&
               clientRequestLine.toUpperCase().startsWith("POST");
            while (moreToRead)
            {  clientRequest.add(clientRequestLine);
               if (clientRequestLine.length() == 0) // blank line
               {  // for POST request a blank line is followed by data
                  if (postRequest)
                  {  // determine length of POST content
                     int length = findContentLength(clientRequest);
                     // add POST content to list
                     char[] postContent = new char[length];
                     br.read(postContent, 0, length);
                     clientRequest.add(new String(postContent));
                  }
                  // end of request
                  moreToRead = false;
               }
               if (moreToRead)
               {  clientRequestLine = br.readLine();
                  // check whether connection has prematurely closed
                  moreToRead = (clientRequestLine != null);
               }
            }
            // send the HTTP reply header
            pw.println("HTTP/1.1 200 OK\r\n" +
               "Server: Echo Web Server\r\n" +
               "Content-Type: text/html\r\n\r\n" +
               "<!DOCTYPE HTML PUBLIC " + QUOTE +
               "-//W3C//DTD HTML 4.0 Transitional//EN" + QUOTE +
               ">\n" + 
               "<HTML>\n" +
               "<HEAD>\n" +
               "<TITLE>Echo Web Server</TITLE>\n" +
               "</HEAD>\n" +
               "<BODY>\n" +
               "<H1>Echo Web Server Response</H1>\n" +
               "<P>The following request was sent to the Server on port 8080:</P>\n" +
               "<PRE>");
            // send the request line by line
            for (String line : clientRequest)
               pw.println(line);
            // send the HTTP reply trailer
            pw.println("</PRE>\n</BODY>\n</HTML>\n");
            System.out.println("Closing connection with "
               + socket.getInetAddress());
         }
         catch (IOException e)
         {  System.err.println("Server error: " + e);
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
      
      // helper method that finds a line that starts with
      // CONTENT_LENGTH and returns the int value after it
      private int findContentLength(List<String> lines)
      {  for (String line : lines)
         {  if (line != null && line.toUpperCase().startsWith
               ("CONTENT-LENGTH"))
            {  // find the following int value 
               StringTokenizer tokenizer = new StringTokenizer(line);
               tokenizer.nextToken(); // CONTENT-LENGTH token
               return Integer.parseInt(tokenizer.nextToken());//length
            }
         }
         return 0; // no CONTENT-LENGTH found
      }
   }
}
