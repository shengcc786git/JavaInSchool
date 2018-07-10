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
	
	private static JPanel Animation_pan = new JPanel(new GridLayout(1,1));//�ʵe����
	private static JPanel Op_pan = new JPanel(new GridLayout(4,1));//�C���ް���������
	private static JButton Game_switch_btn = new JButton();//�C���}�l�B���m���s
	private static int switch_status = 0;//�C���}��,Listener�P�_��
	private static ImageIcon switch_img[] = {//�]�w�}�����sImage
			new ImageIcon("status0.png"),
			new ImageIcon("status1.png")
	};
	
	private static JButton Game_rule_btn = new JButton();//�C���W�h���s
	private static JButton horse_ability_btn = new JButton();//������O���s(�s�u��Ʈw�B���U�d�ݸ��)
	private static JButton horse_record_btn = new JButton();//�ɰ��������s(�W���Ǧ^��Ʈw�B���U�i��)
	private	static ImageIcon op_img[] = {//�]�w�W�h���s �W�h���s �������s Image
			new ImageIcon("rule.png"),
			new ImageIcon("information.png"),
			new ImageIcon("record.png")
	};
	
	private static JPanel music_pan = new JPanel(new GridLayout(1,3));//���֪���
	private static JButton music_start_btn = new JButton();//���ֶ}�l�B�`���B�Ȱ����s
	private static JButton music_loop_btn  = new JButton();
	private static JButton music_stop_btn  = new JButton();
	private static File Game_Music1;
	private static AudioInputStream soundIn;
	private static Clip player;
	
	private static drawPanel Animation_drpan;
	private static race_sever socket_server;
	
	//�C���}���ƥ�
	private static ActionListener GameStatus_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch_status++;
			if (cmd == "Gs" && switch_status%2 == 1) {
				Game_switch_btn.setText("�C�����m");
				Game_switch_btn.setToolTipText("���U��A�C������");
				Game_switch_btn.setIcon(switch_img[1]);
				Animation_drpan.horse_Thread.start();
				Game_switch_status = true;
				Animation_drpan.set_socket_server(socket_server);

			} else {
				Game_switch_btn.setText("�ɰ��}�l");
				Game_switch_btn.setToolTipText("���U��A�ɰ��}�l");
				Game_switch_btn.setIcon(switch_img[0]);
				Animation_drpan.restart();
				Game_switch_status = false;

				clear();
			}
		}
	};
	//�C�����W�h�ƥ�
	private static ActionListener rule_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "rule") {
				JOptionPane.showMessageDialog(null, "", 
						"�ɰ��ի�-�W�h", JOptionPane.PLAIN_MESSAGE, new ImageIcon("show.png"));
			} 
		}
	};
	//����T-��Ʈw-�ƥ�
	private static ActionListener information_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "information") {
				JOptionPane.showMessageDialog(null, get_sql_horse_inf(),
						"������O", JOptionPane.PLAIN_MESSAGE);
			}
		}
	};
	//���֨ƥ�
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
		setTitle("�����ɰ����");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		socket_server = new race_sever();
		Thread socket_thread = new Thread(socket_server);
		socket_thread.start();
		
		try {//���֫إ�
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
	
	//�s��������O��Ʈw
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
			
			/*for(int i=1; i<=numberofColums; i++){//��Ƶ��c����
				System.out.printf("%s\t", metaData.getColumnName(i));
			}
			System.out.printf("\n");
			*/
			while(resultSet.next()){//����ƨ��X
				/*for(int i=1; i<=numberofColums; i++){
					System.out.printf("%s\t", resultSet.getObject(i));
				}
				System.out.printf("\n");*/
				//�ԥX������쪺��ơ�
				horse += "����W�١G" + resultSet.getObject("name") + "\n" +
						 "��O��T�G" + resultSet.getObject("information") + "\n";
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("MySQL connect status ERROR!!");
		}
		return horse;
	}

	//�]�wMusic
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
	
	//�]�wJButton
	public static void set_btn(){
		Game_switch_btn = new JButton("�ɰ��}�l", switch_img[0]);
		Game_switch_btn.setForeground(Color.MAGENTA);
		Game_switch_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		Game_switch_btn.setToolTipText("���U3���A�ɰ��}�l");
		Game_switch_btn.setActionCommand("Gs");
		Game_switch_btn.addActionListener(GameStatus_Listener);
		Op_pan.add(Game_switch_btn);
		
		Game_rule_btn = new JButton("�䰨�W�h", op_img[0]);
		Game_rule_btn.setForeground(Color.red);
		Game_rule_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		Game_rule_btn.setToolTipText("�ɰ������W�h");
		Game_rule_btn.setActionCommand("rule");
		Game_rule_btn.addActionListener(rule_Listener);
		Op_pan.add(Game_rule_btn);
		
		horse_ability_btn = new JButton("������O��T", op_img[1]);
		horse_ability_btn.setForeground(Color.white);
		horse_ability_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		horse_ability_btn.setToolTipText("�ﰨ��O��T");
		horse_ability_btn.setActionCommand("information");
		horse_ability_btn.addActionListener(information_Listener);
		Op_pan.add(horse_ability_btn);
		
		horse_record_btn = new JButton("�ɵ{����", op_img[2]);
		horse_record_btn.setForeground(Color.darkGray);
		horse_record_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		horse_record_btn.setToolTipText("���{�ƦW");
		horse_record_btn.setActionCommand("record");
		Op_pan.add(horse_record_btn);
		
	}
	
	//�]�wPanel��m�B�j�p
	public static void set_pan(){
		Animation_pan.setBounds(250, 0, 1000, 600);
		Op_pan.setBounds(1249, 0, 144, 400);
		music_pan.setBounds(1249, 570, 144, 30);
	}
	
	//�إ߹C������
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
	
	//��l��
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
