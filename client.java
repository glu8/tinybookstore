import java.util.*;
import java.net.URL;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class client {

	public static int callOperation(XmlRpcClient client, String call, Vector<String> params) {
		try {
			//System.out.println("callOperation: " + call);
			String cmd = "sample." + call;
			//System.out.println(cmd);
			//System.out.println("param: " + params);
			Object[] result = (Object[]) client.execute(cmd, params.toArray());
			//System.out.println(result[0]);
			for (int i = 0; i < result.length; i++) {
				System.out.println(result[i]);
			}
			return 0;
		} catch (Exception exception) {
			System.err.println("Client: " + exception);
			return -1;
		}
	}

	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		XmlRpcClient client = null;
		try {
			config.setServerURL(new URL("http://" + host + ":" + 8888));
			client = new XmlRpcClient();
			client.setConfig(config);
		} catch (Exception e) {
			System.out.println("Problem!");
		}

		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Input a request");
			String input = scanner.nextLine();
			// System.out.println("Input received: " + input);
			String[] tokens = input.split("[\\(||\\)]");
			// System.out.println(tokens[0] + "; " + tokens[1]);//"Token 1: %s Token 2:
			// %s\n", tokens[0], tokens[1]);

			Vector<String> params = new Vector<String>();

			switch (tokens[0]) {
				case "search":
					//System.out.printf("calling search %s\n", tokens[1]);
					params.addElement(tokens[1]);
					if (callOperation(client, "search", params) < 0) {
						System.out.println("Error...");
					}
					break;
				case "lookup":
					//System.out.printf("calling lookup %s\n", tokens[1]);
					params.addElement(tokens[1]);
					if (callOperation(client, "lookup", params) < 0) {
						System.out.println("Error...");
					}
					break;
				case "buy":
					//System.out.printf("calling buy %s\n", tokens[1]);
					params.addElement(tokens[1]);
					if (callOperation(client, "buy", params) < 0) {
						System.out.println("Error...");
					}
					break;
				default:
					System.out.printf("Invalid request\n");
			}
		}
	}
}