// import java.net.*;
import java.io.*;
import java.util.*;

public class Item implements Serializable
{
	private float currentBid;
	private Client highestBidder;
	private String itemName;

	public Item(String itemName, float currentBid)
	{
		this.currentBid = currentBid;
		this.itemName = itemName;
		this.highestBidder = null;
	}

	public float getCurrentBid()
	{
		return currentBid;
	}

	public void setCurrentBid(float currentBid)
	{
		this.currentBid = currentBid;
	}

	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
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