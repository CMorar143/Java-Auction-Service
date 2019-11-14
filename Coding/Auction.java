// import java.net.*;
import java.io.*;
import java.util.*;

public class Auction implements Serializable
{
	private ArrayList<Item> Items = new ArrayList<Item>();
	private ArrayList<Client> Clients = new ArrayList<Client>();
	private ArrayList<String> menu = new ArrayList<String>();

	public Auction()
	{
		menu.add("1. Join Auction");
		menu.add("2. Leave Auction");
		menu.add("3. Bid on Item");
		menu.add("4. List Auction Items");
		menu.add("5. Create a New Auction");

		// Create 5 default auction items
		Item item1 = new Item("Antique Chair", 45);
		Items.add(item1);

		Item item2 = new Item("Couch", 35);
		Items.add(item2);

		Item item3 = new Item("Antique Door", 30);
		Items.add(item3);

		Item item4 = new Item("Pots", 15);
		Items.add(item4);

		Item item5 = new Item("Bobble Head Collection", 60);
		Items.add(item5);
	}

	// This should be in the server class
	public ArrayList<String> displayMenu()
	{
		return menu;
	}

	public void addClient(Client c)
	{
		// First we ensure the client doesn't already exist in the list
		int check = 0;
		for (int i = 0; i < Clients.size(); i++)
		{
			String user = Clients.get(i).getUsername();

			// If the client is already in the list
			if (user.equals(c.getUsername()) == true)
			{
				check++;
			}
		}

		if (check == 0)
		{
			Clients.add(c);
			System.out.println(c.getUsername());
			System.out.println(c.getPassword());
		}
		// Add client username and password to file (?)
	}

	public void removeClient(Client c)
	{
		Clients.remove(c);

		// Remove client username and password from file (?)
	}

	public void placeBid(Item item, float bid, Client c)
	{
		// Update the current bid and current client for the item that was passed as the parameter
		item.setCurrentBid(bid);
		item.setHighestBidder(c);
	}

	public void listAuctionItems()
	{
		if (Items != null)
		{
			for (int i = 0; i < Items.size(); i++)
			{
				System.out.println("Item Name: " + Items.get(i).getItemName());
				System.out.println("Current Bid: " + Items.get(i).getCurrentBid() + "\n");
			}
		}
	}

	public void addItem(String name, float currentBid) 
	{
		Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}
}