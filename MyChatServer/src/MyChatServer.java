import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;


public class MyChatServer extends ServerSocket implements Runnable{
	private ServerSocket server;
	private static final int PORT = 9621;
	private String ServerIP;
	
	private HashMap<String, Users> mUsers;

	private Socket client;
	public MyChatServer() throws IOException {
		super();
		mUsers = new HashMap<String, Users>();
	}

	private void waitingForConnect() {
		try {
			try {
				server = new ServerSocket(PORT);
				boolean listening = true;
				ServerIP = InetAddress.getLocalHost().getHostAddress();
				System.out.println("正在运行。。服务器IP："+ServerIP);
				while (listening) {
					new UserThread(server.accept(), mUsers).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		try {
			MyChatServer myChatServer = new MyChatServer();
			Thread t = new Thread(myChatServer);
			t.start();
			myChatServer.waitingForConnect();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO 自动生成的方法存根
		String buffer;
		Scanner scanner = new Scanner(System.in);
		while(true) {
			buffer = scanner.nextLine();
			if(buffer.equals("exit")) {
				System.exit(0);
			}
			else if(buffer.equals("users")) {
				System.out.println(mUsers.size()+"人在线");
				Iterator it = mUsers.keySet().iterator();
				while(it.hasNext()) {
					System.out.println(it.next());
				}
			}
			else if(buffer.equals("ip")) {
				System.out.println("服务器IP："+ServerIP);
			}
			else {
				System.out.println("未定义命令");
			}
		}
	}
}
