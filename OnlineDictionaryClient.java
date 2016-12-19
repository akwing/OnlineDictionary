package OnlineDic;

import java.net.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

public class OnlineDictionaryClient extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int height = 5, width = 30;
	
	private boolean hasLogin;
	private boolean[] b = new boolean[3];
	private int numYoudao, numJinshan, numBing;
	
	private JTextField jtfWord = new JTextField(40);
	  
    private JTextArea jtaYoudao = new JTextArea(height, width), jtaJinshan = new JTextArea(height, width), jtaBing = new JTextArea(height, width);
    private JButton jbt = new JButton("查询");
    private JButton[] jbtzan = new JButton[3], jbtshare = new JButton[3];
    
    private JCheckBox jcbYoudao = new JCheckBox("有道",true), 
    		jcbJinshan =  new JCheckBox("金山",true), 
    		jcbBing = new JCheckBox("必应",true);
    
    private JMenuBar jmb = new JMenuBar();
    private JMenu logMenu = new JMenu("账户"), onlineMenu = new JMenu("线上"), helpMenu = new JMenu("帮助");
    private JMenuItem jmi1 = new JMenuItem("用户注册"), jmi2 = new JMenuItem("用户登陆"), jmi3 = new JMenuItem("用户注销");
    private JLabel jlab1, jlab2, jlab3;
    
    private JPanel p1, p2, p3, jpYoudao, jpJinshan, jpBing, p7;
    
    private Socket socket;
    ObjectOutputStream toServer;
	ObjectInputStream fromServer;
	private String username;
    
	private LoginTask logintask;
	private Thread thread;
	private OnlineDictionaryClient odc = this;
	
	private JFrame UserListFrame = new JFrame("在线用户列表");
	private JTextArea jtaUL = new JTextArea();
	
    public OnlineDictionaryClient() throws UnknownHostException, IOException
    {
    	super("在线词典ver1.0-离线");
    	socket= new Socket("172.26.121.75",8007);
    	
    	toServer =  new ObjectOutputStream(socket.getOutputStream());
		fromServer = new ObjectInputStream(socket.getInputStream());
		
    	hasLogin = false;
    	setResizable(false);
    	this.setSize(700, 600);
    	
    	Toolkit tk = this.getToolkit();
    	Dimension dm = tk.getScreenSize(); 
    	this.setLocation((int)(dm.getWidth()-700)/2 - 150,(int)(dm.getHeight()-700)/2);

    	UserListFrame.setSize(300, 600);
    	UserListFrame.setLocation((int)(dm.getWidth()-700)/2 +540,(int)(dm.getHeight()-700)/2);
    	UserListFrame.setVisible(true);
    	jtaUL.setEditable(false);
    	UserListFrame.add(jtaUL);
    	
    	//菜单栏
    	logMenu.add(jmi1);
    	logMenu.add(jmi2);
    	logMenu.add(jmi3);
    	jmb.add(logMenu);
    	jmb.add(onlineMenu);
    	jmb.add(helpMenu);
    	setJMenuBar(jmb);
    	
    	//输入区
    	p1 = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
		p1.add(jtfWord);
		p1.add(jbt);
		p1.setBorder(new TitledBorder("输入区"));
		this.add(p1,BorderLayout.NORTH);
    	
		//翻译区
		p2 = new JPanel(new BorderLayout());
		p3 = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
		p3.add(jcbYoudao);
		p3.add(jcbJinshan);
		p3.add(jcbBing);
		//p3.setBorder(new TitledBorder("候选区"));
		p2.add(p3, BorderLayout.NORTH);
		
		jlab1 = new JLabel(new ImageIcon("C:\\Users\\user\\workspace\\Youdao.jpg"));
		jlab2 = new JLabel(new ImageIcon("C:\\Users\\user\\workspace\\Jinshan.jpg"));
		jlab3 = new JLabel(new ImageIcon("C:\\Users\\user\\workspace\\Bing.jpg"));
		
		jpYoudao = new JPanel(new FlowLayout(FlowLayout.LEFT,12,4));
		jpJinshan = new JPanel(new FlowLayout(FlowLayout.LEFT,12,4));
		jpBing = new JPanel(new FlowLayout(FlowLayout.LEFT,12,4));
		
		for(int i = 0; i < 3; i++)
		{
			jbtzan[i] = new JButton("\n 赞 \n");
			b[i] = false;
			jbtshare[i] = new JButton("分享");
		} 
		
		jpYoudao.add(jlab1);jpYoudao.add(jtaYoudao);jpYoudao.add(jbtzan[0]);jpYoudao.add(jbtshare[0]);
		
		jpJinshan.add(jlab2);jpJinshan.add(jtaJinshan);jpJinshan.add(jbtzan[1]);jpJinshan.add(jbtshare[1]);
		
		jpBing.add(jlab3);jpBing.add(jtaBing);jpBing.add(jbtzan[2]);jpBing.add(jbtshare[2]);
		
		jpYoudao.setBorder(new TitledBorder("有道"));
		jpJinshan.setBorder(new TitledBorder("金山"));
		jpBing.setBorder(new TitledBorder("必应"));
		jtaYoudao.setEditable(false);
		jtaJinshan.setEditable(false);
		jtaBing.setEditable(false);
		p7 = new JPanel(new GridLayout(0,1));
		p7.add(jpYoudao);
		p7.add(jpJinshan);
		p7.add(jpBing);
		p2.add(p7);
		p2.setBorder(new TitledBorder("翻译区"));
		this.add(p2,BorderLayout.CENTER);
		
		
    	this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	//在此处需要增加用户注销的信息
                System.exit(-1);
            }
        });
    	
    	jbt.addActionListener(new ActionListener(){
			//监听搜索按钮
			public void actionPerformed(ActionEvent e)
			{
				String word = jtfWord.getText();
				if(word.equals(new String("")))
				{
					
				}
				else
				{
					
					word = word.replaceAll(" ", "%20");
					//System.out.print(word);
					try {
						if(jcbYoudao.isSelected())
						{
							jtaYoudao.setText(GetTrans.getFromYoudao(word));
							b[0] = false;
						}
						else
							jtaYoudao.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						if(jcbJinshan.isSelected())
						{
							jtaJinshan.setText(GetTrans.getFromJinshan(word));
							b[1] = false;
						}
						else
							jtaJinshan.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					word = word.replaceAll("%20", "+");
					try {
						if(jcbBing.isSelected())
						{
							jtaBing.setText(GetTrans.getFromBing(word));
							b[2] = false;
						}
						else
							jtaBing.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
    	
    	//用户注册按钮
    	jmi1.addActionListener(new SigninActionListener());
    	
    	//用户登陆按钮
    	jmi2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					logintask = new LoginTask(odc,socket, toServer, fromServer);
					thread = new Thread(logintask);
					thread.start();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
    		
    	});
    	//用户注销按钮
    	jmi3.addActionListener(new ActionListener(){
    		@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
    		{
    			setTitle("在线词典ver1.0-离线");
    			jmi2.setEnabled(true);
    			JOptionPane.showMessageDialog(null, "线上功能已关闭", "注销成功", JOptionPane.INFORMATION_MESSAGE);
    			for(int i = 0; i < 3; i++)
    				jbtzan[i].setText("\n 赞 \n");
    			thread.stop();
    			Message m = new Message();
    			try {
    				m.username = username;
					toServer.writeObject(m);
					toServer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			jmi3.setEnabled(false);
    			jtaUL.setText("");
    		}
    	});
    	jmi3.setEnabled(false);
    	//赞有道
    	jbtzan[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(jtaYoudao.getText().equals(""))
				{
					return;
				}
				if(!b[0])
				{
					b[0] = true;
					numYoudao++;
				}
				else
				{
					b[0] = false;
					numYoudao--;
				}
				jbtzan[0].setText("赞"+numYoudao);
				sort();
				Message m = new Message(true,numYoudao,numJinshan,numBing);
				m.type = Message.ZAN;
				m.username = username;
				try {
					toServer.writeObject(m);
					toServer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
    	});
    	//赞金山
    	jbtzan[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(jtaJinshan.getText().equals(""))
				{
					return;
				}
				if(!b[1])
				{
					b[1] = true;
					numJinshan++;
				}
				else
				{
					b[1] = false;
					numJinshan--;
				}
				jbtzan[1].setText("赞"+numJinshan);
				sort();
				Message m = new Message(true,numYoudao,numJinshan,numBing);
				m.type = Message.ZAN;
				m.username = username;
				try {
					toServer.writeObject(m);
					toServer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
    	});
    	jbtzan[2].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(jtaBing.getText().equals(""))
				{
					return;
				}
				if(!b[2])
				{
					b[2] = true;
					numBing++;
				}
				else
				{
					b[2] = false;
					numBing--;
				}
				jbtzan[2].setText("赞"+numBing);
				sort();
				Message m = new Message(true,numYoudao,numJinshan,numBing);
				m.type = Message.ZAN;
				m.username = username;
				try {
					toServer.writeObject(m);
					toServer.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
    		
    	});
    	jbtshare[0].addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(jtfWord.getText().equals("")||jtaYoudao.getText().equals(""))
					return;
				String inputValue = JOptionPane.showInputDialog("请输入对方的用户名");
				Message m = new Message();
				m.type = Message.SHARE;
				m.username = inputValue;
				m.password = username;
				m.ShrWord = jtfWord.getText();
				m.ShrMeaning = jtaYoudao.getText();
				try {
					toServer.writeObject(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    	});
    	jbtshare[1].addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(jtfWord.getText().equals("")||jtaJinshan.getText().equals(""))
					return;
				String inputValue = JOptionPane.showInputDialog("请输入对方的用户名");
				Message m = new Message();
				m.type = Message.SHARE;
				m.username = inputValue;
				m.password = username;
				m.ShrWord = jtfWord.getText();
				m.ShrMeaning = jtaJinshan.getText();
				try {
					toServer.writeObject(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    	});
    	jbtshare[2].addActionListener(new ActionListener()
    	{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(jtfWord.getText().equals("")||jtaBing.getText().equals(""))
					return;
				String inputValue = JOptionPane.showInputDialog("请输入对方的用户名");
				Message m = new Message();
				m.type = Message.SHARE;
				m.username = inputValue;
				m.password = username;
				m.ShrWord = jtfWord.getText();
				m.ShrMeaning = jtaBing.getText();
				try {
					toServer.writeObject(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
    		
    	});
    	this.setVisible(true);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jbt)
        {
            String word = jtfWord.getText();
            try {
				jtaYoudao.setText(GetTrans.getFromYoudao(word));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jtaYoudao.setText("未查找到释义.");
			}
            try {
				jtaJinshan.setText(GetTrans.getFromJinshan(word));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jtaJinshan.setText("未查找到释义.");
			}
            try {
				jtaBing.setText(GetTrans.getFromBing(word));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jtaBing.setText("未查找到释义.");
			} 
        }
	}
	class SigninActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
				Runnable task = new SigninTask();
				new Thread(task).start();
		}
	}
	class SigninTask extends JFrame implements Runnable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JLabel jlab1 = null;
		JLabel jlab2 = null;
		JButton jbt1 = null;
		JButton jbt2 = null;
		JTextField jtf = null;
		JPasswordField jpw = null;
		
		public SigninTask()
		{
			super("用户注册");
			this.setResizable(false);
			jlab1 = new JLabel("账号");
			jlab2 = new JLabel("密码");
			jbt1 = new JButton("注册");
			jbt2 = new JButton("取消");
			jtf = new JTextField();
			jpw = new JPasswordField();
			
			setLayout(null);
			
			jlab1.setBounds(60,50,40,25);
			jlab2.setBounds(60,90,40,25);
			jbt1.setBounds(80,120,70,32);
			jbt2.setBounds(160,120,70,32);
			jtf.setBounds(90,50,120,25);
			jpw.setBounds(90,90,120,25);
			
			setBounds(500,400,300,200);
			add(jlab1);add(jtf);
			add(jlab2);add(jpw);
			add(jbt1);add(jbt2);
			
			Toolkit tk = this.getToolkit();
	    	Dimension dm = tk.getScreenSize(); 
	    	this.setLocation((int)(dm.getWidth()-300)/2,(int)(dm.getHeight()-200)/2);
	    	
	    	//注册
	    	jbt1.addActionListener(new ActionListener(){
	    		@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e)
	    		{
	    			String username, password;
	    			username = jtf.getText();
	    			password = jpw.getText();
	    			
	    			Message m = new Message(Message.SIGNIN, username, password);		
	    			try {
	    				@SuppressWarnings("resource")
						Socket socket= new Socket("172.26.121.75",8007);
		    			ObjectOutputStream toServer;
		    			ObjectInputStream fromServer;
						toServer =  new ObjectOutputStream(socket.getOutputStream());
						toServer.flush();
						fromServer = new ObjectInputStream(socket.getInputStream());
						
						toServer.writeObject(m);
						m = (Message) fromServer.readObject();
						if(m.b)
						{	
							//注册成功
							JOptionPane.showMessageDialog(null, "赶紧开始你的单词之旅", "注册成功", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
						else
						{
							//注册失败
					    	JOptionPane.showMessageDialog(null, "用户名已存在","注册失败",  JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			
	    		}
	    	});
	    	//取消
	    	jbt2.addActionListener(new ActionListener(){
	    		public void actionPerformed(ActionEvent e)
	    		{
	    			dispose();
	    		}
	    	});
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.setVisible(true);
		}
	}
	
	class LoginTask extends JFrame implements Runnable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JLabel jlab1 = null;
		JLabel jlab2 = null;
		JButton jbt1 = null;
		JButton jbt2 = null;
		JTextField jtf = null;
		JPasswordField jpw = null;
		OnlineDictionaryClient jf;
		Socket loginSocket;
		ObjectOutputStream toServer;
		ObjectInputStream fromServer;
		
		public LoginTask(OnlineDictionaryClient jf, Socket socket, 
				ObjectOutputStream toServer, ObjectInputStream fromServer) throws UnknownHostException, IOException
		{
			super("用户登陆");
			this.jf = jf;
			
			loginSocket= socket;
			this.toServer =  toServer;
			this.fromServer = fromServer;
			
			this.setResizable(false);
			jlab1 = new JLabel("账号");
			jlab2 = new JLabel("密码");
			jbt1 = new JButton("登录");
			jbt2 = new JButton("取消");
			jtf = new JTextField();
			jpw = new JPasswordField();
			
			setLayout(null);
			
			jlab1.setBounds(60,50,40,25);
			jlab2.setBounds(60,90,40,25);
			jbt1.setBounds(80,120,70,32);
			jbt2.setBounds(160,120,70,32);
			jtf.setBounds(90,50,120,25);
			jpw.setBounds(90,90,120,25);
			
			setBounds(500,400,300,200);
			add(jlab1);add(jtf);
			add(jlab2);add(jpw);
			add(jbt1);add(jbt2);
			
			Toolkit tk = this.getToolkit();
	    	Dimension dm = tk.getScreenSize(); 
	    	this.setLocation((int)(dm.getWidth()-300)/2,(int)(dm.getHeight()-200)/2);
	    	
	    	//登录
	    	jbt1.addActionListener(new ActionListener(){
	    		@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e)
	    		{
	    			String username, password;
	    			username = jtf.getText();
	    			password = jpw.getText();
	    			
	    			Message m = new Message(Message.LOGIN, username, password);
	    			
	    			try {

						toServer.writeObject(m);
						toServer.flush();
						m = (Message) fromServer.readObject();
						if(m.b)
						{	
							//登陆成功
							JOptionPane.showMessageDialog(null, "线上功能已启用", "登陆成功", JOptionPane.INFORMATION_MESSAGE);
							hasLogin = true;
							jf.setTitle("在线词典ver1.0-在线");
							jf.jmi2.setEnabled(false);
							jf.jmi3.setEnabled(true);
						
							jf.numYoudao = m.numYoudao;
							jf.numJinshan = m.numJinshan;
							jf.numBing = m.numBing;
							jf.username = username;
							
							jf.jbtzan[0].setText("赞"+m.numYoudao);
							jf.jbtzan[1].setText("赞"+m.numJinshan);
							jf.jbtzan[2].setText("赞"+m.numBing);
							jf.sort();
							dispose();
							new Thread(new ListenerTask()).start();
						}
						else
						{
							//登陆失败
					    	JOptionPane.showMessageDialog(null, "用户名与密码无法登陆","登陆失败",  JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			
	    		}
	    	});
	    	//取消
	    	jbt2.addActionListener(new ActionListener(){
	    		public void actionPerformed(ActionEvent e)
	    		{
	    			dispose();
	    		}
	    	});
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			this.setVisible(true);
		}
	}
	class ListenerTask implements Runnable
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message k;
			try {
				k = (Message) fromServer.readObject();
				jtaUL.setText(k.userlist);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while(true)
			{
				try {
					k = (Message) fromServer.readObject();
					if(k.type == Message.USERLIST){
						jtaUL.setText(k.userlist);
					}
					if(k.type == Message.SHARE)
					{
						JOptionPane.showMessageDialog(null, k.ShrMeaning,k.ShrWord, 
								JOptionPane.INFORMATION_MESSAGE); 
					}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public void sort()
	{
		p7.remove(jpYoudao);
		p7.remove(jpJinshan);
		p7.remove(jpBing);
		//youdao > jinshan
		if(numYoudao > numJinshan)
		{
			if(numJinshan > numBing)
			{
				p7.add(jpYoudao);
				p7.add(jpJinshan);
				p7.add(jpBing);
			}
			else if(numYoudao > numBing)
			{
				p7.add(jpYoudao);
				p7.add(jpBing);
				p7.add(jpJinshan);
			}
			else
			{
				p7.add(jpBing);
				p7.add(jpYoudao);
				p7.add(jpJinshan);
			}
		}
		//jinshan > youdao
		else
		{
			if(numBing > numJinshan)
			{
				p7.add(jpBing);
				p7.add(jpJinshan);
				p7.add(jpYoudao);
			}
			else if(numBing > numYoudao)
			{
				p7.add(jpJinshan);
				p7.add(jpBing);
				p7.add(jpYoudao);
			}
			else
			{
				p7.add(jpJinshan);
				p7.add(jpYoudao);
				p7.add(jpBing);
			}
		}
	}
}

