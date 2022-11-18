import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        try {
            String serverHostname= args[0];

            //On récupere la ligne courante
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            //On déclare une socket
            DatagramSocket clientSocket = new DatagramSocket();

            //on recupere l'adresse sur laquelle il faut envoyer le message (adresse local)
            InetAddress IPAddress = InetAddress.getByName(serverHostname);
            System.out.println ("Attemping to connect to " + IPAddress + ") via UDP port 8080");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            System.out.print("Enter Message: ");

            //On recupere le message
            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();
            System.out.println ("Sending data to " + sendData.length + " bytes to server.");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 8080);
            //On envois le message
            clientSocket.send(sendPacket);

            //On prépare la reception du 1er  message
            DatagramPacket receivePacket =  new DatagramPacket(receiveData, receiveData.length);


            System.out.println ("Waiting for return packet");
            //setSoTimeout(10000) le delai maximal d'attente 10000 mls
            clientSocket.setSoTimeout(10000);

            try {
                clientSocket.receive(receivePacket);
                String modifiedSentence =  new String(receivePacket.getData());
                InetAddress returnIPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                System.out.println ("From server at: " + returnIPAddress + ":" + port);
                System.out.println("Message: " + modifiedSentence);
            }


            catch(SocketTimeoutException ste)
            {
                System.out.println ("Timeout Occurred: Packet assumed lost");
            }

            //clientSocket.close();
        }

        catch (UnknownHostException ex)
        {
            System.err.println(ex);
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }
}

