import java.net.*;
import java.io.*;
import java.util.*;

public class Client implements Serializable
{
	private String username;
	private String password;

	public static void displayMenu(ArrayList<String> menu)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			System.out.println(menu.get(i));
		}
	}

	public Client(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public static void main(String[] args) throws IOException
	{
		Auction auction = null;
		Socket s = new Socket("localhost", 4999);
		ArrayList<String> menu = null;

		Scanner input = new Scanner(System.in);
		ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());

		String reply = null;
		reply = objectIn.readUTF();
		System.out.println(reply);

		String username, password;
		System.out.println("Username: ");
		username = input.nextLine();
		objectOut.writeUTF(username);
		
		System.out.println("\nPassword: ");
		password = input.nextLine();
		objectOut.writeUTF(password);
		objectOut.flush();

		reply = objectIn.readUTF();
		System.out.println(reply);

		// try
		// {
		// 	auction = (Auction) objectIn.readObject();
		// }
		// catch(Exception e)
		// {
		// 	System.out.println(e);
		// }
		
		// auction.listAuctionItems();
		// System.out.println("\n");

		try {
			menu = (ArrayList<String>) objectIn.readObject();
		} catch(Exception e) {
			System.out.println(e);
		}

		int i = 0;

		do
		{
			// auction = null;
			// try {
			// 	auction = (Auction) objectIn.readObject();
			// } catch(Exception e) {
			// 	System.out.println(e);
			// }
			// auction.listAuctionItems();
			// System.out.println("\n");
			
			displayMenu(menu);

			i = input.nextInt();
			objectOut.writeInt(i);
			objectOut.flush();
			input.nextLine();
			// Get input back
			switch (i)
			{
				// Make a bid
				case 1:
				{
					// try {
					// 	auction = (Auction) objectIn.readObject();
					// 	System.out.println("Reassigned in 1 start!\n");				
					// } catch(Exception e) {
					// 	System.out.println(e);
					// }
					// auction.listAuctionItems();
					System.out.println("\n");
					
					reply = objectIn.readUTF();
					System.out.println(reply);

					// auction.listAuctionItems();
					int itemNum = input.nextInt();
					objectOut.writeInt(itemNum);
					objectOut.flush();
					input.nextLine();

					reply = objectIn.readUTF();
					System.out.println(reply);

					float bid = input.nextFloat();
					objectOut.writeFloat(bid);
					objectOut.flush();
					input.nextLine();

					reply = objectIn.readUTF();
					System.out.println(reply);

					try {
						auction = (Auction) objectIn.readObject();
						System.out.println("Reassigned in 1 end!\n");				
					} catch(Exception e) {
						System.out.println(e);
					}

					// boolean bidPlaced = auction.placeBid(itemNum, bid, null);
					// System.out.println(bidPlaced);
					// int num = objectIn.readInt();
					// System.out.println(num);
					auction.listAuctionItems();
					break;
				}

				// Add new item for auction
				case 2:
				{
					try {
						auction = (Auction) objectIn.readObject();
						System.out.println("Reassigned in 2 start!\n");				
					} catch(Exception e) {
						System.out.println(e);
					}

					reply = objectIn.readUTF();
					System.out.println(reply);
					String itemName = null;
					float startingBid = 0;

					System.out.println("What would you like to auction: ");
					itemName = input.nextLine();

					System.out.println("What is the starting bid: ");
					startingBid = input.nextFloat();

					objectOut.writeUTF(itemName);
					objectOut.writeFloat(startingBid);
					objectOut.flush();
					input.nextLine();

					// auction.addItem(itemName, startingBid);
					// objectOut.writeObject(auction);
					// try {
					// 	auction = (Auction) objectIn.readObject();
					// } catch(Exception e) {
					// 	System.out.println(e);
					// }

					// System.out.println("Below should be new!\n");
					// auction.listAuctionItems();
					try {
						auction = (Auction) objectIn.readObject();
						System.out.println("Reassigned in 2 end!\n");				
					} catch(Exception e) {
						System.out.println(e);
					}

					break;
				}

				// Leave auction
				case 3:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
					break;
				}

				default:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
				}
			}
			System.out.println("\n\n");
		}while(i < 6);
		s.close();
	}
}