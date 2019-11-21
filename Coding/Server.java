import java.net.*;
import java.io.*;
import java.util.*;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = new ServerSocket(4999);
		final Auction auction = new Auction();
		final ArrayList<String> menu = auction.displayMenu();
		Socket s = null;

		while(true)
		{
			s = ss.accept();

			String reply = null;
			System.out.println("client accepted");
			
			ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());

			ClientHandler handler = new ClientHandler(s, auction, objectIn, objectOut);

			Thread t = new Thread(handler);

			t.start();
		}
	}
}




// Create the same timer on the server side
// When the timer elapses invoke a method
// The method call a method on the client side (passing the item reference)
// print the highest bidder and bid on the client side
// remove that item from the auction
// restart timers with the new auction


// Make another timer
// every 2 seconds this new timer will check if the other main one is finished
// if it is
	// display winner
// else
	// dont do anything