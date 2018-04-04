package lab1.WebCounter;

/**
   A servlet that demonstrates how session tracking can be used to
   count the number of requests made during a session.
   Note that the servlet requires some Java Enterprise edition API
   and a compatible web server
   @author Andrew Ensor
*/
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestCounter extends HttpServlet {
    private char QUOTE = '"';

    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException { // obtain the session for the client that made the request
	HttpSession session = request.getSession(true); // create if none
	Integer counter = (Integer) session.getAttribute("counter");
	String message;
	if (counter == null) { // new client session
	    counter = new Integer(0);
	    message = "Welcome to the web site";
	} else { // request is from an existing client session
	    counter = new Integer(counter.intValue() + 1);
	    message = "Client has visited " + counter.intValue() + " times since "
		    + new Date(session.getCreationTime());
	}
	session.setAttribute("counter", counter);
	// set response headers before returning any message content
	response.setContentType("text/html");
	// prepare the content of the response
	String thisURL = "RequestCounter";
	try (PrintWriter pw = response.getWriter()) {
	    pw.println("<!DOCTYPE HTML PUBLIC " + QUOTE + "-//W3C//DTD HTML 4.0 Transitional//EN" + QUOTE + ">\n"
		    + "<HTML>\n" + "<HEAD>\n" + "<TITLE>RequestCounter</TITLE>\n" + "</HEAD>\n" + "<BODY>\n"
		    + "<H1>RequestCounter Response</H1>\n" + "<P>" + message + "</P>\n" + "<P>Received from <A HREF="
		    + QUOTE + response.encodeURL(thisURL) + QUOTE + ">RequestCounter servlet</A></P>\n"
		    + "</BODY>\n</HTML>\n");
	}
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }
}
