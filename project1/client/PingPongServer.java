import rmi.*;

public interface PingPongServer {
	
	public String ping(int id) throws RMIException;

}
