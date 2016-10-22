import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;


public class Controler {
	private Map<String, ChattingDlg> mChattingDlgs;
	
	private String mMyIp;
	private WaitingDlg mWaitingDlg;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Controler() {
		try {
			mMyIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "��ȡ����Ip����");
			System.exit(0);
		}
		String serverIP = JOptionPane.showInputDialog(null, "���������IP��", "����");
		try {
			socket = new Socket(serverIP, 9621);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			this.sendCommand("test", mMyIp);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "���ӷ�����ʧ��");
			System.exit(0);
		}
		
		this.mChattingDlgs = new HashMap<String, ChattingDlg>();
		//this.mChattingDlgs = new ChattingDlg[CHATTING_AMOUNT];
		
	}
	public void showWaitingDlg() {
		mWaitingDlg = new WaitingDlg(this);
		mWaitingDlg.setVisible(true);
	}
	public void startNewChat(String IP) {
		if(this.mChattingDlgs.get(IP) != null) {
			JOptionPane.showMessageDialog(null, "�Ự���ڽ���");
			return ;
		}
		ChattingDlg chattingDlg = new ChattingDlg(this, IP);
		chattingDlg.setVisible(true);
		this.mChattingDlgs.put(IP, chattingDlg);
		this.sendCommand("OK", IP);
	}
	public String getMyIP() {
		return mMyIp;
	}
	public void sendCommand(String cmd, String IP) {
		try {
			oos.writeUTF("cmd:"+IP+":"+cmd);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���ݷ��ͳ���");
			System.exit(0);
		}
	}
	public void sendMessage(String msg, String IP) {
		try {
			oos.writeUTF("msg:"+IP+":"+msg);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "���ݷ��ͳ���");
			System.exit(0);
		}
	}
	private void waitingForMsg() {
		String msg = null;
		String[] parse;
		while(true) {
			try {
				msg = ois.readUTF();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "���ӶϿ�");
				System.exit(0);
			}
			parse = msg.split(":", 3);
			if(parse[0].equals("msg")) {
				this.dispatchMsg(parse[2], parse[1]);
			}
			else if(parse[0].equals("cmd")) {
				if(parse[2].equals("null")) {
					JOptionPane.showMessageDialog(null, "IP�����ڻ�Է�������");
					ChattingDlg dlg = this.mChattingDlgs.get(parse[1]);
					if(dlg != null) {
						dlg.dispose();
						this.mChattingDlgs.remove(parse[1]);
					}
				}
			}
		}
	}

	private void dispatchMsg(String msg, String IP) {
		ChattingDlg dlg = this.mChattingDlgs.get(IP);
		if(dlg != null) {
			dlg.printMsg(msg);
		}
		else {
			dlg = new ChattingDlg(this, IP);
			dlg.setVisible(true);
			this.mChattingDlgs.put(IP, dlg);
			dlg.printMsg(msg);
		}
	}
	public void closeChat(String IP) {
		mChattingDlgs.remove(IP);
	}
	public static void main(String[] args) {
		Controler controler = new Controler();
		controler.showWaitingDlg();
		controler.waitingForMsg();
	}
}
