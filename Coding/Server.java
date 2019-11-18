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
			String reply = null;
			System.out.println("client accepted");
			ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());

			reply = "Enter your username and password\n";
			System.out.println(reply);
			objectOut.writeUTF(reply);
			objectOut.flush();

			String username, password;
			username = objectIn.readUTF();
			password = objectIn.readUTF();

			Client c = new Client(username, password);
			
			System.out.println("client added to auction");

			reply = "\n\nThese are the items currently on auction\n";
			System.out.println(reply);
			objectOut.writeUTF(reply);
			objectOut.flush();

			objectOut.writeObject(auction);
			objectOut.writeObject(menu);

			int i = 0;

			do
			{
				i = objectIn.readInt();
				// System.out.println("client chose : " + i);
				// System.out.println(i);
				switch (i)
				{
					case 1:
					{
						reply = "Please choose which item number you would Like to bid on\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();

						int itemNum = objectIn.readInt();
						// auction.listAuctionItems();
						System.out.println("You want to bid on " + itemNum);
						break;
					}

					case 2:
					{
						reply = "You want to create a new auction\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();
						break;
					}

					case 3:
					{
						reply = "You want to leave auction\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();
						break;
					}

					default:
					{
						reply = "You entered invalid input\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();
					}
				}

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