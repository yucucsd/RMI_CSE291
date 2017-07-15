import rmi.*;

public class PingServerFactory {
	public ServerImpl makePingServer() {
		return new ServerImpl();
	}
}
