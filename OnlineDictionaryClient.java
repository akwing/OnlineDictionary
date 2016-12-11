package OnlineDic;

import OnlineDic.GetTrans;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
	
	private JTextField jtfWord = new JTextField(40);
	  
    private JTextArea jtfYoudao = new JTextArea(8,50), jtfJinshan = new JTextArea(8,50), jtfBing = new JTextArea(8,50);
    private JTextArea jtf = new JTextArea(10,10);
  
    private JButton jbt = new JButton("查询");
    
    private JCheckBox jcbYoudao = new JCheckBox("有道",true), 
    		jcbJinshan =  new JCheckBox("金山",true), 
    		jcbBing = new JCheckBox("必应",true);
    
    private JMenuBar jmb = new JMenuBar();
    private JMenu logMenu = new JMenu("账户"), onlineMenu = new JMenu("线上"), helpMenu = new JMenu("帮助");
    private JMenuItem jmi1 = new JMenuItem("用户注册"), jmi2 = new JMenuItem("用户登陆"), jmi3 = new JMenuItem("用户注销");
    
    JPanel p1, p2, p3;
    
    public OnlineDictionaryClient()
    {
    	super("在线词典ver1.0");
    	this.setVisible(true);
    	setResizable(false);
    	this.setSize(800, 600);
    	
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
    	
		//输出区
		p2 = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
		p2.add(jcbYoudao);
		p2.add(jtfYoudao);
		p2.add(jcbJinshan);
		p2.add(jtfJinshan);
		p2.add(jcbBing);
		p2.add(jtfBing);
		jtfYoudao.setEditable(false);
		jtfJinshan.setEditable(false);
		jtfBing.setEditable(false);
		p2.setBorder(new TitledBorder("翻译区"));
		this.add(p2,BorderLayout.CENTER);
		
		//用户列表区（暂定）
		p3 = new JPanel(new FlowLayout(FlowLayout.CENTER,4,4));
		p3.add(jtf);
		p3.setBorder(new TitledBorder("用户区"));
		this.add(p3,BorderLayout.WEST);
		
    	this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
            	//在此处需要增加用户注销的信息
                System.exit(-1);
            }
        });
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jbt)
        {
            String word = jtfWord.getText();
            try {
				jtfYoudao.setText(GetTrans.getFromYoudao(word));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jtfYoudao.setText("未查找到释义.");
			}
            try {
				jtfJinshan.setText(GetTrans.getFromJinshan(word));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jtfJinshan.setText("未查找到释义.");
			}
            try {
				jtfBing.setText(GetTrans.getFromBing(word));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jtfBing.setText("未查找到释义.");
			}
            
        }
	}

}
