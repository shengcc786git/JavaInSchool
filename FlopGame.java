import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.sound.sampled.*;
import javax.sound.sampled.Line.Info;


class sheng_frm extends JFrame implements ActionListener{
	public class ImagePanel extends JPanel{
		public ImagePanel(){
			super();
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(title_img[index] != null){
				g.drawImage(title_img[index].getImage(), 0, 0, this);
				index = (index < maxImg ? index + 1 : 0);
			}
		}
	}
	public class ImagePanel2 extends JPanel{
		public ImagePanel2(){
			super();
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(count_img[index2] != null){
				g.drawImage(count_img[index2].getImage(), 0, 0, this);
				index2 = (index2 < maxImg2 ? index2 + 1 : 0);
			}
		}
	}
	
	private static final long serialVersionUID = 1L;
	private static final InputStream Game_Music = null;
	private static JPanel Menu_pan = new JPanel(new GridLayout(1,1));
	private static JPanel u1_name_pan = new JPanel(new GridLayout(1,1));
	private static JPanel user1_pan = new JPanel(new GridLayout(1,1));
	private static JPanel u1_choice_pan = new JPanel(new GridLayout(1,1));
	private static JPanel u2_name_pan = new JPanel(new GridLayout(1,1));
	private static JPanel user2_pan = new JPanel(new GridLayout(1,1));
	private static JPanel u2_choice_pan = new JPanel(new GridLayout(1,1));
	private static JPanel music_pan = new JPanel(new GridLayout(1,3));
	private static JPanel Op_pan = new JPanel(new GridLayout(4,4));
	private static JPanel gm_btn_pan = new JPanel(new GridLayout(1,1));
	private static JPanel count_pan = new JPanel(new GridLayout(1,1));
	private static JPanel point_pan = new JPanel(new GridLayout(1,1));
	private static JPanel point_pan2 = new JPanel(new GridLayout(1,1));
	
	private static String user1_names[] = {"user1-1.jpg", "user1-2.jpg", "user1-3.jpg"};
	private static String user2_names[] = {"user2-1.jpg", "user2-2.jpg", "user2-3.jpg"};
	private static JComboBox u1_cob;
	private static JComboBox u2_cob;
	private static ImageIcon[] u1_img = {
			new ImageIcon(user1_names[0]),
			new ImageIcon(user1_names[1]), 
			new ImageIcon(user1_names[2])};
	private static ImageIcon[] u2_img = {
			new ImageIcon(user2_names[0]),
			new ImageIcon(user2_names[1]), 
			new ImageIcon(user2_names[2])};
	private static JLabel user1_lab = new JLabel(u1_img[0]);
	private static JLabel user2_lab = new JLabel(u2_img[0]);
	
	private static JButton mu_start_btn = new JButton();
	private static JButton mu_loop_btn  = new JButton();
	private static JButton mu_stop_btn  = new JButton();
	
	private static JButton brand_btn[] = new JButton[16];
	private static JButton Game_btn = new JButton();
	
	private static ImageIcon back = new ImageIcon("back.jpg");
	private static ImageIcon status0 = new ImageIcon("game_btn0.jpg");
	private static ImageIcon status1 = new ImageIcon("game_btn1.jpg");
	private static ImageIcon brand[] = new ImageIcon[8];
	
	private static int status = 1;
	private static boolean Game_start_Status = false;
	private static int brand_limit[] = new int[8];
	private static boolean brand_loc[] = new boolean[16];
	private static int count = 0;
	
	private static int u1_correct = 0;
	private static int u2_correct = 0;
	private static int press_count = 0;
	private static int u_count_status = 0;
	private static int temp = 0;
	private	static boolean u1_status = true , u2_status = false;
	private static boolean brand_status[] = new boolean[16];
	private static int brand_loc_tmp[] = new int[16];
	private static int brand_mark[] = new int[16];
	private static ImageIcon save_brand[] = new ImageIcon[16];
	
	private static File Game_Music1;
	private static AudioInputStream soundIn;
	private static Clip player;
	
	private static ImageIcon count_img[] = {
			new ImageIcon("count.jpg"),
			new ImageIcon("count3.jpg"),
			new ImageIcon("count2.jpg"),
			new ImageIcon("count1.jpg"),
			new ImageIcon("count0.jpg"),
			new ImageIcon("count.jpg")};
	private static JLabel count_lab = new JLabel(count_img[0]);
	
	private Timer timer = new Timer(1000, new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			img_pan.repaint();
		}
	});
	private static JLabel title_lab = new JLabel();
	private static ImageIcon title_img[] = {
			new ImageIcon("title0.jpg"),
			new ImageIcon("title1.jpg"),
			new ImageIcon("title2.jpg"),
			new ImageIcon("title3.jpg")
		};
	static ImagePanel img_pan;
	static ImagePanel2 img_count_pan;
	static int index = 0;
	static int index2 = 0;
	static int maxImg;
	static int maxImg2;
	int n = 0, m=0;
	
	static int t_count = 0;
	
	private static JLabel point_lab = new JLabel("User-1 Vs User-2",0);
	private static JLabel point_lab2 = new JLabel(u1_correct+" : "+u2_correct,0);
	
	private  static Timer timer2 = new Timer(800, new ActionListener(){
		public void actionPerformed(ActionEvent e) {
			t_count++;
			img_count_pan.repaint();
			if(t_count>5){
				for(int i=0; i<16; i++){
					brand_btn[i].setIcon(back);
					timer2.stop();
				}
			}
		}
	});

	public sheng_frm(){
		setSize(800, 635);
		setTitle("陞的小遊戲");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		try {
			Game_Music1 = new File("FlyOut.wav");
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
		
		img_pan = new ImagePanel();
		getContentPane().add(img_pan);
		img_pan.add(title_lab);
		timer.start();
		maxImg = title_img.length - 1;
		
		img_count_pan = new ImagePanel2();
		getContentPane().add(img_count_pan);
		img_count_pan.add(count_lab);
		
	}
	
	private static ActionListener GameStatus_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (cmd == "Gs" && status % 2 == 1) {
				random_brand();
				set_count();
				init_b_status();
				op_game();
				Game_start_Status = true;
				Game_btn.setText("遊戲重置");
				Game_btn.setToolTipText("按下後，遊戲重來");
				Game_btn.setIcon(status1);
				status++;
			} else {
				clear_op();
				//status++;
			}
		}
	};

	private static  ActionListener music_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			switch(cmd){
				case "start":
					player.start();
					mu_start_btn.setEnabled(false);
					mu_stop_btn.setEnabled(true);
					break;
				case "loop":
					player.loop(-1);
					mu_start_btn.setEnabled(false);
					mu_loop_btn.setEnabled(false);
					mu_stop_btn.setEnabled(true);
					break;
				case "stop":
					player.stop();
					mu_start_btn.setEnabled(true);
					mu_loop_btn.setEnabled(true);
					mu_stop_btn.setEnabled(false);
					break;
			}
		}
	};
	
	
	private static ActionListener Op_Game_Listener = new ActionListener() {
		public void actionPerformed(ActionEvent e){
			String cmd = e.getActionCommand();
			switch(cmd){
				case "0" :
				case "1" :
				case "2" :
				case "3" :
				case "4" :
				case "5" :
				case "6" :
				case "7" :
				case "8" :
				case "9" :
				case "10" :
				case "11" :
				case "12" :
				case "13" :
				case "14" :
				case "15" :
					brand_judgment(cmd);
					break;
			}
			point_lab2.setText(u1_correct + " : " + u2_correct);
			showOptionDialog();
		}
	};
	
	public void build_game () throws Exception{
		sheng_frm frm = new sheng_frm();
		set_user();
		set_Music();
		set_pan();
		set_btn();
		set_brd_img();
		set_count_lab();
		set_point_pan();
		
		frm.add(img_pan);
		frm.add(u1_name_pan);
		frm.add(user1_pan);
		frm.add(u1_choice_pan);
		frm.add(u2_name_pan);
		frm.add(user2_pan);
		frm.add(u2_choice_pan);
		frm.add(music_pan);
		frm.add(Op_pan);
		frm.add(gm_btn_pan);
		frm.add(img_count_pan);
		frm.add(point_pan);
		frm.add(point_pan2);
		frm.setVisible(true);
	}
	
	public static void set_pan(){
		img_pan.setBounds(15, 5, 120, 40);
		u1_name_pan.setBounds(15, 50, 120, 25);
		user1_pan.setBounds(15, 75, 120, 150);
		u1_choice_pan.setBounds(15, 225, 120, 25);
		u2_name_pan.setBounds(15, 250, 120, 25);
		user2_pan.setBounds(15, 275, 120, 150);
		u2_choice_pan.setBounds(15, 425, 120, 25);
		music_pan.setBounds(0, 570, 150, 30);
		Op_pan.setBounds(150, 0, 500, 600);
		gm_btn_pan.setBounds(663, 10, 100, 50);
		img_count_pan.setBounds(663, 70, 100, 100);
		point_pan.setBounds(663, 180, 100, 20);
		point_pan2.setBounds(663, 200, 100, 20);
	}
	
	public static void set_user(){
		JLabel u1_name_lab = new JLabel("User-1",0);
		u1_name_lab.setForeground(Color.decode("#0066FF"));
		JLabel u2_name_lab = new JLabel("User-2",0);
		u2_name_lab.setForeground(Color.decode("#FF00FF"));
		
		u1_cob = new JComboBox(user1_names);
		u1_cob.setMaximumRowCount(3);
		u1_cob.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event){
				if(event.getStateChange() == ItemEvent.SELECTED){
					user1_lab.setIcon(u1_img[u1_cob.getSelectedIndex()]);
				}
			}
		});
		u2_cob = new JComboBox(user2_names);
		u2_cob.setMaximumRowCount(3);
		u2_cob.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent event){
				if(event.getStateChange() == ItemEvent.SELECTED){
					user2_lab.setIcon(u2_img[u2_cob.getSelectedIndex()]);
				}
			}
		});
		u1_name_pan.add(u1_name_lab);
		user1_pan.add(user1_lab);
		u1_choice_pan.add(u1_cob);
		u2_name_pan.add(u2_name_lab);
		user2_pan.add(user2_lab);
		u2_choice_pan.add(u2_cob);
	}
	
	public static void set_Music(){
		ImageIcon music_start = new ImageIcon("music_start.jpg");
		ImageIcon music_loop = new ImageIcon("music_loop.jpg");
		ImageIcon music_stop = new ImageIcon("music_stop.jpg");
		
		mu_start_btn.setBackground(Color.darkGray);
		mu_loop_btn.setBackground(Color.darkGray);
		mu_stop_btn.setBackground(Color.darkGray);
		mu_start_btn.setIcon(music_start);
		mu_loop_btn.setIcon(music_loop);
		mu_stop_btn.setIcon(music_stop);
		
		mu_start_btn.setActionCommand("start");
		mu_loop_btn.setActionCommand("loop");
		mu_stop_btn.setActionCommand("stop");

		mu_start_btn.addActionListener(music_Listener);
		mu_loop_btn.addActionListener(music_Listener);
		mu_stop_btn.addActionListener(music_Listener);
		
		music_pan.add(mu_start_btn);
		music_pan.add(mu_loop_btn);
		music_pan.add(mu_stop_btn);
	}
		
	public static void set_btn(){
		for(int i=0; i<16; i++){
			brand_btn[i] = new JButton();
			brand_btn[i].setIcon(back);
			Op_pan.add(brand_btn[i]);
		}
		Game_btn = new JButton("遊戲開始",status0);
		Game_btn.setHorizontalTextPosition(AbstractButton.CENTER);
		Game_btn.setToolTipText("按下後，遊戲開始");
		Game_btn.setActionCommand("Gs");
		Game_btn.addActionListener(GameStatus_Listener);
		gm_btn_pan.add(Game_btn);
	}
	
	public static void set_brd_img(){
		for(int i = 0; i<8; i++){
			brand[i] = new ImageIcon("brand" + i + ".jpg");
		}
	}
		
	public static void random_brand(){
		while(check_brand() != 0){
			int rand = (int)(Math.random()*8);
			if(brand_limit[rand] < 2 && brand_loc[count] == false){
				save_brand[count] = new ImageIcon("brand"+rand+".jpg");
				brand_loc_tmp[count] = rand;
				brand_btn[count].setIcon(brand[rand]);
				brand_loc[count] = true;
				brand_limit[rand]++;
				count++;
				check_brand();
			}
			else{
				check_brand();
			}
		}
	}
	
	public static int check_brand(){
		if(count == 16)
			return 0;
		else
			return 1;
	}
	
	public static void clear_brand(){
		for(int i=0; i<8; i++)
			brand_limit[i] = 0;
		for(int i=0; i<16; i++)
			brand_loc[i] = false;
		count = 0;
	}
	
	public static void set_count_lab(){
		//count_lab.setIcon(count_img);
		count_pan.add(count_lab);
	}
	
	public static void set_count(){
		timer2.start();
		maxImg2 = count_img.length - 1;
	}
	
	public static void op_game(){
		for(int i=0; i<16; i++){
			brand_btn[i].setActionCommand(""+i);
			brand_btn[i].addActionListener(Op_Game_Listener);
		}
	}
	
	
	public static void brand_judgment(String loc){
		press_count++;
		
		int btn_loc = Integer.parseInt(loc);
		//System.out.println(btn_loc);
		
		if(press_count == 1){
			System.out.println("1");
			temp = btn_loc;
			brand_mark[btn_loc] = brand_loc_tmp[btn_loc];
			brand_btn[btn_loc].setIcon(save_brand[btn_loc]);
		}
		else if(press_count == 2){
			System.out.println("1");
			brand_mark[btn_loc] = brand_loc_tmp[btn_loc];
			System.out.println(temp);
			System.out.println(btn_loc);
			brand_btn[btn_loc].setIcon(save_brand[btn_loc]);
			if(brand_mark[temp] == brand_mark[btn_loc]){
				brand_status[temp] = true;	
				brand_status[btn_loc] = true;
				System.out.println("111");
				if(u1_status == true){
					u1_correct++;
				}
				else{
					u2_correct++;
				}
				press_count = 0;
			}
			else{
				if(u_count_status % 2 == 0){
				System.out.println("001");
				u1_status = false;
				u2_status = true;	
				}
				else{
					System.out.println("002");
					u1_status = true;
					u2_status = false;
				}
			brand_btn[temp].setIcon(back);
			brand_btn[btn_loc].setIcon(back);
			press_count = 0;
			}
			u_count_status++;
		}
	}
	
	public static void init_b_status(){
		for(int i=0; i<16; i++)
			brand_loc[i] = false;
	}
	
	public static void clear_op(){
		clear_brand();
		init_b_status();
		press_count = 0;
		temp = 0;
		u_count_status = 0;
		u1_correct = 0;
		u2_correct = 0;
		u1_status = true;
		u2_status = false;
		t_count = 0;
		index2 = 0;
		point_lab2.setText(u1_correct + " : " + u2_correct);
		for(int i=0; i<16; i++){
			brand_status[i] = false;
			brand_btn[i].setIcon(back);
			brand_mark[i] = 0;
			brand_loc_tmp[i] = 0;
		}
		Game_start_Status = false;
		Game_btn.setText("遊戲開始");
		Game_btn.setToolTipText("按下後，遊戲開始");
		Game_btn.setIcon(status0);
		status = 1;
	}
	
	public static void set_point_pan(){
		point_lab.setForeground(Color.decode("#8A2BE2"));
		point_pan.add(point_lab);
		point_lab2.setForeground(Color.decode("#9400D3"));
		point_pan2.add(point_lab2);
	}
	
	public static void showOptionDialog() {
		if (Game_start_Status == true) {
			if((u1_correct + u2_correct) == 8){
				if(u1_correct > u2_correct){
					JOptionPane.showMessageDialog(null,
							"User-1 Win!\n" + "User1 Vs User2 比數:\n" + u1_correct + ":" + u2_correct, "遊戲結束",
							JOptionPane.PLAIN_MESSAGE);
					clear_op();
				}
				else if(u1_correct < u2_correct){
					JOptionPane.showMessageDialog(null,
							"User-2 Win!\n" + "User1 Vs User2 比數:\n" + u1_correct + ":" + u2_correct, "遊戲結束",
							JOptionPane.PLAIN_MESSAGE);
					clear_op();
				}
				else{
					JOptionPane.showMessageDialog(null,
							"Draw!\n" + "User1 Vs User2 比數:\n" + u1_correct + ":" + u2_correct, "遊戲結束",
							JOptionPane.PLAIN_MESSAGE);
					clear_op();
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
}

public class app1{

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		sheng_frm game = new sheng_frm();
		game.build_game();
	}

}
