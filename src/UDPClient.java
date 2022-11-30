import java.io.*;
import java.net.*;
import java.util.Arrays;

public class UDPClient {
    static int BUFFER_SIZE = 1024;
    static int serverPort;
    DatagramSocket clientSocket;
    DatagramPacket receivePacket;
    BufferedReader inFromUser;
    byte[] sendData;
    byte[] receiveData;

    public UDPClient() throws IOException {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            System.err.println(ex);
            System.exit(1);
        }

        //On récupere la ligne courante
        inFromUser = new BufferedReader(new InputStreamReader(System.in));


        byte[] sendData = new byte[BUFFER_SIZE];
        receiveData = new byte[BUFFER_SIZE];

    }

    public void run(InetAddress serverAddress) throws IOException {

        while(true) {
            System.out.print("Enter Message: ");

            //On récupère le message
            String sentence = inFromUser.readLine();
            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

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

                System.out.println("From server at: " + serverAddress + ":" + serverAddress);
                System.out.println("Message: " + echoSentence);
            } catch (SocketTimeoutException ste) {
                System.out.println("Timeout Occurred: Packet assumed lost");
            }
        }
        // clientSocket.close();
    }

    public static void main(String args[]) throws Exception {

        InetAddress serverAddress = InetAddress.getByName(args[0]);
        UDPClient.serverPort = Integer.parseInt(args[1]);

        UDPClient udpClient = new UDPClient();
        udpClient.run(serverAddress);
    }
}

