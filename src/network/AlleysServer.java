package network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class AlleysServer
{
	static private int _PortNumber;
	
	public static void main(String[] args) throws Exception
    {
		if (args.length < 1)
		{
			_PortNumber = 4000;
			System.out.println("No port specified, using default port of: " + _PortNumber);
		}
		else
		{
		    try
		    {
		    	_PortNumber = Integer.parseInt(args[0]);
		    }
		    catch (NumberFormatException e)
		    {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }
		}
		
        ServerSocket listener = new ServerSocket(_PortNumber);
        
        try
        {
            while (true)
            {
            	String line;
            	
                Socket socket = listener.accept();
                BufferedReader readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                
                try
                {
                    writerChannel.write(new Date().toString() + "\n\r");
                    writerChannel.flush();

                    while ((line = readerChannel.readLine()) != null)
                    {
                        System.out.println(line);
                    }
                }
                finally
                {
                    socket.close();
                }
            }
        }
        finally
        {
            listener.close();
        }
    }
}

