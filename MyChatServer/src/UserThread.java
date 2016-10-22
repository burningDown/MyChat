import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;


public class UserThread extends Thread implements Users{
	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private HashMap<String, Users> mUsers;
	private String mIP;
	public UserThread(Socket accept, HashMap<String, Users> users) {
		client = accept;
		mUsers = users;
	}
	public void run() {
		String msg;
		String[] parse;
		Users user;
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			msg = ois.readUTF();
			mIP = msg.split(":", 3)[1];
			mUsers.put(mIP, this);
			System.out.println("成功连接，IP:"+mIP);
		} catch (IOException e) {
			System.out.println("连接出错");
			System.exit(0);
		}
		try {
			while(true) {
				msg = ois.readUTF();
				parse = msg.split(":", 3);
				if(parse[0].equals("msg")) {
					user = mUsers.get(parse[1]);
					if(user != null) {
						user.sendMessage(parse[2], mIP);
					}
					else {
						this.sendCommand("null", parse[1]);
					}
				}
				else if(parse[0].equals("cmd")) {
					if(parse[2].equals("exit")) {
						client.close();
						ois.close();
						oos.close();
						break;
					}
					else {
						user = mUsers.get(parse[1]);
						if(user != null) {
							user.sendCommand(parse[2], mIP);
						}
						else {
							this.sendCommand("null", parse[1]);
						}
						/*msg = parse[0]+":"+mIP+":"+parse[2];
						oos.writeUTF(msg);
						oos.flush();*/
					}
				}
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
		}
		System.out.println(mIP+"：连接断开");
		mUsers.remove(mIP);
	}
	@Override
	public void sendMessage(String msg, String senderIP) throws IOException {
		// TODO 自动生成的方法存根
		String buffer = "msg:"+senderIP+":"+msg;
		oos.writeUTF(buffer);
		oos.flush();
		
	}
	@Override
	public void sendCommand(String cmd, String senderIP) throws IOException {
		// TODO 自动生成的方法存根
		String buffer = "cmd:"+senderIP+":"+cmd;
		oos.writeUTF(buffer);
		oos.flush();
		
	}
}
