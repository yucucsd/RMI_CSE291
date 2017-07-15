import rmi.*;

public class ServerImpl implements PingPongServer{
	
	public String ping(int id) throws RMIException{
		return "pong " + String.valueOf(id);
	};
	
}
