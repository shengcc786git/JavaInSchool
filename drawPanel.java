import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class drawPanel extends JPanel implements Runnable {//drawPanel去完成thread賽馬
		private static int start_X = 0, end_X = 870;
		private static int horse_X[] = {0, 0, 0, 0};
		private static int horse_Y[] = {0, 150, 300, 450};
		private static int move_X[] = {25, 40, 30, 60};
		private static Image img_icon[] = new Image[5];
		private static String horse_name[] = {"黑馬", "赤兔馬", "黑暗獨角獸", "彩虹小馬"};
		private static String horse_place[] = new String[4];
		private static int place = 0;
		private static race_sever socket_server;
		
		Thread horse_Thread;
		
		drawPanel() throws IOException{
			File file[] = new File[5];
			for(int i=0; i<4; i++){
				file[i] = new File("horse"+i+".png");
				img_icon[i] = ImageIO.read(file[i]);
			}
			file[4] = new File("meadow.png");
			img_icon[4] = ImageIO.read(file[4]);
			
			for(int i=0; i<4; i++)
				horse_X[i] = 0;
			place = 0;
			
			horse_Thread = new Thread(this);
		}
		
	    public void paintComponent( Graphics g ) {
	    	super.paintComponent( g );
			this.setBackground(Color.BLUE);	
			g.drawImage(img_icon[4], 0, 0, this);
			for(int i=0; i<4; i++){
				g.drawImage(img_icon[i], horse_X[i], horse_Y[i],this);
			}
	    }

		public void stop(){
			horse_Thread = null;	//將執行緒設為null
		}
		
		public void restart(){
			place = 0;
			for(int i=0; i<4; i++){
				horse_X[i] = 0;
				horse_place[i] = "";
			}
			repaint();
			horse_Thread = new Thread(this);
		}
			    
		@Override
		public void run() {
			// TODO Auto-generated method stub			
			
			while(horse_Thread != null){
				repaint();
				for(int i=0; i<4; i++){
					try{
						Thread.sleep(100);             
					}catch(InterruptedException e){
						
					};
					
					if(judgment(horse_X[i], i) == false){
						if(i == 0){
							int offset = ((int)(Math.random()*100));
							horse_X[i] = horse_X[i] + move_X[i] + offset;
							set_End_X(horse_X[i], i);
						}
						
						if(i == 1){
							int offset = 0;
							if(((int)(Math.random()*50))%2 == 1 && 
								((int)(Math.random()*50))%2 == 1 ){
								offset = 120;
							}
							horse_X[i] = horse_X[i] + move_X[i] + offset;
							set_End_X(horse_X[i], i);
						}
						
						if(i == 2){
							int offset = 0;
							if(((int)(Math.random()*50))%2 == 1){
								offset = 60;
							}
							horse_X[i] = horse_X[i] + move_X[i] + offset;
							set_End_X(horse_X[i], i);
						}
						
						if(i == 3){
							int offset = 30;
							if(((int)(Math.random()*50))%2 == 1){
								offset -= ((int)(Math.random()*60));
							}
							horse_X[i] = horse_X[i] + move_X[i] + offset;
							set_End_X(horse_X[i], i);
						}
					}
				}
			}
			
			String horse_place_string = "horse_rank";
			for(int i = 0; i < horse_place.length; i++)
				horse_place_string += ";" + horse_place[i];
			this.socket_server.send_all_client(horse_place_string);
		}
		
		public static void set_End_X(int horse_X_Loc, int i){//抵達、超過End_X 歸回End_X位置
			if(horse_X_Loc >= end_X)
				horse_X[i] = end_X;
		}
		
		public boolean judgment(int horse_X, int loc){
			if(horse_X < end_X){
				return false;
			}else{
				if(this.place > 3){
					this.stop();
				}else if( !contains( horse_place, horse_name[loc] ) ){
					horse_place[this.place] = horse_name[loc];
					this.place++;
				}
				return true;
			}
		}
		
		public static void show_place(){
			for(int i=0; i<4; i++){
				System.out.println("第"+i+"名 ："+horse_place[i]);
			}
		}
		
		public static boolean contains(String[] arr, String item) {
			for (int i = 0; i < arr.length; i++) {
				if (item == arr[i]) {
					return true;
				}
			}
			return false;
		}
		
		public void set_socket_server(race_sever socket_server){
			drawPanel.socket_server = socket_server;
		}
		
		public String[] get_place_rank(){
			return drawPanel.horse_place;
		}

	}
