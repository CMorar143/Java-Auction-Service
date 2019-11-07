// import java.net.*;
import java.io.*;
import java.util.*;

public class Auction
{
	ArrayList<Item> Items = new ArrayList<>();
	ArrayList<Client> Clients = new ArrayList<>();

	// This should be in the server class
	public void displayMenu()
	{
		// 1 Join auction
		// 2 Leave auction
		// 3 Bid on an item.
		// 4 List auction items
		// 5 Create new auctions
	}

	public void addClient(Client c)
	{
		Clients.add(c);

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
		
	}

	public void addItem(String name, float currentBid) 
	{
		Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}
}