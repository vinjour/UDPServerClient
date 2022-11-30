import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UDPClient {
    static int BUFFER_SIZE = 1024;
    static int serverPort;
    static InetAddress serverAddress;
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
        sendData = new byte[BUFFER_SIZE];
        receiveData = new byte[BUFFER_SIZE];
        receivePacket =  new DatagramPacket(receiveData, receiveData.length);
    }

    public void run(InetAddress serverAddress) throws IOException {

        boolean keepListening = true;

        while(keepListening) {

            sendMessage(serverAddress);

            // delai maximal d'attente 10000 ms
            //clientSocket.setSoTimeout(10000);

            receiveMessage(serverAddress, receivePacket);
        }
        clientSocket.close();
    }

    private void sendMessage(InetAddress serverAddress) throws IOException {
        System.out.print("Enter Message: ");

        String sentence = inFromUser.readLine();
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

        clientSocket.send(sendPacket);
    }

    private void receiveMessage(InetAddress serverAddress, DatagramPacket receivePacket) throws IOException {

        System.out.println ("Waiting for return packet");

        clientSocket.receive(receivePacket);
        int packetLength = receivePacket.getLength();
        System.out.print(packetLength);
        String echoSentence = new String(receiveData, 0, packetLength, StandardCharsets.UTF_8);

        System.out.println("From server at: " + serverAddress + ":" + serverPort);
        System.out.println("Message: " + echoSentence + "\n");
    }

    public static void main(String args[]) throws Exception {

        serverAddress = InetAddress.getByName(args[0]);
        UDPClient.serverPort = Integer.parseInt(args[1]);

        UDPClient udpClient = new UDPClient();
        udpClient.run(serverAddress);
    }
}

