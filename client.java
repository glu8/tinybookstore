import java.util.*;
import java.net.URL;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class Client {
    public static void main (String [] args) {
	String host = (args.length < 1) ? null : args[0];
	XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	XmlRpcClient client=null;
	try {
	    config.setServerURL(new URL("http://" + host + ":" + 8888));
	    client = new XmlRpcClient();
	    client.setConfig(config);
	} catch (Exception e) {
	    System.out.println("Problem!");
	}

	Vector<Integer> params = new Vector<Integer>();
	params.addElement(new Integer(10));
	params.addElement(new Integer(40));

	try {
	    Object[] result = (Object[])client.execute("sample.SumAndDifference", params.toArray());

	    int sum = ((Integer) result[0]).intValue();
	    System.out.println("The sum is: "+ sum);
	    
	    int diff = ((Integer) result[1]).intValue();
	    System.out.println("The difference is: "+ diff);
	    
	} catch (Exception exception) {
	    System.err.println("Client: " + exception);
	}
    }
}