package lab8;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

public class DatabaseQuerySample {
    public static void main(String []args)
            throws SQLException, ClassNotFoundException {
        // The dbURL to contain the Database URL
        String dbURL = "jdbc:derby://localhost:1527/sample";
        // Creating SQL query string
        String sqlQuery = "Select * from CUSTOMER";
        
        // Step 1: Loading the drivers for JAVA DB
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        // Network Driver both will work with this example.
        // You can use any one of them
        //Class.forName("org.apache.derby.jdbc.ClientDriver");
        
        // Step 2: Connecting to sample Database in Java DB
        Connection connection = DriverManager.getConnection(dbURL, "app", "app");
        
        // Step 3: Creating the SQL Statement
        Statement statement = connection.createStatement();
        
        // Step 4: Executing the SQL Statement
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        
        // Step 5: Reading data from the ResultSet
        while(resultSet.next()){
            System.out.println(resultSet.getString(1) + "\t"
                    + resultSet.getString(2) + "\t"
                    + resultSet.getString(3) + "\t"
                    + resultSet.getString(4) + "\t"
                    + resultSet.getString(5) + "\t"
                    + resultSet.getString(6) + "\t"
                    + resultSet.getString(7) + "\t" 
                    + resultSet.getString(8) + "\t"
                    + resultSet.getString(9) + "\t"
                    + resultSet.getString(10) + "\t"
                    + resultSet.getString(11) + "\t"
                    + resultSet.getString(12));
        }
        
        // Step 6: close connection
        connection.close();
    }
}
