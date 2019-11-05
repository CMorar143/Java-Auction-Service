import java.net.*;
import java.io.*;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		Socket ss = new ServerSocket(4999);
		Socket s = ss.accept();

		System.out.println("client connected");
	}
}