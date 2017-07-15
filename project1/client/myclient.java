import rmi.*;
import java.net.*;
import java.io.*;

public class myclient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 3) {
			System.err.println("Usage: java myclent  <hostname> <port number> <idNumber>");
			System.exit(1);
		}
		String hostname = args[0];
		int id = Integer.valueOf(args[2]);
		int portNumber = Integer.valueOf(args[1]);

		InetSocketAddress address = new InetSocketAddress(hostname, portNumber);
		PingPongServer server = Stub.create(PingPongServer.class, address);
		int count = 0;
		try {
			for (int i = 0; i < 4; ++i) {
				String res = server.ping(id);
				if (res.equals("pong " + id)) {
					count++;
				}
				System.out.println((i + 1) + " Tests Completed, " + (i + 1 - count) + " Tests Failed");
			}
		} catch (RMIException e) {
			System.out.println("RMI failed");
		}
	}

}
