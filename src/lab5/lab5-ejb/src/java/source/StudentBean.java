/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author psr5783
 */
@Stateless
@LocalBean
public class StudentBean {
    
    public ArrayList<Student> getStudents() throws ClassNotFoundException, SQLException {
        String dbURL = "jdbc:derby://localhost:1527/DMSDB";
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection connection = DriverManager.getConnection(dbURL, "dms", "dms2018");
        Statement statement;
        statement = connection.createStatement();
        ResultSet ret = statement.executeQuery("SELECT * FROM STUDENT");
     
        ArrayList<Student> std = new ArrayList<>();
        while (ret.next()) {
            std.add(new Student(ret.getInt(1), ret.getString(2), ret.getString(3)));
        }
            statement.close();
            connection.close();
        return std;
    }
    
    public boolean addStudent(Student std)throws ClassNotFoundException, SQLException{
        String dbURL = "jdbc:derby://localhost:1527/DMSDB";
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection connection = DriverManager.getConnection(dbURL, "lab", "lab");
        PreparedStatement statement;
        statement = connection.prepareStatement("INSERT INTO STUDENT VALUES (" + std.stID + "," + std.firstName + "," + std.lastName + ")");
        return statement.execute();
    }

}
