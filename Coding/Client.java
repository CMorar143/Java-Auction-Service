import java.net.*;
import java.io.*;
import java.util.*;

public class Client implements Serializable
{
	private String username;
	private static final int PORT = 1235;

	// Constructor
	public Client(String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return this.username;
	}

	public static void displayMenu(ArrayList<String> menu)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			System.out.println(menu.get(i));
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> getMenu(ObjectInputStream objectIn)
	{
		ArrayList<String> menu = null;

		try {
			menu = (ArrayList<String>) objectIn.readObject();
		} catch(Exception e) {
			System.out.println(e);
		}

		return menu;
	}

	public static void main(String[] args) throws IOException
	{
		Socket s = new Socket(args[0], PORT);
		ArrayList<String> menu = null;
		boolean exit = false;
		boolean allowedToLogin = false;
		String reply = null;
		Item item = null;
		Auction auction = null;
		int menuNum = 0;
		String username;
		ObjectOutputStream objectOut = null;
		ObjectInputStream objectIn = null;
		float bid = 0;

		// For sending and receiving data from the server
		Scanner input = new Scanner(System.in);

		try
		{
			objectOut = new ObjectOutputStream(s.getOutputStream());
			objectIn = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			System.out.println(e);
		}

		// "Enter your username"
		reply = objectIn.readUTF();
		System.out.println(reply);
		
		// Keep looping through until the client enters a unique username
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

		// Read in the main menu
		menu = getMenu(objectIn);

		// Read in the item thats currently on auction
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
		}

		else
		{
			System.out.println("No item to bid on!\nYou will need to add one...");
		}

		// Loop until the client chooses to exit
		do
		{
			// Display the main menu until the client chooses to exit
			displayMenu(menu);
			System.out.println();
			
			// Error checking against invalid menu choices
			// Loops until the client enters a positive integer
			do
			{
				System.out.print("Please choose what you would like to do: ");
				while (!input.hasNextInt())
				{
					System.out.print("Please choose a valid number");
					input.next();
				}
				menuNum = input.nextInt();
			} while(menuNum <= 0);

			// Send client's choice to server
			objectOut.writeInt(menuNum);
			objectOut.flush();
			input.nextLine();

			// Get input back
			switch (menuNum)
			{
				// Make a bid
				case 1:
				{
					// Reassign item object to the one thats currently on auction
					item = null;

					try {
						item = (Item) objectIn.readObject();
					} catch(Exception e) {
						System.out.println(e);
					}

					// Error checking to ensure there is at least one item
					// that's still on auction i.e =. that the auction isn't over
					if (item != null)
					{
						System.out.println("\nThe item currently on sale is:");
						System.out.println("Item Name: " + item.getItemName());
						System.out.println("Current Bid: " + item.getCurrentBid() + "\n");

						reply = objectIn.readUTF();
						System.out.println(reply);

						// Send bid amount to the server
						do
						{
							while (!input.hasNextFloat())
							{
								System.out.print("Please choose a valid number: ");
								input.next();
							}
							bid = input.nextFloat();
						} while(bid <= 0);

						objectOut.writeFloat(bid);
						objectOut.flush();
						input.nextLine();

						// Feedback regarding whether or not the bid was successful
						reply = objectIn.readUTF();
						System.out.println(reply);
					}

					// There are no items
					else
					{
						System.out.println("No item to bid on!\nYou will need to add one...");
					}
					break;
				}

				// Add new item for auction
				case 2:
				{
					// Client specifies the new items name and starting bid
					String itemName = null;
					float startingBid = 0;

					System.out.print("What would you like to auction off: ");
					itemName = input.nextLine();

					System.out.print("What is the starting bid: ");
					do
					{
						while (!input.hasNextFloat())
						{
							System.out.print("Please choose a valid number: ");
							input.next();
						}
						startingBid = input.nextFloat();
					} while(startingBid <= 0);

					// Send these to the server
					// The server creates the item and adds it to the auction
					objectOut.writeUTF(itemName);
					objectOut.writeFloat(startingBid);
					objectOut.flush();
					input.nextLine();

					break;
				}

				// Display the list of auction items
				case 3:
				{
					auction = null;

					try {
						auction = (Auction) objectIn.readObject();
					} catch(Exception e) {
						System.out.println(e);
					}

					if (auction != null)
					{
						auction.listAuctionItems();
					}

					break;
				}

				// Leave auction
				case 4:
				{
					// Gracefully terminate the application
					reply = objectIn.readUTF();
					System.out.println(reply);
					exit = true;
					break;
				}

				// This ensures the client enters a value between 1-4 inclusive
				default:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
				}
			}

			// For spacing the UI
			System.out.println();
		} while(!exit);


		s.close();

		try {
            // closing resources 
            objectIn.close(); 
            objectOut.close();      
        } catch(IOException e) { 
            e.printStackTrace(); 
        }
	}
}
