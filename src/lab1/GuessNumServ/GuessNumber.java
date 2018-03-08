
/**
 * A servlet that demonstrates how session tracking can be used to count the
 * number of requests made during a session. Note that the servlet requires some
 * Java Enterprise edition API and a compatible web server
 *
 * @author Andrew Ensor
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GuessNumber extends HttpServlet {

    private final char QUOTE = '"';

    public void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {  // obtain the session for the client that made the request
        HttpSession session = request.getSession(true); //create if none
        Integer counter = (Integer) session.getAttribute("counter");
        Integer number = (Integer) session.getAttribute("number");
        Integer guess = Integer.parseInt(request.getParameter("guess"));
        String message;
        if (number == null || number == 0 || counter == null) {
            counter = 1;
            number = (int) (Math.random() * 100);
        }
        if (guess != null && number.equals(guess)) {
            message = "That's ("+guess+") the right number! you made it in " + counter + " try's!";
        } else {
            counter += 1;
            message = "That's ("+guess+") the wrong number! ";
            if (guess != null && guess < number) {
                message += "Try a bigger number!";
            } else if (guess != null) {
                message += "Try a smaler number!";
            } else {
                message = "You have to enter a number!";
            }
        }
        System.out.println("--------------------------------");
        System.out.println("Number:"+number);
        System.out.println("Guess:"+guess);
        System.out.println("Count:"+counter);
        System.out.println("--------------------------------");
        session.setAttribute("counter", counter);
        session.setAttribute("number", number);
        // set response headers before returning any message content
        response.setContentType("text/html");
        // prepare the content of the response
        String thisURL = "RequestCounter";
        try (PrintWriter pw = response.getWriter()) {
            pw.println("<!DOCTYPE HTML PUBLIC " + QUOTE
                    + "-//W3C//DTD HTML 4.0 Transitional//EN" + QUOTE + ">\n"
                    + "<HTML>\n" + "<HEAD>\n"
                    + "<TITLE>RequestCounter</TITLE>\n" + "</HEAD>\n" + "<BODY>\n" + message + "<form action=\"http://localhost:8080/Lab1/GuessNumber\">\n"
                    + "            <p>Number:\n"
                    + "                <input type=\"text\" name=\"guess\"></p>\n"
                    + "            <input type=\"submit\">\n"
                    + "        </form>" + "</BODY>\n</HTML>\n");
        }
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
