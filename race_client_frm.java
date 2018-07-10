import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class race_client_frm extends JFrame{
	private static JPanel title_pan = new JPanel(new GridLayout(1,1));//��`���D����
	private static JLabel title_lab = new JLabel("�Ȥ��`����", 0);//��`���D
	private static JPanel Op_pan = new JPanel(new GridLayout(1,3));//�C���ް���������
	private static JButton Game_rule_btn = new JButton();//�C���W�h���s
	private static JButton horse_ability_btn = new JButton();//������O���s(�s�u��Ʈw�B���U�d�ݸ��)
	private static JButton horse_record_btn = new JButton();//�ɰ��������s(�W���Ǧ^��Ʈw�B���U�i��)
	private	static ImageIcon op_img[] = {//�]�w�W�h���s �W�h���s �������s Image
			new ImageIcon("rule.png"),
			new ImageIcon("information.png"),
			new ImageIcon("record.png")
	};
	private static JPanel bet_2_pan = new JPanel(new GridLayout(1,2));//��`���Ϫ���
	private static JLabel bet_alone_lab = new JLabel("�WĹ�U�`��", 0);//��`���ϼ��D
	private static JLabel bet_continuous_lab = new JLabel("�s���U�`��", 0);//��`���ϼ��D
	private static JPanel alone_pan = new JPanel(new GridLayout(4,1));//�WĹ����
	static JCheckBox alone_JCheckBox[] = new JCheckBox[4];
	static ButtonGroup btn_group = new ButtonGroup();
	private static JPanel continuous_No_pan = new JPanel(new GridLayout(4,1));//�s���W�Ǫ���
	private static JPanel continuous_ComboBox_pan = new JPanel(new GridLayout(4,1));//�s����檩��
	private static JLabel no1_lab = new JLabel("No.1 :", 0);
	private static JLabel no2_lab = new JLabel("No.2 :", 0);
	private static JLabel no3_lab = new JLabel("No.3 :", 0);
	private static JLabel no4_lab = new JLabel("No.4 :", 0);
	private static JComboBox continuous_JComboBox_no1 = new JComboBox();
	private static JComboBox continuous_JComboBox_no2 = new JComboBox();
	private static JComboBox continuous_JComboBox_no3 = new JComboBox();
	private static JComboBox continuous_JComboBox_no4 = new JComboBox();
	private static String horse_name[] = {"�°�", "���߰�", "�·t�W���~", "�m�i�p��"};
	private static String predict_No1_name = "�°�";
	private static String place_names[] = {"�°�", "�°�", "�°�", "�°�"};
	private static JPanel bet_pan = new JPanel();//�U�`����(�s�usever�����ɰ����G)
	private static JButton bet_btn = new JButton();//�U�`��(�s�usever�����ɰ����G)
	private static JPanel choice_bet_pan = new JPanel();//��ܤU�`���B���D����
	private static JLabel choice_bet_lab = new JLabel("��	�п�ܤU�`���B 	��");//��ܤU�`���B
	private static JPanel choice_money_pan = new JPanel(new GridLayout(2,3));//��ܪ��B����
	private static JCheckBox money_JCheckBox[] = new JCheckBox[6];
	private static ButtonGroup btn_group2 = new ButtonGroup();
	private static int Real_bet_money = 1000; 
	private static int player_money = 10000; 
	private static int bet_money[] = {1000, 6000, 10000, 15000, 30000, 100000};
	private static JPanel res_money_pan = new JPanel(new GridLayout(3,1));
	private static JLabel res_horse_lab = new JLabel("", 0);
	private static JLabel res_lab = new JLabel("�� �z���W�Ѿl���B  ��", 0);
	private static JLabel res_money_lab = new JLabel("10000", 0);
	
	private static race_client client_con;
	
	static ActionListener bet_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(player_money >= Real_bet_money){
				String cmd = e.getActionCommand();
				client_con.Send_cmd_to_Server("bet_horse;" + predict_No1_name + ";" + Real_bet_money + ";");
				player_money = player_money - Real_bet_money;
				res_money_lab.setText(String.valueOf(player_money));
				res_horse_lab.setText("�U�`���B�G" + String.valueOf(Real_bet_money));
			}
		}
	};
	
	static ActionListener money_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			int loc = Integer.parseInt(cmd);
			if(player_money >= bet_money[loc])
				Real_bet_money = bet_money[loc];
		}
	};
	
	static ActionListener alone_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			predict_No1_name = cmd;
		}
	};
	
	static ActionListener continuous_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			int loc = Integer.parseInt(cmd);
			if(cmd == "0"){
				place_names[0] = (String) continuous_JComboBox_no1.getSelectedItem();
				System.out.println(place_names[0]);
			}
			if(cmd == "1"){
				place_names[1] = (String) continuous_JComboBox_no2.getSelectedItem();
				System.out.println(place_names[1]);
			}
			if(cmd == "2"){
				place_names[2] = (String) continuous_JComboBox_no3.getSelectedItem();
				System.out.println(place_names[2]);
			}
			if(cmd == "3"){
				place_names[3] = (String) continuous_JComboBox_no4.getSelectedItem();
				System.out.println(place_names[3]);
			}
		}
	};
	
	
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
	
	public race_client_frm(){
		setSize(500, 800);
		setTitle("Client-��`����");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
	}
	
	public static String get_sql_horse_inf(){
		String DB_name = "stu1103108157";
		String DB_user = "stu1103108157";
		String DB_password = "stu1103108157";
		String DB_URL = "jdbc:mysql://203.64.91.58/" + DB_name;
		Connection connection = null;
		java.sql.Statement statement = null;
		ResultSet resultSet = null;
		
		String horse = "";
		
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
	
	//�]�w�U�`��
	public static void set_bet(){
		alone_JCheckBox[0] = new JCheckBox("1.�°�");
		alone_JCheckBox[0].setSelected(true);// �w�]��
		alone_JCheckBox[1] = new JCheckBox("2.���߰�");
		alone_JCheckBox[2] = new JCheckBox("3.�·t�W���~");
		alone_JCheckBox[3] = new JCheckBox("4.�m�i�p��");
		
		for(int i=0; i<4; i++){
			alone_JCheckBox[i].setActionCommand(horse_name[i]);
			alone_JCheckBox[i].addActionListener(alone_Listener);
		}
			
		for(int i=0; i<4; i++){
			btn_group.add(alone_JCheckBox[i]);
			alone_pan.add(alone_JCheckBox[i]);
		}
		
		money_JCheckBox[0] = new JCheckBox("1,000");
		money_JCheckBox[0].setSelected(true);// �w�]��
		money_JCheckBox[1] = new JCheckBox("6,000");
		money_JCheckBox[2] = new JCheckBox("10,000");
		money_JCheckBox[3] = new JCheckBox("15,000");
		money_JCheckBox[4] = new JCheckBox("30,000");
		money_JCheckBox[5] = new JCheckBox("100,000");
		
		for(int i=0; i<6; i++){
			money_JCheckBox[i].setActionCommand(""+i+"");
			money_JCheckBox[i].addActionListener(money_Listener);
		}
		
		for(int i=0; i<6; i++){
			btn_group2.add(money_JCheckBox[i]);
			choice_money_pan.add(money_JCheckBox[i]);
		}

	}
	
	public static void set_Combobox(){
		continuous_JComboBox_no1 = new JComboBox(horse_name);
		continuous_JComboBox_no2 = new JComboBox(horse_name);
		continuous_JComboBox_no3 = new JComboBox(horse_name);
		continuous_JComboBox_no4 = new JComboBox(horse_name);
		
		continuous_JComboBox_no1.setActionCommand("0");
		continuous_JComboBox_no2.setActionCommand("1");
		continuous_JComboBox_no3.setActionCommand("2");
		continuous_JComboBox_no4.setActionCommand("3");
		continuous_JComboBox_no1.addActionListener(continuous_Listener);
		continuous_JComboBox_no2.addActionListener(continuous_Listener);
		continuous_JComboBox_no3.addActionListener(continuous_Listener);
		continuous_JComboBox_no4.addActionListener(continuous_Listener);
		
		continuous_ComboBox_pan.add(continuous_JComboBox_no1);
		continuous_ComboBox_pan.add(continuous_JComboBox_no2);
		continuous_ComboBox_pan.add(continuous_JComboBox_no3);
		continuous_ComboBox_pan.add(continuous_JComboBox_no4);
	}
	
	public static void set_lab(){
		title_lab.setForeground(Color.decode("#8A2BE2"));
		title_pan.add(title_lab);
		
		bet_alone_lab.setForeground(Color.decode("#0066FF"));
		bet_continuous_lab.setForeground(Color.decode("#E63F00"));
		bet_2_pan.add(bet_alone_lab);
		bet_2_pan.add(bet_continuous_lab);
		
		no1_lab.setForeground(Color.decode("#666666"));
		no2_lab.setForeground(Color.decode("#666666"));
		no3_lab.setForeground(Color.decode("#666666"));
		no4_lab.setForeground(Color.decode("#666666"));
		continuous_No_pan.add(no1_lab);
		continuous_No_pan.add(no2_lab);
		continuous_No_pan.add(no3_lab);
		continuous_No_pan.add(no4_lab);
		
		choice_bet_lab.setForeground(Color.decode("#FF7744"));
		choice_bet_pan.add(choice_bet_lab);
		
		res_money_pan.add(res_horse_lab);
		res_money_pan.add(res_lab);
		res_money_pan.add(res_money_lab);
	}
	
	public static void set_btn(){
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
		Op_pan.add(horse_record_btn);;
		
		bet_btn = new JButton("�T�{�U�`");
		bet_btn.setActionCommand("bet");
		bet_btn.addActionListener(bet_Listener);
		bet_pan.add(bet_btn);
	}
	
	public static void set_pan(){
		title_pan.setBounds(0, 0, 500, 50);
		Op_pan.setBounds(25, 50, 450, 100);
		bet_2_pan.setBounds(0, 150, 500, 50);
		alone_pan.setBounds(80, 200, 100, 100);
		continuous_No_pan.setBounds(290, 200, 60, 100);
		continuous_ComboBox_pan.setBounds(360, 200, 100, 100);
		choice_bet_pan.setBounds(0, 310, 500, 50);
		choice_money_pan.setBounds(125, 350, 250, 100);
		bet_pan.setBounds(150, 500, 200, 50);
		res_money_pan.setBounds(0, 600, 500, 150);
	}
	
	public static void bet_win(String win_horse_name){
		System.out.println("bet_win");
		player_money += Real_bet_money*2;
		res_horse_lab.setText("�Ĥ@�W�G" + win_horse_name);
		res_lab.setText("�zĹ�o�F " + String.valueOf(Real_bet_money*2) + " ��");
		res_money_lab.setText(String.valueOf(player_money));
		
	};
	
	public static void bet_lose(String win_horse_name){
		System.out.println("bet_lose");
		res_horse_lab.setText("�Ĥ@�W�G" + win_horse_name);
		res_lab.setText("�z��F�A�z���W�Ѿl���B");
	};
	
	public String get_predict_No1_name(){
		return this.predict_No1_name;
	};
	
	
	
	public void build_game(race_client_frm frm) throws IOException {
		set_lab();
		set_btn();
		set_bet();
		set_Combobox();
		set_pan();

		frm.add(title_pan);
		frm.add(bet_2_pan);
		frm.add(Op_pan);
		frm.add(continuous_No_pan);
		frm.add(continuous_ComboBox_pan);
		frm.add(alone_pan);
		frm.add(choice_bet_pan);
		frm.add(choice_money_pan);
		frm.add(bet_pan);
		frm.add(res_money_pan);
		
		frm.setVisible(true);
	}
	
	public static void main(String[] args) throws IOException {
		race_client_frm client_Game = new race_client_frm();
		client_Game.build_game(client_Game);
		
		client_con = new race_client();
		Thread client_socket_thread = new Thread(client_con);
		client_socket_thread.start();

		client_con.set_race_client_frm(client_Game);
	}
}
