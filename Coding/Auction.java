/**
 * This class contains all the basic functionality and variables
 * pertaining to the auction, very often it is used by the server 
 * and is accessed by the client via the client handler class.
 * 
 * 
 * @author Cian Morar (C16460726) 
 * @date 21st November 2019
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Auction implements Serializable
{
	private ArrayList<Item> Items = new ArrayList<Item>();
	private ArrayList<Client> Clients = new ArrayList<Client>();
	private ArrayList<String> menu = new ArrayList<String>();

	// Constructor
	public Auction()
	{
		menu.add("==> Please choose what you would like to do");
		menu.add("1. Bid On The Item");
		menu.add("2. Create A New Auction");
		menu.add("3. List Auction Items");
		menu.add("4. Leave Auction");

		// Create 5 default auction items
		Item item1 = new Item("Antique Chair", 45);
		Items.add(item1);

		Item item2 = new Item("Couch", 35);
		Items.add(item2);

		Item item3 = new Item("Antique Door", 30);
		Items.add(item3);

		Item item4 = new Item("Pots", 15);
		Items.add(item4);

		// Item item5 = new Item("Bobble Head Collection", 60);
		// Items.add(item5);
	}

	// Return the menu for the server to send to the client
	public ArrayList<String> displayMenu()
	{
		return menu;
	}

	// Add the client to the list of clients
	public void addClient(Client c)
	{
		Clients.add(c);
	}

	// Remove the client to the list of clients
	// This will allow others to use that username if the original client has left
	public void removeClient(Client c)
	{
		Clients.remove(c);
	}

	// This used to enforce a unqiue name policy within the system
	// It loops through, checking if the username already exists and returns 
	// a boolean to tell the server whether or not it does already exist
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

	// Checks wwhether or not the new bid is higher than the original
	// and returns a boolean to inform the server whether the action was successful
	// Note: Items.get(0) will always be the one currently on auction
	public boolean placeBid(float bid, Client c)
	{
		// Update the current bid and current client for the item that was passed as the parameter
		if (areThereItems())
		{
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
		} else {
			return false;
		}
	}

	// Used to validate whether the client successfully added a new item
	public void listAuctionItems()
	{
		if (areThereItems())
		{
			System.out.println("=> The items currently on auction are:");
			for (int i = 0; i < Items.size(); i++)
			{
				System.out.print(String.valueOf(i+1) + ". ");
				System.out.println("Item Name: " + Items.get(i).getItemName());
				System.out.println("   Current Bid: " + Items.get(i).getCurrentBid() + "\n");
			}
		}

		else
		{
			System.out.println("=> There are no items left in the auction to display!");
		}
	}

	// Announces the winner
	// Removes the current item so that the next one is now on auction
	public void announceWinner()
	{
		if(areThereItems())
		{
			Item item = auctionItem();
			if (item.getHighestBidder() != null)
			{
				System.out.println("=> The " + item.getItemName() + " has been sold for " + item.getCurrentBid() + " euro!");
				System.out.println("=> And the winner is: " + item.getHighestBidder().getUsername());
			}

			else
			{
				System.out.println("=> Item has been scrapped as nobody bid on it");
			}

			Items.remove(item);
		}
	}

	// Used by the server to add the new item provided by the client
	public void addItem(String name, float currentBid) 
	{
		Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}

	// This returns the  item that is currently up for auction
	public Item auctionItem()
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

	// Checksto see if the list is empty
	// i.e. if all the items have already been auctioned off
	public boolean areThereItems()
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