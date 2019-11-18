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

			// objectOut.writeObject(auction);
			objectOut.writeObject(menu);

			int i = 0;

			do
			{
				// objectOut.writeObject(auction);
				i = objectIn.readInt();
				// System.out.println("client chose : " + i);
				// System.out.println(i);
				switch (i)
				{
					case 1:
					{
						// objectOut.writeObject(auction);
						reply = "Please choose which item number you would Like to bid on\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();

						int itemNum = objectIn.readInt();
						// auction.listAuctionItems();
						// float currentBid = auction.getItemBid(itemNum);
						reply = "What would you like to bid? (Must be greater than the current bid)\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();

						float bid = objectIn.readFloat();
						boolean bidPlaced = auction.placeBid(itemNum, bid, c);

						if(bidPlaced)
						{
							reply = "Congrats you are now the highest bidder!\n";
							System.out.println(reply);
							objectOut.writeUTF(reply);
							objectOut.flush();
							auction.listAuctionItems();
						}

						else
						{
							reply = "That didn't work!\n";
							System.out.println(reply);
							objectOut.writeUTF(reply);
							objectOut.flush();
							auction.listAuctionItems();
						}
						objectOut.writeObject(auction);
						// objectOut.writeObject(auction);
						// int num = 0;
						// objectOut.writeInt(num);
						// objectOut.flush();
						// input.nextLine();
						// auction.listAuctionItems();
						break;
					}

					case 2:
					{
						objectOut.writeObject(auction);
						// auction.listAuctionItems();
						reply = "You want to create a new auction\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();
						// try {
						// 	auction = (Auction) objectIn.readObject();
						// } catch(Exception e) {
						// 	System.out.println(e);
						// }
						String itemName = objectIn.readUTF();
						float startingBid = objectIn.readFloat();
						auction.addItem(itemName, startingBid);
						// objectOut.writeObject(auction);
						auction.listAuctionItems();
						System.out.println("test2 should be above!\n");
						objectOut.writeObject(auction);
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
				// objectOut.writeObject(auction);
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