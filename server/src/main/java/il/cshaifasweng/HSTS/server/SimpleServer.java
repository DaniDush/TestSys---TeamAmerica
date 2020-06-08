package il.cshaifasweng.HSTS.server;

import il.cshaifasweng.HSTS.server.ocsf.AbstractServer;
import il.cshaifasweng.HSTS.server.ConnectToDB;
import il.cshaifasweng.HSTS.entities.Carrier;
import il.cshaifasweng.HSTS.entities.CarrierType;

import il.cshaifasweng.HSTS.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.*; 

public class SimpleServer extends AbstractServer {

	public ConnectToDB dbConnector;	   
	
	public SimpleServer(int port) {
		super(port);
		this.dbConnector = new ConnectToDB();
		dbConnector.connectToDB();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Received Message From Client");
		// where is the carrier? said unused
		
		Carrier msgFromClient = null;
		msgFromClient = (Carrier)msg;
		switch(msgFromClient.carrierType){
			case(CarrierType.User ):
				handleUserMessage(msgFromClient, client);
		}
	}
	
	protected void handleUserMessage(Carrier carrier, ConnectionToClient client) {
		String UserNameFromClient = msgFromClient.carrierMessageMap.get("userName");
		String PassFromClient = msgFromClient.carrierMessageMap.get("pass");
		int checkedRole = UserController.getRole(UserNameFromClient, PassFromClient);
		
		System.out.println("checkedRole is " + checkedRole);
		
		Carrier msg2SimpleClient = new Carrier();
		msg2SimpleClient.carrierType = CarrierType.User 
		msg2SimpleClient.carrierMessageMap.put("userName", "Daniel"); 
		msg2SimpleClient.carrierMessageMap.put("pass", "Alexey");
		msg2SimpleClient.carrierMessageMap.put("role", "1");
		System.out.println("user is " + UserNameFromClient);
		System.out.println("pass is " + PassFromClient);
		try{
			client.sendToClient(msg2SimpleClient);
		}
		catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void clientConnected(ConnectionToClient client) {
		super.clientConnected(client);
		System.out.println("Client connected: " + client.getInetAddress());
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Connecting");
		if (args.length != 1) {
			SimpleServer server = new SimpleServer(3002);
			server.listen();
			//System.out.println("Required argument: <port>");
			// TODO - create a login screen for server where we enter the port
		} else {
			SimpleServer server = new SimpleServer(Integer.parseInt(args[0]));
			server.listen();
		}
	}
	

	
	

}
