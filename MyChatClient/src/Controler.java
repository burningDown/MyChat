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
			JOptionPane.showMessageDialog(null, "获取本机Ip出错");
			System.exit(0);
		}
		String serverIP = JOptionPane.showInputDialog(null, "输入服务器IP：", "输入");
		try {
			socket = new Socket(serverIP, 9621);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			this.sendCommand("test", mMyIp);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "连接服务器失败");
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
			JOptionPane.showMessageDialog(null, "会话正在进行");
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
			JOptionPane.showMessageDialog(null, "数据发送出错");
			System.exit(0);
		}
	}
	public void sendMessage(String msg, String IP) {
		try {
			oos.writeUTF("msg:"+IP+":"+msg);
			oos.flush();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "数据发送出错");
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
				JOptionPane.showMessageDialog(null, "连接断开");
				System.exit(0);
			}
			parse = msg.split(":", 3);
			if(parse[0].equals("msg")) {
				this.dispatchMsg(parse[2], parse[1]);
			}
			else if(parse[0].equals("cmd")) {
				if(parse[2].equals("null")) {
					JOptionPane.showMessageDialog(null, "IP不存在或对方不在线");
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
