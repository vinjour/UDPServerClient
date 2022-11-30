import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    static int BUFFER_SIZE = 1024;
    static int DEFAULT_PORT = 8080;
    DatagramSocket serverSocket;
    DatagramPacket receivePacket;
    byte[] receiveData;
    byte[] sendData;

    public UDPServer() {
        try {
            serverSocket = new DatagramSocket(DEFAULT_PORT);
        } catch (SocketException ex) {
            System.out.println("UDP Port " + DEFAULT_PORT + " is occupied");
            System.exit(1);
        }

        receiveData = new byte[BUFFER_SIZE];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
    }

    public void launch() throws IOException {
        boolean keepListening = true;

        while (keepListening) {

            String sentence = receiveMessage();
            sendMessage(sentence);
        }
        serverSocket.close();
    }

    private String receiveMessage() throws IOException {

        System.out.println("Waiting for datagram packet");

        serverSocket.receive(receivePacket);
        int packetLength = receivePacket.getLength();
        String sentence = new String(receiveData, 0, packetLength, StandardCharsets.UTF_8);
        return sentence;
    }

    private void sendMessage(String sentence) throws IOException {
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        System.out.println("From: " + clientAddress + ":" + clientPort);
        System.out.println("Message: " + sentence);

        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sentence.length(), clientAddress, clientPort);
        serverSocket.send(sendPacket);
    }

    public static void main(String[] args) throws Exception {

        UDPServer udpServer = new UDPServer();
        udpServer.launch();
    }
}
