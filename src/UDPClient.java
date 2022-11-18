import java.io.*;
import java.net.*;
import java.util.Arrays;

public class UDPClient {
    public static void main(String args[]) throws Exception
    {
        try {
            InetAddress IPaddress = InetAddress.getByName(args[0]);
            int port = Integer.parseInt(args[1]);

            //On récupere la ligne courante
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            //On déclare une socket
            DatagramSocket clientSocket = new DatagramSocket();

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            while(true) {
                System.out.print("Enter Message: ");

                //On récupère le message
                String sentence = inFromUser.readLine();
                sendData = sentence.getBytes();
                System.out.println ("Sending " + sendData.length + " bytes to server.");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPaddress, port);

                //On envoi le message
                clientSocket.send(sendPacket);

                //On prépare la reception du 1er message
                DatagramPacket receivePacket =  new DatagramPacket(receiveData, receiveData.length);


                System.out.println ("Waiting for return packet");
                // delai maximal d'attente 10000 ms
                clientSocket.setSoTimeout(10000);


                try {
                    clientSocket.receive(receivePacket);

                    receivePacket.getData();
                    int l = receivePacket.getLength();
                    byte[] cutData = Arrays.copyOf(receiveData, l);
                    String echoSentence = new String(cutData);

                    System.out.println("From server at: " + IPaddress + ":" + port);
                    System.out.println("Message: " + echoSentence);
                }
                catch (SocketTimeoutException ste) {
                    System.out.println("Timeout Occurred: Packet assumed lost");
                }
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

