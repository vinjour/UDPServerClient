import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UDPServer {

    static int BUFFER_SIZE = 1024;
    static int serverPort;
    DatagramSocket serverSocket;
    DatagramPacket receivePacket;
    DataProcessing data;
    byte[] receiveData;

    public UDPServer(int serverPort, DataProcessing datap) {
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException ex) {
            System.out.println("UDP Port " + serverPort + " is occupied");
            System.exit(1);
        }

        receiveData = new byte[BUFFER_SIZE];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        data = datap;
    }

    public void launch() throws IOException {
        boolean listening = true;

        while (listening) {

            receiveMessage();
            String sentence = data.process(receivePacket);
            sendMessage(sentence);
        }
        serverSocket.close();
    }

    private DatagramPacket receiveMessage() throws IOException {

        System.out.println("Waiting for datagram packet");

        serverSocket.receive(receivePacket);
        return receivePacket;
    }


    private void sendMessage(String sentence) throws IOException {
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();

        DatagramPacket sendPacket = new DatagramPacket(sentence.getBytes(StandardCharsets.UTF_8), sentence.length(), clientAddress, clientPort);
        serverSocket.send(sendPacket);
    }

    public static void main(String args[]) throws Exception {

        if (args.length != 1) {
            System.err.println("Usage: java UDPServer <port>");
            System.exit(1);
        }

        DataProcessing data = new DataProcessing();
        serverPort = Integer.parseInt(args[0]);

        UDPServer udpServer = new UDPServer(serverPort, data);
        udpServer.launch();
    }
}

