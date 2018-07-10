import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class sheng_frm extends JFrame implements ActionListener {	
	private static boolean Game_switch_status = false;
	
	private static JPanel Animation_pan = new JPanel(new GridLayout(1,1));//動畫版面
	private static JPanel Op_pan = new JPanel(new GridLayout(4,1));//遊戲操做相關版面
	private static JButton Game_switch_btn = new JButton();//遊戲開始、重置按鈕
	private static int switch_status = 0;//遊戲開關,Listener判斷用
	private static ImageIcon switch_img[] = {//設定開關按鈕Image
			new ImageIcon("status0.png"),
			new ImageIcon("status1.png")
	};
	
	private static JButton Game_rule_btn = new JButton();//遊戲規則按鈕
	private static JButton horse_ability_btn = new JButton();//馬的能力按鈕(連線資料庫、按下查看資料)
	private static JButton horse_record_btn = new JButton();//賽馬紀錄按鈕(名次傳回資料庫、按下可看)
	private	static ImageIcon op_img[] = {//設定規則按鈕 規則按鈕 紀錄按鈕 Image
			new ImageIcon("rule.png"),
			new ImageIcon("information.png"),
			new ImageIcon("record.png")
	};
	
	private static JPanel music_pan = new JPanel(new GridLayout(1,3));//音樂版面
	private static JButton music_start_btn = new JButton();//音樂開始、循環、暫停按鈕
	private static JButton music_loop_btn  = new JButton();
	private static JButton music_stop_btn  = new JButton();
	private static File Game_Music1;
	private static AudioInputStream soundIn;
	private static Clip player;
	
	private static drawPanel Animation_drpan;
	private static race_sever socket_server;
	
	//遊戲開關事件
	private static ActionListener GameStatus_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch_status++;
			if (cmd == "Gs" && switch_status%2 == 1) {
				Game_switch_btn.setText("遊戲重置");
				Game_switch_btn.setToolTipText("按下後，遊戲重來");
				Game_switch_btn.setIcon(switch_img[1]);
				Animation_drpan.horse_Thread.start();
				Game_switch_status = true;
				Animation_drpan.set_socket_server(socket_server);

			} else {
				Game_switch_btn.setText("賽馬開始");
				Game_switch_btn.setToolTipText("按下後，賽馬開始");
				Game_switch_btn.setIcon(switch_img[0]);
				Animation_drpan.restart();
				Game_switch_status = false;

				clear();
			}
		}
	};
	//遊戲關規則事件
	private static ActionListener rule_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "rule") {
				JOptionPane.showMessageDialog(null, "", 
						"賽馬博弈-規則", JOptionPane.PLAIN_MESSAGE, new ImageIcon("show.png"));
			} 
		}
	};
	//馬資訊-資料庫-事件
	private static ActionListener information_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "information") {
				JOptionPane.showMessageDialog(null, get_sql_horse_inf(),
						"馬的能力", JOptionPane.PLAIN_MESSAGE);
			}
		}
	};
	//音樂事件
	private static ActionListener music_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
				case "start":
					player.start();
					music_start_btn.setEnabled(false);
					music_stop_btn.setEnabled(true);
					break;
				case "loop":
					player.loop(-1);
					music_start_btn.setEnabled(false);
					music_loop_btn.setEnabled(false);
					music_stop_btn.setEnabled(true);
					break;
				case "stop":
					player.stop();
					music_start_btn.setEnabled(true);
					music_loop_btn.setEnabled(true);
					music_stop_btn.setEnabled(false);
					break;
			}
		}
	};
	
	public sheng_frm(){
		setSize(1400, 900);
		setTitle("陞的賽馬賭場");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		socket_server = new race_sever();
		Thread socket_thread = new Thread(socket_server);
		socket_thread.start();
		
		try {//音樂建立
			Game_Music1 = new File("BigTiger.wav");
			soundIn = AudioSystem.getAudioInputStream(Game_Music1);
			player = AudioSystem.getClip();
			player.open(soundIn);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	//連結馬的能力資料庫
	public static String get_sql_horse_inf(){
		String DB_name = "stu1103108157";
		String DB_user = "stu1103108157";
		String DB_password = "stu1103108157";
		String DB_URL = "jdbc:mysql://203.64.91.58/" + DB_name;
		Connection connection = null;
		java.sql.Statement statement = null;
		ResultSet resultSet = null;
		
		String horse = "", inf = "";
		
		try{
			connection = DriverManager.getConnection(DB_URL, DB_user, DB_password);
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM `horse_inf`");
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberofColums = metaData.getColumnCount();
			
			/*for(int i=1; i<=numberofColums; i++){//資料結構欄位數
				System.out.printf("%s\t", metaData.getColumnName(i));
			}
			System.out.printf("\n");
			*/
			while(resultSet.next()){//欄位資料取出
				/*for(int i=1; i<=numberofColums; i++){
					System.out.printf("%s\t", resultSet.getObject(i));
				}
				System.out.printf("\n");*/
				//拉出那個欄位的資料↓
				horse += "馬兒名稱：" + resultSet.getObject("name") + "\n" +
						 "能力資訊：" + resultSet.getObject("information") + "\n";
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("MySQL connect status ERROR!!");
		}
		return horse;
	}

	//設定Music
 	public static void set_Music(){
		ImageIcon music_start = new ImageIcon("music_start.jpg");
		ImageIcon music_loop = new ImageIcon("music_loop.jpg");
		ImageIcon music_stop = new ImageIcon("music_stop.jpg");
		
		music_start_btn.setBackground(Color.darkGray);
		music_loop_btn.setBackground(Color.darkGray);
		music_stop_btn.setBackground(Color.darkGray);
		music_start_btn.setIcon(music_start);
		music_loop_btn.setIcon(music_loop);
		music_stop_btn.setIcon(music_stop);
		
		music_start_btn.setActionCommand("start");
		music_loop_btn.setActionCommand("loop");
		music_stop_btn.setActionCommand("stop");

		music_start_btn.addActionListener(music_Listener);
		music_loop_btn.addActionListener(music_Listener);
		music_stop_btn.addActionListener(music_Listener);
		
		music_pan.add(music_start_btn);
		music_pan.add(music_loop_btn);
		music_pan.add(music_stop_btn);
	}
	
	//設定JButton
	public static void set_btn(){
		Game_switch_btn = new JButton("賽馬開始", switch_img[0]);
		Game_switch_btn.setForeground(Color.MAGENTA);
		Game_switch_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		Game_switch_btn.setToolTipText("按下3秒後，賽馬開始");
		Game_switch_btn.setActionCommand("Gs");
		Game_switch_btn.addActionListener(GameStatus_Listener);
		Op_pan.add(Game_switch_btn);
		
		Game_rule_btn = new JButton("賭馬規則", op_img[0]);
		Game_rule_btn.setForeground(Color.red);
		Game_rule_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		Game_rule_btn.setToolTipText("賽馬相關規則");
		Game_rule_btn.setActionCommand("rule");
		Game_rule_btn.addActionListener(rule_Listener);
		Op_pan.add(Game_rule_btn);
		
		horse_ability_btn = new JButton("馬的能力資訊", op_img[1]);
		horse_ability_btn.setForeground(Color.white);
		horse_ability_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		horse_ability_btn.setToolTipText("選馬能力資訊");
		horse_ability_btn.setActionCommand("information");
		horse_ability_btn.addActionListener(information_Listener);
		Op_pan.add(horse_ability_btn);
		
		horse_record_btn = new JButton("賽程紀錄", op_img[2]);
		horse_record_btn.setForeground(Color.darkGray);
		horse_record_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		horse_record_btn.setToolTipText("歷程排名");
		horse_record_btn.setActionCommand("record");
		Op_pan.add(horse_record_btn);
		
	}
	
	//設定Panel位置、大小
	public static void set_pan(){
		Animation_pan.setBounds(250, 0, 1000, 600);
		Op_pan.setBounds(1249, 0, 144, 400);
		music_pan.setBounds(1249, 570, 144, 30);
	}
	
	//建立遊戲視窗
	public void build_game(sheng_frm frm) throws IOException {
		Animation_drpan = new drawPanel();
		//Animation_drpan.horse_Thread.start();
		Animation_pan.add(Animation_drpan);
		set_btn();
		set_Music();
		set_pan();
		
		frm.add(Animation_pan);
		frm.add(Op_pan);
		frm.add(music_pan);
		frm.setVisible(true);
		
	}
	
	//初始化
	public static void clear(){
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

public class horse_race {	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		sheng_frm Race_Game = new sheng_frm();
		Race_Game.build_game(Race_Game);
	}

}
