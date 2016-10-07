package codebar.compiler;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServerSocket
{
	public static void main(String args[]) throws IOException
	{
		try
		{
			int n=0;
			int port_number=3029;
			ServerSocket socket=new ServerSocket(port_number);
			System.out.println("CodeBar compilation server is runnning...");
			while(true)
			{
				n++;
				Socket s=socket.accept();
				ClientThread client=new ClientThread(s,n);
				client.start();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
