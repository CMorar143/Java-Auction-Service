import java.net.*;
import java.io.*;
import java.util.*;

public class Client
{
	public static void main(String[] args) throws IOException
	{
		Auction auction = null;
		Socket s = new Socket("localhost", 4999);
		ArrayList<String> menu = null;

		// InputStreamReader in = new InputStreamReader(s.getInputStream());
		// BufferedReader bf = new BufferedReader(in);
		Scanner scan = new Scanner(System.in);
		ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());

		// while(true)
		{
			// String messageFromServer = bf.readLine();
			// if (bf.readLine() != null)
			// 	System.out.println(messageFromServer + "\n");

			// // Display Menu
			// for (int i = 0; i < 5; i++)
			// {
			// 	// if (bf.readLine() != null)
			// 	{
			// 		String menuItem = bf.readLine();
			// 		menu.add(menuItem);
			// 	}
			// }

			// String str = bf.readLine();
			// System.out.println("server : " + str);

			// displayMenu(menu);

			try
			{
				Object object = objectIn.readObject();
                menu = (ArrayList<String>)object;
			}
			catch(Exception e)
			{
				System.out.println(e);
			}

			System.out.println(menu);

			// if (auction != null)
			// {
			// 	System.out.println(":)");
			// }

			// // Display menu
			// Arraylist<String> menu = new Arraylist<String>();

			// int i = scan.nextInt();
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