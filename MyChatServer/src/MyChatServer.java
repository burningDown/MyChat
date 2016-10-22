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
				System.out.println("�������С���������IP��"+ServerIP);
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO �Զ����ɵķ������
		String buffer;
		Scanner scanner = new Scanner(System.in);
		while(true) {
			buffer = scanner.nextLine();
			if(buffer.equals("exit")) {
				System.exit(0);
			}
			else if(buffer.equals("users")) {
				System.out.println(mUsers.size()+"������");
				Iterator it = mUsers.keySet().iterator();
				while(it.hasNext()) {
					System.out.println(it.next());
				}
			}
			else if(buffer.equals("ip")) {
				System.out.println("������IP��"+ServerIP);
			}
			else {
				System.out.println("δ��������");
			}
		}
	}
}
