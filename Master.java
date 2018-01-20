/*Master program - 012419134*/

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;

/*Master Class*/
public class Master
{
	/*Declaration*/
	int port;																	// Port number declaration
	ServerSocket ss;															// Server socket object
	MasterThread mt = new MasterThread();										// MasterThread object
	Thread sthread;																// Thread object
	static ArrayList<ClientInfo> clientList = new ArrayList<ClientInfo>();		// Array to hold list of clients
	
	/*constructor*/
	public Master(int port)
	{
		try {
			this.port = port;
			this.ss = new ServerSocket(port);
			this.sthread  = new Thread(this.mt); 
			this.sthread.start(); 												// Starting the Master Thread
		}
		catch (IOException e)	{	e.printStackTrace();	}
	}
	
	/*Client Attributes to hold client information*/
	private static class ClientInfo {
		private String hname;													// Host name
		private String ip;														// Host IP Address
		private int port;														// Port Number
		private Socket sock;
		private Date rDate;
		private boolean connected;

		public ClientInfo(String hname, String ip, int port, Socket sock, Date rDate)
		{
			this.hname = hname;
			this.ip = ip;
			this.port = port;
			this.sock = sock;
			this.rDate = rDate;
		}
		
		/*Methods to return client information*/
		public String getHostname()		{	return hname;	}
		public String getIpaddress()	{	return ip;		}
		public int getPort()			{	return port;	}
		public Socket getSocket()		{	return sock;	}
		public Date getDate()			{	return rDate;	}

		public boolean isConnected() {
			return connected;
		}

		public void setConnected(boolean connected) {
			this.connected = connected;
		}
	}
	
	/*Main method*/
	public static void main(String[] args)
	{
		int port = 0;
		if((args.length==2) && (args[0].equals("-p"))){
			port = Integer.parseInt(args[1]);
			System.out.println("Server Thread created");
		}
		else{
			System.out.println("\nIncorrect set of arguments");
			System.exit(1);
		}
		@SuppressWarnings("unused")
		Master m = new Master(port);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		while(true)
		{
			String cmd = null;
			try
			{
				System.out.print("> ");
				cmd = buffer.readLine();
				if(cmd.equals("exit")){
					System.out.println("\nExit command executed");
					System.exit(0);
				}
				if(cmd!= null)
				{
					String[] cmd_split = cmd.split(" ");
					if(cmd_split.length <= 0)
						return;
					
					/*List command*/
					else if(cmd_split[0].equals("list")){
							System.out.println("Slave_Host_Name       IPAddress       Source_Port_Number       Registration_Date");
							for(ClientInfo i: clientList)
							{
								System.out.print(i.getHostname() + "              ");
								System.out.print(i.getIpaddress() + "             ");
								System.out.print(i.getPort() + "              ");
								System.out.print(i.getDate() + "              \n");
							}
					}
					
					/*Connect Command*/
					else if(cmd_split[0].equals("connect")){
							for(ClientInfo i : clientList)
							{
								System.out.println("Connection to " + cmd_split[2] + " successful");
								PrintWriter pw = new PrintWriter(i.getSocket().getOutputStream(), true);
								pw.println(cmd);
							}
					}
					
					/*Disconnect Command*/
					else if(cmd_split[0].equals("disconnect")){
							for(ClientInfo i : clientList)
							{
								System.out.println("Connection terminated with " + cmd_split[2]);
								PrintWriter pw = new PrintWriter(i.getSocket().getOutputStream(), true);
								pw.println(cmd);
							}
					}
					
					/*Rise-fake-url Command*/
					else if(cmd_split[0].equals("rise-fake-url")){
						if(cmd_split.length != 3 ) {
							System.out.println("\nIncorrect format of arguments");
							System.exit(1);
						}
						else if(Integer.parseInt(cmd_split[1]) == port) {
							System.out.println("\nCannot establish connection to a port already in use\n");
							System.exit(1);
						}
						
						else {
							for(ClientInfo i : clientList)
							{
								System.out.println("Fake Url created");
								PrintWriter pw = new PrintWriter(i.getSocket().getOutputStream(), true);
								pw.println(cmd);
								if (i.isConnected()) {
									continue;
								}
								i.setConnected(true);
								break;
							}
						}
						
					}
					
					/*Down-fake-url command*/
					else if(cmd_split[0].equals("down-fake-url")) {
						if(cmd_split.length != 3) {
							System.out.println("\nIncorrect format of arguments");
							System.exit(1);
						}
						else if(Integer.parseInt(cmd_split[1]) == port) {
							System.out.println("\nCannot close Master port\n");
							System.exit(1);
						}
						
						else {
							for(ClientInfo i : clientList)
							{
								System.out.println("Connection brought down");
								PrintWriter pw = new PrintWriter(i.getSocket().getOutputStream(), true);
								pw.println(cmd);
								break;
							}
						}
					}
					
					else{
							System.out.println("Not a valid command.");
							System.exit(1);
							break;
					}
				}
			} catch (IOException e){	e.printStackTrace();	}
		}
	}

	private class MasterThread implements Runnable
	{
		/*Overriding the run() method*/
		public void run()
		{
			try
			{
				while(true)
				{
					Socket sc = ss.accept();
					InetAddress inet = sc.getInetAddress();
					String hname = inet.getHostName();
					String ip = inet.getHostAddress();
					int pno = port;
					Date rDate = new Date(System.currentTimeMillis());
					ClientInfo cl = new ClientInfo(hname, ip, pno, sc, rDate);			// Passing the client information to array
					clientList.add(cl);													// Client information added to the array
				}
			}catch (IOException e)	{	e.printStackTrace();	}
		}
	}	
}