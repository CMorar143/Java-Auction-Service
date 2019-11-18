import java.net.*;
import java.io.*;
import java.util.*;

public class Client
{
	private String username;
	private String password;

	public static void displayMenu(ArrayList<String> menu)
	{
		for (int i = 0; i < menu.size(); i++)
		{
			System.out.println(menu.get(i));
		}
	}

	public Client(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUsername()
	{
		return this.username;
	}

	public String getPassword()
	{
		return this.password;
	}

	public static void main(String[] args) throws IOException
	{
		Auction auction = null;
		Socket s = new Socket("localhost", 4999);
		ArrayList<String> menu = null;

		Scanner input = new Scanner(System.in);
		ObjectOutputStream objectOut = new ObjectOutputStream(s.getOutputStream());
		ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());

		String reply = null;
		reply = objectIn.readUTF();
		System.out.println(reply);

		String username, password;
		System.out.println("Username: ");
		username = input.nextLine();
		objectOut.writeUTF(username);
		
		System.out.println("\nPassword: ");
		password = input.nextLine();
		objectOut.writeUTF(password);
		objectOut.flush();

		reply = objectIn.readUTF();
		System.out.println(reply);

		try
		{
			Object object = objectIn.readObject();
			auction = (Auction)object;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		auction.listAuctionItems();
		System.out.println("\n");

		try
		{
			Object object = objectIn.readObject();
			menu = (ArrayList<String>)object;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		// DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		int i = 0;

		do
		{
			// System.out.println(menu);
			displayMenu(menu);

			i = input.nextInt();
			objectOut.writeInt(i);
			objectOut.flush();
			input.nextLine();
			// Get input back
			switch (i)
			{
				// Make a bid
				case 1:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);

					auction.listAuctionItems();
					int itemNum = input.nextInt();
					objectOut.writeInt(itemNum);
					objectOut.flush();
					input.nextLine();

					break;
				}

				// Add new item for auction
				case 2:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
					break;
				}

				// Leave auction
				case 3:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
					break;
				}

				default:
				{
					reply = objectIn.readUTF();
					System.out.println(reply);
				}
			}

			// dos.flush();
			// bw.flush();
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

			// if (auction != null)
			// {
			// 	System.out.println(":)");
			// }
		}while(i < 6);
		s.close();
	}
}