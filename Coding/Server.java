import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server /*implements Runnable*/
{  
	// Array of clients	
	// private ChatServerThread clients[] = new ChatServerThread[50];
	// private ServerSocket server = null;
	// private Thread       thread = null;
	// // private Auction 	 auction = null;
	// private int 		 clientCount = 0;

	// public void run()
	// {

	// }

	public static void main(String[] args) throws IOException
	{
		Scanner input = new Scanner(System.in);
		ServerSocket ss = new ServerSocket(4999);
		Auction auction = null;
		
		// while(auction != null)
		// {
			Socket s = ss.accept();

			System.out.println("client connected");
			
			// ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
			// // InputStreamReader in = new InputStreamReader(s.getInputStream());
			// // BufferedReader bf = new BufferedReader(in);

			// // String str = bf.readLine();
			// // System.out.println("client : " + str);

			// objectOut.writeObject(auction);
			// // PrintWriter pr = new PrintWriter(s.getOutputStream());
			// pr.println("test");
			// pr.flush();
		// }



		ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());
		// InputStreamReader in = new InputStreamReader(s.getInputStream());
		// BufferedReader bf = new BufferedReader(in);

		// String str = bf.readLine();
		// System.out.println("server : " + str);

		try
		{
			auction = (Auction) objectIn.readObject();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		if (auction != null)
		{
			System.out.println(":)");
		}

		auction.displayMenu();

		ss.close();
		s.close();
	}
}