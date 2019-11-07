import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server implements Runnable
{  
	// Array of clients	
	// private ChatServerThread clients[] = new ChatServerThread[50];
	private ServerSocket server = null;
	private Thread       thread = null;
	private Auction 	 auction = null;
	private int 		 clientCount = 0;

	public void run()
	{

	}

	public static void main(String[] args) throws IOException
	{
		Scanner input = new Scanner(System.in);
		ServerSocket ss = new ServerSocket(4999);
		
		while(true)
		{
			Socket s = ss.accept();

			System.out.println("client connected");

			String string = input.nextLine();

			InputStreamReader in = new InputStreamReader(s.getInputStream());
			BufferedReader bf = new BufferedReader(in);

			String str = bf.readLine();
			System.out.println("client : " + str);

			PrintWriter pr = new PrintWriter(s.getOutputStream());
			pr.println(string);
			pr.flush();
			// ss.close();
		}
	}
}