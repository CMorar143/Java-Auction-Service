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
	}

	// This should be in the server class
	public ArrayList<String> displayMenu()
	{
		return menu;
	}

	public void addClient(Client c)
	{
		Clients.add(c);
		System.out.println(c.getUsername());
		System.out.println(c.getPassword());

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
				System.out.println(Items.get(i));
			}
		}
	}

	public void addItem(String name, float currentBid) 
	{
		Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}
}