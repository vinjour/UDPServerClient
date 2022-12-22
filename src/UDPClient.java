import java.io.*;
import java.net.*;


public class UDPClient {
    static int BUFFER_SIZE = 1024;
    static int TIMER_SIZE = 10000;  // Milliseconds
    static int serverPort;
    static InetAddress serverAddress;
    DatagramSocket clientSocket;
    DatagramPacket receivePacket;
    BufferedReader readMsgToSend;
    DataProcessing data;
    byte[] sendData;
    byte[] receiveData;

    public UDPClient(DataProcessing datap) {
        try {
            clientSocket = new DatagramSocket();
        } catch (SocketException ex) {
            System.err.println(ex);
            System.exit(1);
        }

        readMsgToSend = new BufferedReader(new InputStreamReader(System.in));
        sendData = new byte[BUFFER_SIZE];
        receiveData = new byte[BUFFER_SIZE];
        receivePacket =  new DatagramPacket(receiveData, receiveData.length);
        data = datap;
    }

    public void run(InetAddress serverAddress) throws IOException {

        boolean connected = true;

        while(connected) {

            sendMessage(serverAddress);
            clientSocket.setSoTimeout(TIMER_SIZE);
            receiveMessage(serverAddress, receivePacket);
            data.process(receivePacket);
        }
        clientSocket.close();
    }

    private void sendMessage(InetAddress serverAddress) throws IOException {
        System.out.print("Enter Message: ");

        String sentence = readMsgToSend.readLine();
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
        clientSocket.send(sendPacket);
    }

    private void receiveMessage(InetAddress serverAddress, DatagramPacket receivePacket) throws IOException {

        System.out.println ("Waiting for return packet");
        clientSocket.receive(receivePacket);
        System.out.println("From server at: " + serverAddress + ":" + serverPort);
    }

    public static void main(String args[]) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: java UDPClient <IPaddress> <port>");
            System.exit(1);
        }

        DataProcessing data = new DataProcessing();
        serverAddress = InetAddress.getByName(args[0]);
        UDPClient.serverPort = Integer.parseInt(args[1]);

        UDPClient udpClient = new UDPClient(data);
        udpClient.run(serverAddress);
    }
}

