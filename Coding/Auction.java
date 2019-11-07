// import java.net.*;
import java.io.*;
import java.util.*;

public class Auction implements Serializable
{
	ArrayList<Item> Items = new ArrayList<>();
	ArrayList<Client> Clients = new ArrayList<>();

	public Auction()
	{

	}

	// This should be in the server class
	public void displayMenu()
	{
		System.out.println("test");
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