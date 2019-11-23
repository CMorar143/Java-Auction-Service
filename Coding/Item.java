/**
 * Contains all the basic fields useful for dealing with
 * an item object. It contains fields for the current bid,
 * the current client who placed that bid and the name of the item.
 * The class also comes with the necessary getter and setter methods for these fields.
 * 
 * 
 * @author Cian Morar (C16460726) 
 * @date 21st November 2019
 * 
 */

import java.io.Serializable;

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