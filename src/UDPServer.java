import java.io.*;
import java.net.*;
import java.util.Arrays;

public class UDPServer {
    public static void main(String args[]) throws Exception
    {
        try
        {
            DatagramSocket serverSocket = new DatagramSocket(8080);

            byte[] receiveData = new byte[1024];
            byte[] sendData1  = new byte[1024];
            byte[] sendData2  = new byte[1024];
            while(true)     // permet au serveur de rester en écoute
            {

                receiveData = new byte[1024];

                DatagramPacket receivePacket =
                        new DatagramPacket(receiveData, receiveData.length);

                System.out.println ("Waiting for datagram packet");

                serverSocket.receive(receivePacket);

                receivePacket.getData();
                int l = receivePacket.getLength();
                byte[] cutData = Arrays.copyOf(receiveData, l);
                String sentence = new String(cutData);

                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();

                System.out.println ("From: " + IPAddress + ":" + port);
                System.out.println ("Message: " + sentence);

                sendData1 = sentence.getBytes();

                DatagramPacket sendPacket1 = new DatagramPacket(sendData1, l, IPAddress, port);
                serverSocket.send(sendPacket1);
                //serverSocket.close();
            }
        }
        catch (SocketException ex) {
            System.out.println("UDP Port 8080 is occupied.");
            System.exit(1);
        }
    }
//serverSocket.close();
}
