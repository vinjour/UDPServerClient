import java.net.*;
import java.nio.charset.StandardCharsets;


public class DataProcessing {

    static int BUFFER_SIZE = 1024;
    DatagramPacket receivePacket;
    byte[] receiveData;

    public DataProcessing() {
        receiveData = new byte[BUFFER_SIZE];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
    }

    public String process(DatagramPacket receivePacket){
        String echoSentence = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
        System.out.println("Message: " + echoSentence + "\n" );

        return echoSentence;
    }

    public static void main(String[] args) {

        DataProcessing dataprocessing = new DataProcessing();
        dataprocessing.process(dataprocessing.receivePacket);
    }
}
