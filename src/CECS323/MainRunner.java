package CECS323;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Derek
 */
public class MainRunner {

    static String USER = " ";
    static String PASS = " ";
    static String DBNAME = "323Project";
    static String DB_URL = "jdbc:derby://localhost:1527/";
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";


    public static void main(String args[]) {

        DB_URL = DB_URL + DBNAME;
        Connection conn = null; //initialize the connection

        try{


            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            //Make Menu With proper connection
            Menu menu = new Menu(conn);
            //Start Menu
            menu.menuThing();



        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("Failed connection to DataBase");
            //se.printStackTrace();
        } catch (ClassNotFoundException CNFE) {
            //Handle errors for Class.forName
            System.out.println("Can't find JDBC Driver Class");
            //CNFE.printStackTrace();


        } finally {
            //finally block used to close resources
            try {
                if (conn != null) {
                    conn.close();

                }
            } catch (SQLException se) {
                System.out.println("Trouble Closing Connection.");
                //se.printStackTrace();
            }//end finally try
        }//end try} while (option != 0);

    }
}
