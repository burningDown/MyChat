import java.io.IOException;
import java.io.ObjectInputStream;


public interface Users {
	public void sendMessage(String msg, String senderIP) throws IOException;
	public void sendCommand(String cmd, String senderIP) throws IOException;
}
