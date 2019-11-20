// import java.net.*;
import java.io.*;
import java.util.*;

public class Auction implements Serializable
{
	private ArrayList<Item> Items = new ArrayList<Item>();
	private ArrayList<Client> Clients = new ArrayList<Client>();
	private ArrayList<String> menu = new ArrayList<String>();
	Timer timer = new Timer();
	Timer timeLeft = new Timer();
	TimerTask task = new TimerTask() {
		public void run()
		{
			// Announce winner of auction and move onto next item
			System.out.println("test");
		}
	};

	TimerTask getTime = new TimerTask()
	{
		int seconds = 6;
		int i = 0;
		
		@Override
		public void run()
		{
			i++;

			if(i % seconds == 0)
			{
				System.out.println("Timer action!");
				timeLeft.cancel();
			}

			else
			{
				System.out.println("Time left:" + (seconds - (i %seconds)) );
			}
		}
	};

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

	public synchronized boolean addClient(Client c)
	{
		// First we ensure the client doesn't already exist in the list
		int check = 0;
		for (int i = 0; i < Clients.size(); i++)
		{
			String user = Clients.get(i).getUsername();

			// If the client is already in the list
			if (user.equals(c.getUsername()) == true)
			{
				check = 1;
			}
		}

		if (check == 0)
		{
			Clients.add(c);
			return true;
		}

		else
		{
			return false;
		}
		// Add client username and password to file (?)
	}

	public synchronized void removeClient(Client c)
	{
		Clients.remove(c);

		// Remove client username and password from file (?)
	}

	public synchronized boolean placeBid(float bid, Client c)
	{
		// Update the current bid and current client for the item that was passed as the parameter
		float currentBid = Items.get(0).getCurrentBid();

		if (bid > currentBid)
		{
			Items.get(0).setCurrentBid(bid);
			Items.get(0).setHighestBidder(c);
			// Reset the timer

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
		Item item = auctionItem();
		if (item.getHighestBidder() != null)
		{
			System.out.println("winner declared here " + item.getHighestBidder().getUsername());
		}

		else
		{
			System.out.println("No winner\n");
		}
		Items.remove(item);
		// System.out.println("winner declared here");
	}

	public synchronized void addItem(String name, float currentBid) 
	{
		Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}

	public synchronized Item auctionItem()
	{
		return Items.get(0);
	}

	public synchronized void startTimer()
	{
		// timer.scheduleAtFixedRate(task, 0, 6000);
		// timer.schedule(task, 6000);
	}

	public synchronized void getTimeRemaining()
	{
		timeLeft.schedule(getTime, 0, 1000);
	}

	public synchronized void stopTimers()
	{
		timer.cancel();
		timeLeft.cancel();
	}
}