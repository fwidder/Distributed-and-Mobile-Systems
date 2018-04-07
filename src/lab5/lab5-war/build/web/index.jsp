<%-- 
    Document   : student
    Created on : 5/04/2018, 11:57:49 AM
    Author     : psr5783
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id= "student" scope= "session"   
             class= "source.StudentBean" >  
</jsp:useBean>
<%@ page import="source.Student" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <p>Number of Students in DB: <%= student.getStudents().size()%></p>
        <table>
            <%
                Student[] s = student.getStudents().toArray(new Student[0]);
                for (int i = 0; i < s.length; i++) {
            %>

            <tr>
                <td> <%=s[i].stID%></td>
                <td><%=s[i].firstName%> </td>
                <td> <%=s[i].lastName%></td>
            </tr>

            <%
                }
            %>
        </table>
        <form>
            <p>ID:
                <input type="text" name="id"></p>
            <p>First name:
                <input type="text" name="firstname"></p>
            <p>Last name:
                <input type="text" name="lastname"></p>
            

        <%!
void sampleFunction()
{

%>
Comeeeeeeees innnnnnnnn

<%!

}

%> 
        <input type="submit" name="H" onClick="sampleFunction()" >
        </form>  
    </body>
</html>
