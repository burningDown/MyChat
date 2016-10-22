import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class WaitingDlg extends JFrame implements ActionListener{
	
	private String mMyIp;
	private Controler controler;
	private JLabel mLabMyIp;
	private JLabel mLabWaiting;
	private JLabel mLabStartChat;
	private JTextField mTextInputIp;
	private JButton mBtnStartChat;
	
	public WaitingDlg(Controler controler) {
		this.setSize(300, 500);
		this.setTitle("MyChat");
		this.controler = controler;
		mMyIp = new String(controler.getMyIP());
		this.mLabMyIp = new JLabel("本机IP："+mMyIp);
		this.mLabWaiting = new JLabel("等待连接...");
		this.mLabStartChat = new JLabel("输入Ip开始新对话");
		this.mTextInputIp = new JTextField();
		this.mBtnStartChat = new JButton("连接");
		this.mBtnStartChat.addActionListener(this);
		

		GridBagLayout mainLayout = new GridBagLayout();
		JPanel panelForButton = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panelForButton.setLayout(flowLayout);
		panelForButton.add(mBtnStartChat);
		
		this.setLayout(mainLayout);
		this.add(mLabMyIp);
		this.add(mLabStartChat);
		this.add(mLabStartChat);
		this.add(mTextInputIp);
		this.add(panelForButton);
		
		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		s.insets = new Insets(0, 10, 20, 10);
		s.weightx = 1;
		s.gridwidth = 0;
		s.weighty = 0;
		mainLayout.setConstraints(mLabMyIp, s);
		mainLayout.setConstraints(mLabWaiting, s);
		mainLayout.setConstraints(mLabStartChat, s);
		mainLayout.setConstraints(mTextInputIp, s);
		mainLayout.setConstraints(panelForButton, s);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(mBtnStartChat)) {
			this.controler.startNewChat(this.mTextInputIp.getText());
		}
	}
	
}
