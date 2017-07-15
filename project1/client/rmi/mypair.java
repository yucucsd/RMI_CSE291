package rmi;
import java.io.*;

public class mypair implements Serializable {
	protected boolean flag;
	protected Object result;
	
	public mypair(boolean flag, Object result) {
		// TODO Auto-generated constructor stub
		this.flag = flag;
		this.result = result;
	}

}
