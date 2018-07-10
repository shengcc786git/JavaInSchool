import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class race_client implements Runnable {
	private String host = "127.0.0.1";
	private int port = 2000;
	private Socket socket = null;
	private DataInputStream in = null;
	private DataOutputStream out = null;
	private race_client_frm RaceClientFrm = null;
	
	public void ClientStart() {
		String Get_cmd = "";
		boolean Socket_Survive = true;
		
		try {
			socket = new Socket( this.host, this.port );
			in = new DataInputStream( socket.getInputStream() );
			out = new DataOutputStream( socket.getOutputStream() );
			System.out.println( in.readUTF() );
			
			int count = 0;
			// TODO ³B²zµ{§Ç
			while( Socket_Survive == true ) {
				//this.Send_cmd_to_Server("test");
				Get_cmd = in.readUTF();
				System.out.println("Send_cmd¡G" + Get_cmd);
				
				String[] Get_cmd_array = Get_cmd.split(";");
				
				switch( Get_cmd_array[0] ) {
					case "horse_bet_res":
						if(Get_cmd_array[1].equals(RaceClientFrm.get_predict_No1_name())){
							RaceClientFrm.bet_win(Get_cmd_array[1]);
						}else{
							RaceClientFrm.bet_lose(Get_cmd_array[1]);
						}
						break;
					case "stop":
						Socket_Survive = false;
						break;
				}
			}
		}catch(EOFException e) {
			System.out.println( "EOF_Exception" );
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
				if(socket != null)
					socket.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Send_cmd_to_Server(String Send_cmd) {
		try {
			if(Send_cmd != ""){
				System.out.println("Send_cmd¡G" + Send_cmd);
				out.writeUTF(Send_cmd);
				out.flush();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.ClientStart();
	}
	
	public void set_race_client_frm(race_client_frm RaceClientFrm){
		this.RaceClientFrm = RaceClientFrm;
	}
	
	/*public static void main( String[] args ) {
		Thread socket_thread = new Thread(new race_client());
		socket_thread.start();
	}*/
}
