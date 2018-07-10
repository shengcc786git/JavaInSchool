import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class race_sever implements Runnable {
	private ServerSocket seversock = null;
	private ExecutorService SocketThread = Executors.newCachedThreadPool();	
	private Vector client_out_arr;
	
	public void Listensever(int port)	{
		client_out_arr = new Vector();
		try {
			//建立ServerSocket
			this.seversock = new ServerSocket(port);
			System.out.println("Server Start port " + port);
			
			while( true ) {
				Socket clientsock = this.seversock.accept();
				SocketThread.execute( new SocketProcess(clientsock) );
				
				//int SocketCount = (Thread.activeCount() - 2);
				//System.out.println( seversock.getLocalSocketAddress() + 
				//					"目前有" + SocketCount + "個連線"); 
			}	
		}catch ( IOException e ) {
			e.printStackTrace();
		}finally {
			if( SocketThread != null ) {
				SocketThread.shutdown();
			}
			if( seversock != null ) {
				try {
					seversock.close();
				}catch( IOException e ) {
					e.printStackTrace();
                }
			}
        }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.Listensever(2000);
	}
	
	/*
	public static void main( String[] args ) {
		Thread socket_thread = new Thread(new race_sever());
		socket_thread.start();
    }*/

	class SocketProcess implements Runnable {
		private Socket clientsock = null;
		
		public SocketProcess(Socket clientsock) {
			// TODO Auto-generated constructor stub
			this.clientsock = clientsock;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String Send_cmd = "";
			String Get_cmd = "";
			boolean Socket_Survive = true;
			DataInputStream in = null;
			DataOutputStream out = null;
			try {
				in = new DataInputStream( this.clientsock.getInputStream() );
				out = new DataOutputStream( this.clientsock.getOutputStream() );
				client_out_arr.add(out);
				out.writeUTF("Hi, " + clientsock.getInetAddress().getHostAddress());
				out.flush();
				
				// TODO 處理程序
				while( Socket_Survive == true ) {
					Get_cmd = in.readUTF();
					
					String[] Get_cmd_array = Get_cmd.split(";");
					switch( Get_cmd_array[0] ) {
						case "bet_horse":
							System.out.println(Get_cmd_array[1] + " & " + Get_cmd_array[2]);
							break;
						case "stop":
							Send_cmd = "stop";
							break;
					}

					if(Send_cmd != ""){
						System.out.println("Send_cmd：" + Send_cmd);
						out.writeUTF(Send_cmd);
						out.flush();
						switch( Send_cmd ) {
							case "test":
								break;
							case "stop":
								Socket_Survive = false;
								break;
						}
						Send_cmd = "";
					}
					
				}
			}catch( EOFException e ) {
				System.out.println( "EOF_Exception" );
			}catch( SocketException e ) {
				System.out.println( "Client 意外中斷" );
			}catch( IOException e ) {
				e.printStackTrace();
			}finally {
				try {
					if( in != null )
						in.close();
						
					if( out != null )
						out.close();
					
					if ( this.clientsock != null && !this.clientsock.isClosed() ){
						this.clientsock.close();
					}
				}catch( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void send_all_client(String Send_cmd){
		System.out.println("Send_cmd：" + Send_cmd);
		String[] Send_cmd_array = Send_cmd.split(";");
		
		Iterator out_arr = client_out_arr.iterator();
		while(out_arr.hasNext()){          
			try {
				DataOutputStream out = (DataOutputStream) out_arr.next();			
				
				switch( Send_cmd_array[0] ) {
					case "horse_rank":
						out.writeUTF("horse_bet_res;" + Send_cmd_array[1]);
						out.flush();
						break;
				}
			}catch(Exception ex) {
				System.out.println("send_all_client ERROR");
			}
		}
	}
}
