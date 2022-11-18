import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String args[]) throws Exception
    {
        String var="bye";
        var=var.intern();
        try
        {
            DatagramSocket serverSocket = new DatagramSocket(8080);

            byte[] receiveData = new byte[1024];
            byte[] sendData1  = new byte[1024];
            byte[] sendData2  = new byte[1024];
            while(true) //while(true) permet au serveur de rester en ecoute
            {

                receiveData = new byte[1024];

                DatagramPacket receivePacket =
                        new DatagramPacket(receiveData, receiveData.length);

                System.out.println ("Waiting for datagram packet");

                serverSocket.receive(receivePacket);

                String sentence = new String(receivePacket.getData());
                sentence=sentence.intern();


                InetAddress IPAddress = receivePacket.getAddress();

                int port = receivePacket.getPort();

                System.out.println ("From: " + IPAddress + ":" + port);
                System.out.println ("Message: " + sentence+var);
                if(sentence==var)
                {String capitalizedSentence = sentence.toUpperCase();

                    sendData1 = capitalizedSentence.getBytes();

                    DatagramPacket sendPacket1 = new DatagramPacket(sendData1, sendData2.length, IPAddress, port);

                    serverSocket.send(sendPacket1);
                    //serverSocket.close();
                }

                else if(sentence!=var)
                {String phrase="donnez le mot bye";

                    sendData2 = phrase.getBytes();

                    DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, IPAddress, port);

                    serverSocket.send(sendPacket2);
                    serverSocket.close();
                }

            }

        }
        catch (SocketException ex) {
            System.out.println("UDP Port 8080 is occupied.");
            System.exit(1);
        }

    }
//serverSocket.close();
}
