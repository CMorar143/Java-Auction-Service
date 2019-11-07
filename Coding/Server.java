import java.net.*;
import java.io.*;

public class Server implements Runnable
{  
	// Array of clients	
	// private ChatServerThread clients[] = new ChatServerThread[50];
	private ServerSocket server = null;
	private Thread       thread = null;
	private int clientCount = 0;

	public void run()
	{

	}

	public static void main(String[] args) throws IOException
	{
		while(true)
		{
			ServerSocket ss = new ServerSocket(4999);
			Socket s = ss.accept();

			System.out.println("client connected");

			InputStreamReader in = new InputStreamReader(s.getInputStream());
			BufferedReader bf = new BufferedReader(in);

			String str = bf.readLine();
			System.out.println("client : " + str);

			PrintWriter pr = new PrintWriter(s.getOutputStream());
			pr.println("hello from SERVER");
			pr.flush();
			ss.close();
		}
	}
}