import java.net.*;
import java.io.*;
import java.util.*;

public class Client
{
	public static void main(String[] args) throws IOException
	{
		Auction auction = null;
		Socket s = new Socket("localhost", 4999);
		ArrayList<String> menu = new ArrayList<String>();

		InputStreamReader in = new InputStreamReader(s.getInputStream());
		BufferedReader bf = new BufferedReader(in);
		Scanner scan = new Scanner(System.in);

		while(true)
		{
			String messageFromServer = bf.readLine();
			System.out.println(messageFromServer + "\n");

			// Display Menu
			for (int i = 0; i < 5; i++)
			{
				String menuItem = bf.readLine();
				menu.add(menuItem);
			}

			// String str = bf.readLine();
			// System.out.println("server : " + str);

			displayMenu(menu);

			// try
			// {
			// 	auction = (Auction) objectIn.readObject();
			// }
			// catch(Exception e)
			// {
			// 	System.out.println(e);
			// }

			// if (auction != null)
			// {
			// 	System.out.println(":)");
			// }

			// // Display menu
			// Arraylist<String> menu = new Arraylist<String>();
			
			int i = scan.nextInt();
		}
		// s.close();
	}

	public static void displayMenu(ArrayList<String> menu)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			System.out.println(menu.get(i));
		}
	}
}