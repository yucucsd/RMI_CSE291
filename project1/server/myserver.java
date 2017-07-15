import rmi.*;

import java.io.IOException;
import java.net.*;

public class myserver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PingServerFactory factory = new PingServerFactory();
		ServerImpl server = factory.makePingServer();
		if (args.length != 1) {
			System.err.println("Usage: java myserver  <port number>");
			System.exit(1);
		}
		int portNumber = Integer.valueOf(args[0]);
		try {
			InetSocketAddress address = new InetSocketAddress("127.0.0.1", portNumber);
			Skeleton<PingPongServer> skeleton = new Skeleton(PingPongServer.class, server, address);
			skeleton.start();

		} catch (RMIException e) {

		}
	}

}
