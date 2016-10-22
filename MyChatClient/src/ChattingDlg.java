import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;


public class ChattingDlg extends JFrame implements ActionListener{
	private JTextArea mTextOutput;
	private JTextArea mTextInput;
	private JLabel mLabInput;
	private JButton mBtnSend;
	
	private Controler controler;
	private String targetIP;
	public ChattingDlg (Controler controler, String IP) {
		this.setSize(500, 700);
		
		this.controler = controler;
		this.targetIP = IP;
		
		this.setTitle("与"+targetIP+"会话");
		mTextOutput = new JTextArea();
		mTextInput = new JTextArea();
		mLabInput = new JLabel("输入");
		mBtnSend = new JButton("发送");
		mBtnSend.addActionListener(this);
		
		mTextOutput.setEditable(false);
		mTextOutput.setBackground(Color.LIGHT_GRAY);
		
		JScrollPane scrollForOutput = new JScrollPane(mTextOutput);
		
		JPanel panelForBtn = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.CENTER);
		panelForBtn.setLayout(flowLayout);
		panelForBtn.setBackground(Color.darkGray);
		panelForBtn.add(mBtnSend);
		
		JPanel textPanel = new JPanel();
		GridBagLayout textLayout = new GridBagLayout();
		textPanel.setLayout(textLayout);
		textPanel.setBackground(Color.GRAY);
		textPanel.add(scrollForOutput);
		textPanel.add(mLabInput);
		textPanel.add(mTextInput);
		GridBagConstraints s = new GridBagConstraints();
		s.fill = GridBagConstraints.BOTH;
		s.insets = new Insets(10, 2, 0, 2);
		s.weightx = 1;
		s.gridwidth = 0;
		s.weighty = 1;
		s.gridheight = 8;
		textLayout.setConstraints(scrollForOutput, s);
		s.weighty = 0;
		s.gridheight = 1;
		textLayout.setConstraints(mLabInput, s);
		textLayout.setConstraints(mTextInput, s);
		textPanel.setBorder(new EmptyBorder(10, 0, 20, 0));
		
		this.setLayout(new BorderLayout());
		this.add(textPanel, BorderLayout.CENTER);
		this.add(panelForBtn, BorderLayout.SOUTH);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				controler.closeChat(targetIP);
				ChattingDlg.this.dispose();
			}
		});
	}
	public String getIP() {
		return this.targetIP;
	}
	public void printMsg(String msg) {
		this.mTextOutput.append("HE/SHE:\n"+msg+"\n\n");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(this.mBtnSend)) {
			this.controler.sendMessage(this.mTextInput.getText(), targetIP);
			this.mTextOutput.append("YOU:\n"+this.mTextInput.getText()+"\n\n");
			this.mTextInput.setText("");
			//...........................................................
		}
	}
}
