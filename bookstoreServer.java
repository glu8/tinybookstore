import org.apache.xmlrpc.webserver.WebServer;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.XmlRpcException;
import java.sql.*;
import java.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class bookstoreServer {

    public Integer[] SumAndDifference(int x, int y) {
        Integer[] array = new Integer[2];
        array[0] = new Integer(x + y);
        array[1] = new Integer(y - x);
        return array;
    }

    public ArrayList search(String topic) {

        Connection c = null;
        Statement stmt = null;
        ArrayList array = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM BOOKS WHERE TOPIC = '%s'", topic));


            while (rs.next()) {
                String title = rs.getString("TITLE");
                String id = Integer.toString(rs.getInt("ID"));  
                array.add(title);
                array.add(id);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return array;
    }

    public ArrayList lookup(String item_number) {

        Connection c = null;
        Statement stmt = null;
        ArrayList array = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM BOOKS WHERE ID = %s", item_number));

            while (rs.next()) {
                String title = rs.getString("TITLE");
                String topic = rs.getString("TOPIC");
                String stock = Integer.toString(rs.getInt("STOCK"));    
                String price = Float.toString(rs.getFloat("PRICE"));

                array.add(title);
                array.add(topic);
                array.add(price);
                array.add(stock);


            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return array;
    }

    public ArrayList buy(String item_number) {

        Connection c = null;
        Statement stmt = null;
        ArrayList array = new ArrayList<String>();
        try{
            FileWriter csvWriter = new FileWriter("log.csv", true);
       

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            int rows = stmt.executeUpdate(String.format("UPDATE BOOKS SET STOCK = STOCK - 1 WHERE STOCK > 0 AND ID = %s", item_number));
            c.commit();

            String purchaseStatusMsg;
            if (rows < 1){
                purchaseStatusMsg = "Purchase Denied: Item Out of Stock";
                } else {
                purchaseStatusMsg = String.format("Purchase Successful! Bought book %s. Enjoy Your Book :)", item_number);
                csvWriter.append(item_number);
                csvWriter.append("\n");
    
            }

            array.add(purchaseStatusMsg);
            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        
        
        csvWriter.flush();
        csvWriter.close();

    }catch (IOException e) {
        System.out.println("file exception");
        }
        return array;
    }

    public static boolean update(String item_number, String price) {

        Connection c = null;
        Statement stmt = null;

        boolean success = false;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            int rows = stmt.executeUpdate(String.format("UPDATE BOOKS SET PRICE = %s WHERE ID = %s", price, item_number));
            c.commit();

            if (rows > 0){
                success = true;
                } 


            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return success;
    }



    public static boolean restock(String item_number, String amount) {

        Connection c = null;
        Statement stmt = null;
        ArrayList array = new ArrayList<String>();

        boolean success = false;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM BOOKS WHERE ID = %s", item_number));
            
            int stock = rs.getInt("STOCK");    

            stock = stock + Integer.parseInt(amount);

            stmt = c.createStatement();
            int rows = stmt.executeUpdate(String.format("UPDATE BOOKS SET STOCK = %d WHERE ID = %s", stock, item_number));
            c.commit();

            String updateStatusMsg;
            if (rows > 0){
                success = true;
                } 


            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return success;
    }

    public static synchronized void log() {

        String csvFile = "log.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("End of log");


        } catch (Exception e) {
            System.out.println("No purchases made");
        }

    }


    public static void main(String[] args) {
        try {

            bookstoreServer bookServer = new bookstoreServer();

            PropertyHandlerMapping phm = new PropertyHandlerMapping();
            XmlRpcServer xmlRpcServer;

            // set up the webserver
            WebServer server = new WebServer(8888);
            xmlRpcServer = server.getXmlRpcServer();
            phm.addHandler("sample", bookstoreServer.class);
            xmlRpcServer.setHandlerMapping(phm);
            server.start();
            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");

            while (true) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Input a request");
                String input = scanner.nextLine();
                // System.out.println("Input received: " + input);
                String[] tokens = input.split("[ ]");
                // System.out.println(tokens[0] + "; " + tokens[1]);//"Token 1: %s Token 2:
                // %s\n", tokens[0], tokens[1]);
                
                Vector<String> params = new Vector<String>();
    
                switch (tokens[0]) {
                    case "log":
                        System.out.printf("calling log\n");
                        log();
                        break;
                    case "update":
                        System.out.printf("calling update\n");
                        if(update(tokens[1], tokens[2])){
                            System.out.println("successful update!");
                        } else{
                            System.out.println("unsuccessful update!");
                        };
                        break;
                    case "restock":
                        System.out.printf("calling restock\n");
                        if(restock(tokens[1], tokens[2])){
                            System.out.println("successful restock!");
                        } else{
                            System.out.println("unsuccessful restock!");
                        };
                        break;
                    default:
                        System.out.printf("Invalid request\n");
                }
            }



        } catch (Exception exception) {
            System.err.println("JavaServer: " + exception);
        }
    }
}
