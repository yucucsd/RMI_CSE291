package rmi;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

class Listener extends Thread {

	private Skeleton ske = null;// the skeleton related with this listener thread
	private ServerSocket ssocket = null;// the socket of this listener thread

	public Listener() {
		// TODO Auto-generated constructor stub
	}

	

	public Listener(ThreadGroup group, Runnable target, String name, long stackSize) {
		super(group, target, name, stackSize);
		// TODO Auto-generated constructor stub
	}

	public Listener(Skeleton ske, ServerSocket server) {
		this.ske = ske;
		this.ssocket = server;
	}

	public void run(){
		//System.out.println("in listenner run"); 
		while(this.ske.isrunning){//
			try{
				//System.out.println("listener isrunning " + this.ske.isrunning);
				//System.out.println("hellllo");
				Socket myclient = this.ssocket.accept();
		//		System.out.println("hellllo2");
				
				Skehandler clienthandler = new Skehandler(this.ske, myclient);
				if(this.ske.isrunning){
					//System.out.println("about to start skehandler in listener run");
					clienthandler.start();
		//			System.out.println("clienthandler started");
				}
			}
			catch (IOException name) {
		//		System.out.println("listener run io exception");
			}	
			
		}
		
		//System.out.println("listener while break ");
	}
}
