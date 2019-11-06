// import java.net.*;
import java.io.*;
import java.util*;

public class Item
{
	private float currentBid;
	private Client highestBidder = null;

	public Item(float currentBid)
	{
		this.currentBid = currentBid;
	}

	public float getCurrentBid()
	{
		return currentBid;
	}

	public void setCurrentBid(float currentBid)
	{
		this.currentBid = currentBid;
	}

	public Client getHighestBidder()
	{
		return highestBidder;
	}

	public void setHighestBidder(Client highestBidder)
	{
		this.highestBidder = highestBidder;
	}
}