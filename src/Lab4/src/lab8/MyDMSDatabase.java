package lab8;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;

public class MyDMSDatabase {
    public static void main(String [] args)
            throws SQLException, ClassNotFoundException {
        // The driverURL to contain the Database Driver
        String driverURL = "org.apache.derby.jdbc.EmbeddedDriver";
        // The dbURL to contain the Database URL
        String dbURL = "jdbc:derby://localhost:1527/DMSDB2;" + 
                "create=true;user=dms;password=dms2018";
        // Creating SQL query string
        String sqlQuery;
        ResultSet resultSet;
        int resultDB;
        
        // Step 1: Loading the drivers for JAVA DB
        Class.forName(driverURL);
        // Network Driver both will work with this example.
        // You can use any one of them
        //Class.forName("org.apache.derby.jdbc.ClientDriver");
        
        // Step 2: Connecting to sample Database in Java DB
        Connection connection = DriverManager.getConnection(dbURL);
        System.out.println("Database is created...");
        
        // Step 3: Creating the SQL Statement
        Statement statement = connection.createStatement();
        
        // Step 4: Creating a new Student table in DMSDB
        sqlQuery = "DROP TABLE STUDENT_COURSE_RESULT";
        resultDB = statement.executeUpdate(sqlQuery);
        sqlQuery = "DROP TABLE COURSE";
        resultDB = statement.executeUpdate(sqlQuery);
        sqlQuery = "DROP TABLE STUDENT";
        resultDB = statement.executeUpdate(sqlQuery);
        
        // Step 4: Creating a new Student table in DMSDB
        sqlQuery = "CREATE TABLE STUDENT " + "(StID INT PRIMARY KEY," +
                " firstName VARCHAR(20), lastName VARCHAR(20))";
        resultDB = statement.executeUpdate(sqlQuery);
        if(resultDB == 0)
            System.out.println("Student Table is created");
        
        

        // Step 4: Creating a new Student table in DMSDB
        sqlQuery = "CREATE TABLE COURSE " + "(courseID INT PRIMARY KEY," +
                " courseName VARCHAR(50), courseDescription VARCHAR(500), courseLevel INT)";
        resultDB = statement.executeUpdate(sqlQuery);
        if(resultDB == 0)
            System.out.println("Course Table is created");
        
        // Step 4: Creating a new Student table in DMSDB
        sqlQuery = "CREATE TABLE STUDENT_COURSE_RESULT (courseID INT, stID INT, marks INT, PRIMARY KEY(courseID, stID), FOREIGN KEY (courseID) REFERENCES COURSE(courseID), FOREIGN KEY (stID) REFERENCES STUDENT(StID))";
        resultDB = statement.executeUpdate(sqlQuery);
        if(resultDB == 0)
            System.out.println("Student_Course_Result Table is created");
        
        // Step 5: Inserting a record in the Student table in DMSDB
        sqlQuery = "INSERT INTO STUDENT VALUES" +
                "(1, 'David', 'Citizen')," +
                "(2, 'John', 'Keats')," +
                "(3, 'William', 'Blake')," +
                "(4, 'Leonardo', 'Davinci')";
        resultDB = statement.executeUpdate(sqlQuery);
        if(resultDB == 4)
            System.out.println("4 records are insterted in Student Table");
        
        // Step 5: Inserting a record in the Student table in DMSDB
        sqlQuery = "INSERT INTO COURSE VALUES" +
                "(1, 'C1', 'C1',1)," +
                "(2, 'C2', 'C2',2)," +
                "(3, 'C3', 'C3',3)," +
                "(4, 'C4', 'C4',4)" ;
        resultDB = statement.executeUpdate(sqlQuery);
        if(resultDB == 4)
            System.out.println("4 records are insterted in COURSE");
        // Step 5: Inserting a record in the Student table in DMSDB
        sqlQuery = "INSERT INTO STUDENT_COURSE_RESULT VALUES" +
                "(1, 1, 1)," +
                "(2, 2, 2)," +
                "(3, 3, 3)," +
                "(4, 4, 4)" ;
        resultDB = statement.executeUpdate(sqlQuery);
        if(resultDB == 4)
            System.out.println("4 records are insterted in STUDENT_COURSE_RESULT");
        // Step 6: Reading records from Student Table
        sqlQuery = "Select * from COURSE";
        resultSet = statement.executeQuery(sqlQuery);

        // Step 7: Reading data from the ResultSet
        while(resultSet.next()){
            System.out.println(resultSet.getString(1) + "\t"
                    + resultSet.getString(2) + "\t"
                    + resultSet.getString(2) + "\t"
                    + resultSet.getString(4));
        }  // Step 6: Reading records from Student Table
        sqlQuery = "Select * from STUDENT";
        resultSet = statement.executeQuery(sqlQuery);

        // Step 7: Reading data from the ResultSet
        while(resultSet.next()){
            System.out.println(resultSet.getString(1) + "\t"
                    + resultSet.getString(2) + "\t"
                    + resultSet.getString(3));
        }  // Step 6: Reading records from Student Table
        sqlQuery = "Select * from STUDENT_COURSE_RESULT";
        resultSet = statement.executeQuery(sqlQuery);

        // Step 7: Reading data from the ResultSet
        while(resultSet.next()){
            System.out.println(resultSet.getString(1) + "\t"
                    + resultSet.getString(2) + "\t"
                    + resultSet.getString(3));
        }
        
        // Step 6: close connection
        statement.close();
        connection.close();
    }
}
