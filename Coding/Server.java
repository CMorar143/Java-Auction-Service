/**
 * The server is where all the clients connect in order
 * to gain access to the auction and it’s items.
 * In order to handle multiple clients in concurrency, the Server
 * class consists of a while loop in which the main purpose is to
 * listen and accept all incoming client requests.
 * Once the client is accepted it will create a ClientHandler thread for each one.
 * This enables the server to be able to handle requests from multiple clients at once.
 * 
 * 
 * @author Cian Morar (C16460726) 
 * @date 21st November 2019
 * 
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class Server
{
	private static final int PORT = 1235;
	private static ServerSocket ss;

	public static void main(String[] args) throws IOException
	{
		try
		{
			ss = new ServerSocket(1235);
		}
		catch (IOException ioEx)
		{
			System.out.println("\nUnable to set up port!");
			System.exit(1);
		}
		final Auction auction = new Auction();
		final ArrayList<String> menu = auction.displayMenu();
		Socket s = null;

		// Loop forever
		// Accept client requests and create a client handler thread
		// for each one. This allows the server to handle mutliple
		// clients in concurrency
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