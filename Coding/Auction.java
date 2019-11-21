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
		menu.add("Please choose what you would like to do");
		menu.add("1. Bid On The Item");
		menu.add("2. Create A New Auction");
		menu.add("3. Leave Auction");

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
	public synchronized ArrayList<String> displayMenu()
	{
		return menu;
	}

	public synchronized void addClient(Client c)
	{
		Clients.add(c);
	}

	public boolean doesClientExist(String name)
	{
		boolean clientExists = false;
		for (int i = 0; i < Clients.size(); i++)
		{
			String user = Clients.get(i).getUsername();

			// If the client is already in the list
			if (user.equals(name) == true)
			{
				clientExists = true;
			}
		}

		return clientExists;
	}

	public synchronized void removeClient(Client c)
	{
		Clients.remove(c);
	}

	public synchronized boolean placeBid(float bid, Client c)
	{
		// Update the current bid and current client for the item that was passed as the parameter
		float currentBid = Items.get(0).getCurrentBid();

		if (bid > currentBid)
		{
			Items.get(0).setCurrentBid(bid);
			Items.get(0).setHighestBidder(c);
			return true;
		}

		else
		{
			return false;
		}
	}

	public synchronized void listAuctionItems()
	{
		if (Items != null)
		{
			for (int i = 0; i < Items.size(); i++)
			{
				System.out.print(String.valueOf(i+1) + ". ");
				System.out.println("Item Name: " + Items.get(i).getItemName());
				System.out.println("   Current Bid: " + Items.get(i).getCurrentBid() + "\n");
			}
		}
	}

	public synchronized void AnnounceWinner()
	{
		if(areThereItems())
		{
			Item item = auctionItem();
			if (item.getHighestBidder() != null)
			{
				System.out.println("The " + item.getItemName() + " has been sold for " + item.getCurrentBid() + " euro!");
				System.out.println("And the winner is: " + item.getHighestBidder().getUsername());
			}

			else
			{
				System.out.println("Item has been scrapped as nobody bid on it");
			}

			Items.remove(item);
		}
	}

	public synchronized void addItem(String name, float currentBid) 
	{
		Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}

	public synchronized Item auctionItem()
	{
		if (areThereItems())
		{
			return Items.get(0);
		}

		else
		{
			return null;
		}
	}

	public synchronized boolean areThereItems()
	{
		if (Items.size() == 0)
		{
			return false;
		}

		else
		{
			return true;
		}
	}
}