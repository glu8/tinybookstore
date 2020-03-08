import org.apache.xmlrpc.webserver.WebServer;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.XmlRpcException;
import java.sql.*;
import java.util.*;

public class bookstoreServer {

    public Integer[] SumAndDifference(int x, int y) {
        Integer[] array = new Integer[2];
        array[0] = new Integer(x + y);
        array[1] = new Integer(y - x);
        return array;
    }

    public ArrayList getByTopic(String topic) {

        Connection c = null;
        Statement stmt = null;
        ArrayList array = new ArrayList<String>();

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:test.db");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS WHERE TOPIC = 'College Life';");


            while (rs.next()) {
                String title = rs.getString("TITLE");
                String id = Integer.toString(rs.getInt("ID"));  
                array.add(title);
                array.add(id);
                System.out.println(array);
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

    public static void main(String[] args) {
        try {

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
        } catch (Exception exception) {
            System.err.println("JavaServer: " + exception);
        }
    }
}
