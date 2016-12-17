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
	
	private JTextField jtfWord = new JTextField(40);
	  
    private JTextArea jtaYoudao = new JTextArea(height, width), jtaJinshan = new JTextArea(height, width), jtaBing = new JTextArea(height, width);
    private JButton jbt = new JButton("查询"), jbt1, jbt2, jbt3;
    
    private JCheckBox jcbYoudao = new JCheckBox("有道",true), 
    		jcbJinshan =  new JCheckBox("金山",true), 
    		jcbBing = new JCheckBox("必应",true);
    
    private JMenuBar jmb = new JMenuBar();
    private JMenu logMenu = new JMenu("账户"), onlineMenu = new JMenu("线上"), helpMenu = new JMenu("帮助");
    private JMenuItem jmi1 = new JMenuItem("用户注册"), jmi2 = new JMenuItem("用户登陆"), jmi3 = new JMenuItem("用户注销");
    private JLabel jlab1, jlab2, jlab3;
    
    private JPanel p1, p2, p3, jpYoudao, jpJinshan, jpBing, p7;
    
    public OnlineDictionaryClient()
    {
    	super("在线词典ver1.0");
    	hasLogin = false;
    	setResizable(false);
    	this.setSize(700, 600);
    	Toolkit tk = this.getToolkit();
    	Dimension dm = tk.getScreenSize(); 
    	this.setLocation((int)(dm.getWidth()-700)/2,(int)(dm.getHeight()-700)/2);

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
		
		jpYoudao = new JPanel(new FlowLayout(FlowLayout.LEFT,15,4));
		jpJinshan = new JPanel(new FlowLayout(FlowLayout.LEFT,15,4));
		jpBing = new JPanel(new FlowLayout(FlowLayout.LEFT,15,4));
		
		jbt1 = new JButton("\n 赞 \n");
		jbt2 = new JButton("\n 赞 \n");
		jbt3 = new JButton("\n 赞 \n");
		
		jpYoudao.add(jlab1);jpYoudao.add(jtaYoudao);jpYoudao.add(jbt1);
		
		jpJinshan.add(jlab2);jpJinshan.add(jtaJinshan);jpJinshan.add(jbt2);
		
		jpBing.add(jlab3);jpBing.add(jtaBing);jpBing.add(jbt3);
		
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
							jtaYoudao.setText(GetTrans.getFromYoudao(word));
						else
							jtaYoudao.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						if(jcbJinshan.isSelected())
							jtaJinshan.setText(GetTrans.getFromJinshan(word));
						else
							jtaJinshan.setText("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					word = word.replaceAll("%20", "+");
					try {
						if(jcbBing.isSelected())
							jtaBing.setText(GetTrans.getFromBing(word));
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
    	jmi1.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			Runnable task = new SigninTask();
    			new Thread(task).start();
    		}
    	});
    	
    	//用户登陆按钮
    	jmi2.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			Runnable task = new LoginTask();
    			new Thread(task).start();
    		}
    	});
    	//用户登陆按钮
    	jmi2.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			Runnable task = new LoginTask();
    			new Thread(task).start();
    		}
    	});
    	//用户注销按钮
    	jmi3.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent e)
    		{
    			
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
	    			
	    			//System.out.println(username+"\n"+password);
	    			
	    			Message m = new Message(Message.SIGNIN, username, password);
	    			
	    			ObjectOutputStream toServer;
	    			ObjectInputStream fromServer;
	    			try {
						Socket socket = new Socket("172.26.121.75",8006);
						toServer =  new ObjectOutputStream(socket.getOutputStream());
						toServer.flush();
						fromServer = new ObjectInputStream(socket.getInputStream());
						
						toServer.writeObject(m);
						m = (Message) fromServer.readObject();
						if(m.b)
						{	
							//注册成功
						}
						else
						{
							//注册失败
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			dispose();
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
		
		public LoginTask()
		{
			super("用户登陆");
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
	    		public void actionPerformed(ActionEvent e)
	    		{
	    			String username, password;
	    			username = jtf.getText();
	    			password = jpw.getText();
	    			
	    			//System.out.println(username+"\n"+password);
	    			
	    			Message m = new Message(Message.LOGIN, username, password);
	    			
	    			ObjectOutputStream toServer;
	    			ObjectInputStream fromServer;
	    			try {
						Socket socket = new Socket("172.26.121.75",8006);
						toServer =  new ObjectOutputStream(socket.getOutputStream());
						toServer.flush();
						fromServer = new ObjectInputStream(socket.getInputStream());
						
						toServer.writeObject(m);
						m = (Message) fromServer.readObject();
						if(m.b)
						{	
							//登陆成功
						}
						else
						{
							//登陆失败
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    			dispose();
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
}

