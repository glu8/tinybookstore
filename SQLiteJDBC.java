import java.sql.*;

public class SQLiteJDBC {
    public static void main( String args[] ) {
	Connection c = null;
	Statement stmt = null;

	try {
	    Class.forName("org.sqlite.JDBC");
	    c = DriverManager.getConnection("jdbc:sqlite:test.db");
	} catch ( Exception e ) {
	    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    System.exit(0);
	}
	System.out.println("Opened database successfully");

	try {
	    //Class.forName("org.sqlite.JDBC");
	    //c = DriverManager.getConnection("jdbc:sqlite:test.db");
	    c.setAutoCommit(false);
	    System.out.println("Opened database successfully");

		stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery( "SELECT * FROM BOOKS WHERE TOPIC = 'College Life';" );
      
      while ( rs.next() ) {
         String  title = rs.getString("TITLE");
         int id  = rs.getInt("ID");
         
		 System.out.println( "TITLE = " + title );
         System.out.println( "ID = " + id );
         System.out.println();
      }
      rs.close();
      stmt.close();
      c.close();

/*
	    stmt = c.createStatement();
	    String sql = "INSERT INTO BOOKS (ID,TITLE,TOPIC,STOCK,PRICE) " +
			 "VALUES (53477, 'Achieve Less Bugs and More Hugs', 'Distributed Systems', 1,10 );";
	    stmt.executeUpdate(sql);

	    sql = "INSERT INTO BOOKS (ID,TITLE,TOPIC,STOCK,PRICE) " +
			 "VALUES (53573, 'Distributed Systems for Dummies', 'Distributed Systems', 1, 10 );";
	    stmt.executeUpdate(sql);

	    sql = "INSERT INTO BOOKS (ID,TITLE,TOPIC,STOCK,PRICE) " +
			 "VALUES (12365, 'Surviving College', 'College Life', 1, 10 );";
	    stmt.executeUpdate(sql);
	    
	    sql = "INSERT INTO BOOKS (ID,TITLE,TOPIC,STOCK,PRICE) " +
			 "VALUES (12498, 'Cooking for the Impatient Undergraduate', 'College Life', 1, 10 );";
	    stmt.executeUpdate(sql);
	    
	    stmt.close();
		
	    c.commit();
	    c.close();
		*/
	} catch ( Exception e ) {
	    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    System.exit(0);
	}


  
    }
}
