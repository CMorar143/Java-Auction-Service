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

	private static Timer timer = new Timer();
	private static Timer checkTimer = new Timer();

	private static TimerTask check = new TimerTask() {
		public void run()
		{
			// Announce winner of auction and move onto next item
			if (MyTimerTask.isAuctionOver())
			{
				System.out.println("WORKS");
			}

			else
			{
				System.out.println("working so far");
			}
		}
	};

	public static void AnnounceWinner(Item sold)
	{
		// Item item = auction.auctionItem();
		// System.out.println("winner declared here" + item.getHighestBidder().getUsername());
	}

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

			MyTimerTask task = new MyTimerTask(auction.auctionItem());
			checkTimer.schedule(check, 0, 1000);
			timer.schedule(task, 6000);
			// System.out.println("winner declared here" + sold.getHighestBidder().getUsername());
			auction.startTimer();
			auction.getTimeRemaining();

			reply = "Enter your username and password\n";
			System.out.println(reply);
			objectOut.writeUTF(reply);
			objectOut.flush();

			String username, password;
			username = objectIn.readUTF();
			password = objectIn.readUTF();

			Client c = new Client(username, password);
			boolean clientAdded = auction.addClient(c);
			if(clientAdded)
			{
				System.out.println("client added to auction");
			}

			objectOut.writeObject(menu);

			Item item = auction.auctionItem();
			objectOut.writeObject(item);

			int i = 0;

			do
			{
				i = objectIn.readInt();
				// Check timer
				switch (i)
				{
					case 1:
					{
						// Check timer
						objectOut.flush();
						objectOut.reset();
						// Send item infor thats on sale
						item = auction.auctionItem();
						objectOut.writeObject(item);
						reply = "What would you like to bid? (Must be greater than the current bid)\n";
						System.out.println(reply);
						objectOut.writeUTF(reply);
						objectOut.flush();

						float bid = objectIn.readFloat();
						// Check timer
						boolean bidPlaced = auction.placeBid(bid, c);

						if(bidPlaced)
						{
							reply = "Congrats you are now the highest bidder!\n";
							System.out.println(reply);
							objectOut.writeUTF(reply);
							objectOut.flush();
							// auction.stopTimers();
						}

						else
						{
							reply = "That didn't work!\n";
							System.out.println(reply);
							objectOut.writeUTF(reply);
							objectOut.flush();
						}
	
						break;
					}

					case 2:
					{
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
	
	static class MyTimerTask extends TimerTask  
	{
		private Item item;
		private static boolean isFinished = false;

		public MyTimerTask(Item item) 
		{
			this.item = item;
		}

		@Override
		public void run() 
		{
			// Announce winner of auction and move onto next item
			System.out.println("you entered the other timer class");
			// returnItem();
			isFinished = true;
	    }

		public static boolean isAuctionOver() 
		{
	    	return isFinished;
		}

	    // public Item returnItem()
	    // {

	    // }
	}
}



// Create the same timer on the server side
// When the timer elapses invoke a method
// The method call a method on the client side (passing the item reference)
// print the highest bidder and bid on the client side
// remove that item from the auction
// restart timers with the new auction


// Make another timer
// every 2 seconds this new timer will check if the other main one is running
// if it is
	// display winner
// else
	// dont do anything