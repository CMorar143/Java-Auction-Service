// import java.net.*;
import java.io.*;
import java.util.*;

public class Auction
{
	// ArrayList<String>
	ArrayList<Items> Items = new ArrayList<>();

	public void addItem(String name, float currentBid) 
	{
    	Item newItem = new Item(name, currentBid);
		Items.add(newItem);
	}

	public void displayMenu()
	{
		
	}
}