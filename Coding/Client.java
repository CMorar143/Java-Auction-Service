import java.net.*;
import java.io.*;
import java.util.*;

public class Client
{
	public static void main(String[] args) throws IOException
	{
		Auction auction = null;
		Socket s = new Socket("localhost", 4999);

		// PrintWriter pr = new PrintWriter(s.getOutputStream());
		// pr.println("hello from client");
		// pr.flush();

		ObjectInputStream objectIn = new ObjectInputStream(s.getInputStream());
		// InputStreamReader in = new InputStreamReader(s.getInputStream());
		// BufferedReader bf = new BufferedReader(in);

		// String str = bf.readLine();
		// System.out.println("server : " + str);

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

		s.close();
	}
}