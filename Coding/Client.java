import java.net.*;
import java.io.*;
import java.util.*;

public class Client implements Serializable
{
	private String username;
	public static boolean newBid = false;

	public static void displayMenu(ArrayList<String> menu)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			System.out.println(menu.get(i));
		}
	}

	public Client(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return this.username;
	}

	public static void main(String[] args) throws IOException
	{

		Socket s = new Socket("localhost", 4999);
		ArrayList<String> menu = null;
		boolean exit = false;
		boolean allowedToLogin = false;

		Scanner input = new Scanner(System.in);
		ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());

		String reply = null;
		reply = objectIn.readUTF();
		System.out.println(reply);

		String username;
		
		while(!allowedToLogin)
		{
			System.out.print("Please enter a unique username: ");
			username = input.nextLine();
			objectOut.writeUTF(username);
			objectOut.flush();
			allowedToLogin = objectIn.readBoolean();

			if (!allowedToLogin)
			{
				System.out.println(username + " is already taken!");
			}
		}

		try {
			menu = (ArrayList<String>) objectIn.readObject();
		} catch(Exception e) {
			System.out.println(e);
		}

		Item item = null;

		try {
			item = (Item) objectIn.readObject();
		} catch(Exception e) {
			System.out.println(e);
		}

		System.out.println("\nThe item currently on sale is:");
		System.out.println("Item Name: " + item.getItemName());
		System.out.println("Current Bid: " + item.getCurrentBid() + "\n");

		int i = 0;

		do
		{
			// Display item thats on sale
			displayMenu(menu);
			System.out.println();
			
			do
			{
				System.out.print("Please choose what you would like to do: ");
				while (!input.hasNextInt())
				{
					System.out.print("Please choose a valid number");
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

					try {
						item = (Item) objectIn.readObject();
					} catch(Exception e) {
						System.out.println(e);
					}

					if (item != null)
					{
						System.out.println("\nThe item currently on sale is:");
						System.out.println("Item Name: " + item.getItemName());
						System.out.println("Current Bid: " + item.getCurrentBid() + "\n");

						reply = objectIn.readUTF();
						System.out.println(reply);

						float bid = input.nextFloat();
						objectOut.writeFloat(bid);
						objectOut.flush();
						input.nextLine();

						reply = objectIn.readUTF();
						System.out.println(reply);
					}

					else
					{
						System.out.println("No item to bid on!");
					}
					break;
				}

				// Add new item for auction
				case 2:
				{
					String itemName = null;
					float startingBid = 0;

					System.out.print("What would you like to auction off: ");
					itemName = input.nextLine();

					System.out.print("What is the starting bid: ");
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
			System.out.println();
		} while(!exit);
		s.close();
	}
}