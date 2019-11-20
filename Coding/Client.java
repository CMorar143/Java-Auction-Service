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
		Socket s = new Socket("localhost", 4999);
		ArrayList<String> menu = null;
		boolean exit = false;

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

		try {
			menu = (ArrayList<String>) objectIn.readObject();
		} catch(Exception e) {
			System.out.println(e);
		}

		Item item = null;

		System.out.println("\n");
		try {
			item = (Item) objectIn.readObject();
		} catch(Exception e) {
			System.out.println(e);
		}

		System.out.println("The item currently on sale is:\n");
		System.out.println("Item Name: " + item.getItemName());
		System.out.println("Current Bid: " + item.getCurrentBid() + "\n");

		int i = 0;

		do
		{
			// Display item thats on sale
			System.out.println("\n");
			displayMenu(menu);
			System.out.println();
			
			do
			{
				System.out.println("Please choose what you would like to do: ");
				while (!input.hasNextInt())
				{
					System.out.println("Please choose a valid number");
					input.next();
				}
				i = input.nextInt();
			} while(i <= 0);
			objectOut.writeInt(i);
			objectOut.flush();
			input.nextLine();
			// Get input back
			switch (i)
			{
				// Make a bid
				case 1:
				{
					item = null;

					System.out.println("\n");
					try {
						item = (Item) objectIn.readObject();
					} catch(Exception e) {
						System.out.println(e);
					}

					System.out.println("The item currently on sale is:\n");
					System.out.println("Item Name: " + item.getItemName());
					System.out.println("Current Bid: " + item.getCurrentBid() + "\n");
					System.out.println("print timer here");

					// reply = objectIn.readUTF();
					// System.out.println(reply);

					// auction.listAuctionItems();
					// int itemNum = input.nextInt();
					// objectOut.writeInt(itemNum);
					// objectOut.flush();
					// input.nextLine();

					reply = objectIn.readUTF();
					System.out.println(reply);

					float bid = input.nextFloat();
					objectOut.writeFloat(bid);
					objectOut.flush();
					input.nextLine();

					reply = objectIn.readUTF();
					System.out.println(reply);
					break;
				}

				// Add new item for auction
				case 2:
				{
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

					break;
				}

				// Leave auction
				case 3:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
					exit = true;
					break;
				}

				default:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
				}
			}
			System.out.println("\n\n");
		}while(!exit);
		s.close();
	}
}