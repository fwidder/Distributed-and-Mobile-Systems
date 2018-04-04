package lab3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;

public class DBUI extends JFrame {
    /**
    * 
    */
    private final static long serialVersionUID = -6555418624852962991L;
    static JComboBox<String> jComboBox1 = new JComboBox<>();
    static JTable jTable1 = new JTable(null);

    public static void main(String... strings) {
	DBUI.jComboBox1.addItem("Student");
	DBUI.jComboBox1.addItem("Course");
	DBUI.jComboBox1.addItem("Result");
	DBUI.jComboBox1.addActionListener(e -> {
	    try {
		String item = DBUI.jComboBox1.getSelectedItem().toString();
		// The driverURL to contain the Database Driver
		String driverURL = "org.apache.derby.jdbc.EmbeddedDriver";
		// The dbURL to contain the Database URL
		String dbURL = "jdbc:derby://localhost:1527/DMSDB2;" + "create=true;user=dms;password=dms2018";
		// Creating SQL query string
		String sqlQuery;
		ResultSet resultSet;
		// Step 1: Loading the drivers for JAVA DB
		Class.forName(driverURL);
		// Network Driver both will work with this example.
		// You can use any one of them
		// Class.forName("org.apache.derby.jdbc.ClientDriver");

		// Step 2: Connecting to sample Database in Java DB
		Connection connection = DriverManager.getConnection(dbURL);
		System.out.println("Database is created...");

		// Step 3: Creating the SQL Statement
		Statement statement = connection.createStatement();
		if (item.equals("Student")) {
		    sqlQuery = "Select * from STUDENT";
		    resultSet = statement.executeQuery(sqlQuery);
		    String[] col = new String[resultSet.getMetaData().getColumnCount()];
		    col[0] = "0";
		    col[0] = "1";
		    col[0] = "2";
		    int l = 0;
		    while (resultSet.next()) {
			l++;
		    }

		    Object[][] res = new Object[l][resultSet.getMetaData().getColumnCount()];

		    resultSet.beforeFirst();
		    int i = 0;
		    while (resultSet.next()) {
			for (int j = 0; j < resultSet.getMetaData().getColumnCount(); j++) {
			    res[i][j] = resultSet.getString(j);
			}

		    }
		    DBUI.jTable1.setModel(new javax.swing.table.DefaultTableModel(res, col));
		} else if (item.equals("Course")) {
		    sqlQuery = "Select * from COURSE";
		    resultSet = statement.executeQuery(sqlQuery);
		} else if (item.equals("Result")) {
		    sqlQuery = "Select * from STUDENT_CLOURSE_RESULT";
		    resultSet = statement.executeQuery(sqlQuery);
		} else {

		}

		// Step 6: close connection
		statement.close();
		connection.close();

	    } catch (Exception e2) {

	    } // TODO Auto-generated method stub

	});
	new DBUI("");
    }

    /**
     * @param string
     */
    public DBUI(String string) {
	// TODO Auto-generated constructor stub
    }
}