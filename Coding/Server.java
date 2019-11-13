import java.net.*;
import java.io.*;
import java.util.*;

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
		Auction auction = new Auction();
		ArrayList<String> menu = auction.displayMenu();

		while(true)
		{
			Socket s = ss.accept();
			
			// OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
			// BufferedWriter bf = new BufferedWriter(out);
			
			System.out.println("client connected");
			
			
			// InputStreamReader in = new InputStreamReader(s.getInputStream());
			// BufferedReader br = new BufferedReader(in);
			// DataInputStream dis = new DataInputStream(s.getInputStream());
			ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());

			objectOut.writeObject(menu);

			ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());
			int i = 0;

			do
			{
				i = objectIn.readInt();
				// System.out.println("client chose : " + i);
				System.out.println(i);
			}while(i < 6);
			// objectOut.writeObject(auction);
			// PrintWriter pr = new PrintWriter(s.getOutputStream());
			// pr.println("test");
			// pr.flush();

			// String st = "teststr";

			// bf.write(st);
			// bf.flush();

			// System.out.println(menu);

			// String str = "Choose an option from the menu";
			// bf.write(str + "\r\n");
			// bf.flush();

			// Send menu to client
			// for (int i = 0; i < menu.size(); i++)
			// {
			// // 	str = menu.get(i);
			// // 	bf.write(str + "\r\n");
			// // 	bf.flush();
   //              pr.println(menu.get(i) + "\r\n");
   //              System.out.println(menu.get(i));
			// }

			// ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
			// InputStreamReader in = new InputStreamReader(s.getInputStream());
			// BufferedReader bf = new BufferedReader(in);

			// String str = bf.readLine();
			// System.out.println("client : " + str);
			// Display menu
			
			// objectOut.flush();
			s.close();
		}
		// ss.close();
	}
}