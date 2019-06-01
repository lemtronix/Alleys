package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Date;

public class AlleysClient {

	private static int _PortNumber;
	private static String _IpAddress;
	
	public static void main(String[] args) throws Exception
	{
		if (args.length < 1)
		{
			System.err.println("You must specify an IP address to connect on.");
			System.exit(1);
		}
		else if (args.length < 2)
		{
			System.err.println("You must specify a port to connect to.");
			System.exit(1);
		}
		else
		{
		    try
		    {
		    	_IpAddress = args[0];
		    	_PortNumber = Integer.parseInt(args[1]);
		    }
		    catch (NumberFormatException e)
		    {
		        System.err.println("Argument" + args[1] + " must be an integer.");
		        System.exit(1);
		    }
		}
		
		Socket socket = null;
		
		try
		{
			socket = new Socket(_IpAddress, _PortNumber);
			BufferedWriter writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			BufferedReader readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line;
	
			writerChannel.write(new Date().toString() + "\n\r");
			writerChannel.flush();
	
			while ((line = readerChannel.readLine()) != null)
			{
				System.out.println(line);
			}
		}
		catch(ConnectException e)
		{
			System.out.println("Server refused the connection request.");
		}
		finally
		{
			if (socket != null)
			{
				socket.close();
			}
		}
	}
}
