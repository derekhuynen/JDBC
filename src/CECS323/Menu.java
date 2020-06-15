package CECS323;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;

/**
 *
 * @author Derek
 */
public class Menu {

    private final Connection conn;
    private final String displayFormat = "%-30s%-30s%-30s%-30s\n";

    private Statement stmt = null;  //initialize the statement that we're using
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private int option;
    private final Scanner cases = new Scanner(System.in);
    private String sql;

    public Menu(Connection conn) {
        this.conn = conn;
    }

    public static String dispNull(String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0) {
            return "N/A";
        } else {
            return input;
        }
    }

    public void menuThing() {

        try (Scanner in = new Scanner(System.in)) {
            do {
                try {
                    do {
                        String menu = "\n Please select an option to perform."
                                + "\n1. List all Writing Groups."
                                + "\n2. List All the Data for a specific Group."
                                + "\n3. List all Publishers."
                                + "\n4. List all data for a specific publisher."
                                + "\n5. List all Book titles."
                                + "\n6. List all the data for a specific Book."
                                + "\n7. Insert a Book."
                                + "\n8. Insert new Publisher.(And More)"
                                + "\n9. Remove a specific book."
                                + "\n0. Exit.";
                        System.out.println(menu);
                        System.out.print("Enter an option: ");
                        this.option = in.nextInt();
                    } while (this.option < 0 || this.option > 9); // This will make the menu repeat if option is higher than 6 or lowen than 0.

                    switch (this.option) {
                        case 1:
                            System.out.println("1. List all Writing Groups.");
                            case1();
                            break;

                        case 2:
                            System.out.println("2. List All the Data for a specific Group.");
                            case2();
                            break;

                        case 3:
                            System.out.println("3. List all Publishers.");
                            case3();
                            break;
                        case 4:
                            System.out.println("4. List all data for a specific publisher.");
                            case4();
                            break;

                        case 5:
                            System.out.println("5. List all Book titles.");

                            case5();
                            break;
                        case 6:
                            System.out.println("6. List all the data for a specific Book.");
                            case6();
                            break;
                        case 7:
                            System.out.println("7. Insert a Book.");
                            case7();
                            break;
                        case 8:
                            System.out.println("8. Insert new Publisher.(And More)");
                            case8();
                            break;
                        case 9:
                            System.out.println("9. Remove a specific book.");
                            case9();
                            break;
                        default:
                            System.out.println("Hasta la vista Baby");
                            break; //I always use this break, even when not needed.
                    }

                } catch (SQLException se) {
                    System.out.println("Failed Creating SQl Statment.\nTry Again.");
                    //se.printStackTrace();
                }

            } while (this.option != 0);

        } finally {//Close All
            this.cases.close();

            try {
                if (this.stmt != null) {
                    this.stmt.close();
                }
            } catch (SQLException se1) {
                // nothing we can do
                System.out.println("Trouble Closing Statement Connections.");
            }

            try {
                if (this.pstmt != null) {
                    this.pstmt.close();
                }
            } catch (SQLException se2) {
                // nothing we can do
                System.out.println("Trouble Closing Prepared Statement Connections.");
            }

            try {
                if (this.rs != null) {
                    this.rs.close();
                }
            } catch (SQLException se3) {
                // nothing we can do
                System.out.println("Trouble Closing ResultSet Connections.");
            }
        }
    }

    public void case1() throws SQLException {

        this.stmt = this.conn.createStatement();
        this.sql = "SELECT groupName, headWriter, yearFormed, subject FROM writingGroups";
        this.rs = this.stmt.executeQuery(this.sql);

        System.out.printf(this.displayFormat, "Group Name", "Head Writer", "Year Formed", "Subject");
        while (this.rs.next()) {
            //Retrieve by column name
            String groupName = this.rs.getString("groupName");
            String headWriter = this.rs.getString("headWriter");
            String yearFormed = this.rs.getString("yearFormed");
            String subject = this.rs.getString("subject");

            //Display values
            System.out.printf(this.displayFormat,
                    dispNull(groupName), dispNull(headWriter), dispNull(yearFormed), dispNull(subject));
        }
    }

    public void case2() throws SQLException {

        boolean valid = false;

        do {
            System.out.print("Please Enter a Group Name: ");
            String result = this.cases.nextLine().trim();

            this.sql = "SELECT groupName, headWriter, yearFormed, subject FROM writingGroups WHERE groupName =  ?";

            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1, result);
            this.rs = this.pstmt.executeQuery();

            if (!this.rs.next()) {
                System.out.println("Group Name is not in the DataBase Try Again.");
            } else {
                this.rs = this.pstmt.executeQuery();
                valid = true;
                while (this.rs.next()) {
                    //Retrieve by column name
                    String groupName = this.rs.getString("groupName");
                    String headWriter = this.rs.getString("headWriter");
                    String yearFormed = this.rs.getString("yearFormed");
                    String subject = this.rs.getString("subject");

                    //Display values
                    System.out.printf(this.displayFormat,
                            dispNull(groupName), dispNull(headWriter), dispNull(yearFormed), dispNull(subject));
                }
            }
        } while (!valid);

    }

    public void case3() throws SQLException {

        this.stmt = this.conn.createStatement();

        this.sql = "SELECT publisherName, publisherAddress, publisherPhone, publisherEmail FROM publishers";
        this.rs = this.stmt.executeQuery(this.sql);

        System.out.printf(this.displayFormat, "Publisher Name", "Publisher Address", "Publisher Phone Number", "Publisher Email");
        while (this.rs.next()) {
            //Retrieve by column name
            String publisherName = this.rs.getString("publisherName");
            String publisherAddress = this.rs.getString("publisherAddress");
            String publisherPhone = this.rs.getString("publisherPhone");
            String publisherEmail = this.rs.getString("publisherEmail");

            //Display values
            System.out.printf(this.displayFormat,
                    dispNull(publisherName), dispNull(publisherAddress), dispNull(publisherPhone), dispNull(publisherEmail));
        }

    }

    public void case4() throws SQLException {
        boolean valid = false;

        do {
            System.out.print("Please Enter a Publisher Name: ");
            String userInput = this.cases.nextLine().trim();

            this.sql = "SELECT publisherName, publisherAddress, publisherPhone, publisherEmail FROM publishers WHERE publisherName =  ?";

            this.pstmt = this.conn.prepareStatement(this.sql);
            this.pstmt.setString(1, userInput);

            this.rs = this.pstmt.executeQuery();

            if (!this.rs.next()) {
                System.out.println("Publisher Name is not in the DataBase Try Again.");
            } else {
                this.rs = this.pstmt.executeQuery();
                valid = true;

                while (this.rs.next()) {
                    //Retrieve by column name
                    String publisherName = this.rs.getString("publisherName");
                    String publisherAddress = this.rs.getString("publisherAddress");
                    String publisherPhone = this.rs.getString("publisherPhone");
                    String publisherEmail = this.rs.getString("publisherEmail");
                    //Display values
                    System.out.printf(this.displayFormat,
                            dispNull(publisherName), dispNull(publisherAddress), dispNull(publisherPhone), dispNull(publisherEmail));
                }
            }
        } while (!valid);
    }

    public void case5() throws SQLException {
        this.stmt = this.conn.createStatement();

        this.sql = "SELECT groupName, bookTitle, publisherName, numberPages FROM books";
        this.rs = this.stmt.executeQuery(sql);

        System.out.printf(this.displayFormat, "Group Name", "Book Title", "Publisher Name", "Number of Pages");
        while (this.rs.next()) {
            //Retrieve by column name
            String groupName = this.rs.getString("groupName");
            String bookTitle = this.rs.getString("bookTitle");
            String publisherName = this.rs.getString("publisherName");
            String numberPages = this.rs.getString("numberPages");

            //Display values
            System.out.printf(this.displayFormat,
                    dispNull(groupName), dispNull(bookTitle), dispNull(publisherName), dispNull(numberPages));
        }
    }

    public void case6() throws SQLException {
        boolean valid = false;
        do {
            System.out.print("Please Enter a Book Title: ");
            String userInput = this.cases.nextLine().trim();

            this.sql = "SELECT groupName, bookTitle, publisherName, numberPages FROM books WHERE bookTitle =  ?";

            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1, userInput);

            this.rs = this.pstmt.executeQuery();

            if (!this.rs.next()) {
                System.out.println("BookTitle is not in the DataBase Try Again.");
            } else {
                this.rs = this.pstmt.executeQuery();
                valid = true;
                System.out.printf(this.displayFormat, "Group Name", "Book Title", "Publisher Name", "Number of Pages");
                while (this.rs.next()) {
                    //Retrieve by column name
                    String groupName = this.rs.getString("groupName");
                    String bookTitle = this.rs.getString("bookTitle");
                    String publisherName = this.rs.getString("publisherName");
                    String numberPages = this.rs.getString("numberPages");
                    //Display values
                    System.out.printf(this.displayFormat,
                            dispNull(groupName), dispNull(bookTitle), dispNull(publisherName), dispNull(numberPages));
                }
            }
        } while (!valid);
    }

    public void case7() throws SQLException {
        boolean valid = false;

        do {
            String groupName;
            do {
                System.out.print("Please Enter a Group Name to Insert: ");
                groupName = this.cases.nextLine().trim();
            } while (!checkValid("SELECT groupName, headWriter, yearFormed, subject FROM writingGroups WHERE groupName = ?", groupName));

            System.out.print("Please Enter a Book Title to Insert: ");
            String bookTitle = this.cases.nextLine().trim();

            String publisherName;
            do {
                System.out.print("Please Enter a Publisher Name to Insert: ");
                publisherName = this.cases.nextLine().trim();
            } while (!checkValid("SELECT publisherName, publisherAddress, publisherPhone, publisherEmail FROM publishers WHERE publisherName = ?", publisherName));

            System.out.print("Please Enter Year of Publication: ");
            int yearPublished = this.cases.nextInt();

            System.out.print("Please Enter Number of Pages of Book: ");
            int numberPages = this.cases.nextInt();

            this.sql = "SELECT groupName, bookTitle, publisherName, yearPublished, numberPages FROM books WHERE groupName = ? AND bookTitle = ?";
            this.pstmt = this.conn.prepareStatement(this.sql);
            this.pstmt.setString(1, groupName);
            this.pstmt.setString(2, bookTitle);
            this.rs = this.pstmt.executeQuery();

            if (this.rs.next()) {
                System.out.println("The GroupName and BookTitle combination already exists. Try Again.");
            } else {
                valid = true;
                this.sql = "INSERT INTO  books (groupName, bookTitle, publisherName, yearPublished,numberPages)VALUES  (?,?,?,?,?)";
                this.pstmt = this.conn.prepareStatement(this.sql);
                this.pstmt.setString(1, groupName);
                this.pstmt.setString(2, bookTitle);
                this.pstmt.setString(3, publisherName);
                this.pstmt.setInt(4, yearPublished);
                this.pstmt.setInt(5, numberPages);

                if (this.pstmt.executeUpdate() > 0) {
                    System.out.println("Inserted a New Book into the DataBase.");
                } else {
                    System.out.println("Failed to Update Books");
                }
            }
        } while (!valid);

        //Checks for duplicates before INSERT
    }

    public void case8() throws SQLException {

        boolean valid = false;
        boolean valid2 = false;
        String publisherUpdate;
        String newPublisherName;

        do {

            System.out.print("Please Enter the new Publisher Name: ");
            publisherUpdate = this.cases.nextLine().trim();
            this.sql = "SELECT publisherName, publisherAddress, publisherPhone, publisherEmail FROM publishers WHERE publisherName = ?";

            if (checkValid(this.sql, publisherUpdate)) {
                System.out.println("The publisher already is in the Table");
            } else {
                valid = true;
            }
        } while (!valid);

        System.out.print("Please Enter the new Publisher Address: ");
        String publisherAddress = this.cases.nextLine().trim();
        System.out.print("Please Enter the new Publisher Phone Number: ");
        String publisherPhone = this.cases.nextLine().trim();
        System.out.print("Please Enter the new Publisher Email: ");
        String publisherEmail = this.cases.nextLine().trim();

        do {
            System.out.print("Please Enter a Publisher Name to Update: ");
            newPublisherName = this.cases.nextLine().trim();
            this.sql = "SELECT publisherName, publisherAddress, publisherPhone, publisherEmail FROM publishers WHERE publisherName = ?";

            if (!checkValid(this.sql, newPublisherName)) {
                System.out.println("The publisher isn't in the DataBase. Try Again");
            } else {
                valid2 = true;
            }
        } while (!valid2);

        this.sql = "INSERT INTO publishers (publisherName, publisherAddress, publisherPhone, publisherEmail) VALUES (?, ? , ?, ?)";
        this.pstmt = this.conn.prepareStatement(this.sql);
        this.pstmt.setString(1, publisherUpdate);
        this.pstmt.setString(2, publisherAddress);
        this.pstmt.setString(3, publisherPhone);
        this.pstmt.setString(4, publisherEmail);
        if (this.pstmt.executeUpdate() > 0) {
            System.out.println("Inserted a New Publisher");
        } else {
            System.out.println("Failed to Insert New Publisher");
        }

        this.sql = "UPDATE books set publisherName = ? where publisherName = ?";
        this.pstmt = this.conn.prepareStatement(this.sql);
        this.pstmt.setString(1, publisherUpdate);
        this.pstmt.setString(2, newPublisherName);
        if (this.pstmt.executeUpdate() > 0) {
            System.out.println("Updated Books with New Publisher");
        } else {
            System.out.println("Failed to Update Books");
        }

    }

    public void case9() throws SQLException {
        boolean valid = false;

        do {
            System.out.print("Please Enter the Group Name of the Book: ");
            String groupName = this.cases.nextLine().trim();
            System.out.print("Please Enter the book you want to remove: ");
            String bookTitle = this.cases.nextLine().trim();

            this.sql = "SELECT groupName, bookTitle, publisherName, yearPublished, numberPages FROM books WHERE groupName = ? AND bookTitle = ?";
            this.pstmt = this.conn.prepareStatement(this.sql);
            this.pstmt.setString(1, groupName);
            this.pstmt.setString(2, bookTitle);
            this.rs = this.pstmt.executeQuery();

            if (!this.rs.next()) {
                System.out.println("The Book you are trying to Remove is not in the Table. Try Again.");
            } else {
                valid = true;
                this.sql = "DELETE FROM books where groupName = ? and bookTitle = ?";
                this.pstmt = this.conn.prepareStatement(this.sql);
                this.pstmt.setString(1, groupName);
                this.pstmt.setString(2, bookTitle);
                if (this.pstmt.executeUpdate() > 0) {
                    System.out.println("You Deleted a Book From the DataBase");
                } else {
                    System.out.println("Failed to Delete Book.");
                }
            }
        } while (!valid);
    }

    public boolean checkValid(String sql, String userInput) {

        boolean result = false;

        try {
            this.pstmt = this.conn.prepareStatement(sql);
            this.pstmt.setString(1, userInput);
            this.rs = this.pstmt.executeQuery();
            result = this.rs.next();
        } catch (SQLException e) {
            System.out.println("Bad SQL Statement Sorry Try Again.");
            //e.printStackTrace();
        }

        return result;
    }
}