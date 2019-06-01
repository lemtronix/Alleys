package network;

// A Java program for a Client 
import java.net.*;
import java.io.*;

public class AlleysClient
{
    // initialize socket and input output streams
    private Socket socket = null;
    private BufferedReader input = null;
    private PrintWriter out = null;

    // constructor to put IP address and port
    public AlleysClient(String address, int port)
    {
        // establish a connection
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal
            input = new BufferedReader(new InputStreamReader(System.in));

            // sends output to the socket
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (UnknownHostException u)
        {
            System.out.println(u);
        }
        catch (IOException i)
        {
            System.out.println(i);
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.println(line);
            }
            catch (IOException i)
            {
                System.out.println(i);
            }
        }

        // close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i)
        {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        AlleysClient client = new AlleysClient("127.0.0.1", 5000);
    }
}