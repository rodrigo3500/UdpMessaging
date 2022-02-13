package Assignment2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UdpMessenger
{
    private byte[] data;
    private DatagramPacket packet;
    private final int MAX_REQUEST_BYTES;

    public UdpMessenger(int maxRequestBytes){
        MAX_REQUEST_BYTES = maxRequestBytes;
    }

    /**
     * Prints packet length, data and byte representation
     */
    public void printPacketData(DatagramPacket packet){
        int len = packet.getLength();
        String contents = new String(packet.getData(), StandardCharsets.UTF_8);
        System.out.println("Length: " + len);
        System.out.println("Data: \""+ contents + "\"");
        System.out.println("Byte Representation: "+ Arrays.toString(packet.getData()) + "\n");
    }

    /**
     * Send a message to a specified port on the local host IP
     * @param port Int port number for destination socket
     * @param message String message to send
     */
    protected void send(int port, String message, DatagramSocket socket)
    {// Prepare a DatagramPacket and send it via sendReceiveSocket
        data = message.getBytes();
        try {
            packet = new DatagramPacket(data,data.length, InetAddress.getLocalHost(), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Send message via sendReceive sendSocket
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        // Prints out the information it has received (print the request both as a String and bytes)
        System.out.println("Packet Sent ==>");
        System.out.println("To Address: "+ packet.getSocketAddress());
        System.out.println("Using Port: "+ packet.getPort());
        printPacketData(packet);
    }

    /**
     *  Receive a UDP packet and prints the contents
     * @return DatagramPacket received
     */
    protected DatagramPacket receive(DatagramSocket socket){
        data = new byte[MAX_REQUEST_BYTES];
        packet = new DatagramPacket(data, data.length);
        try { // Block until a datagram is received via socket.
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Packet Received <==");
        System.out.println("From Address: "+ packet.getSocketAddress());
        System.out.println("Using Port: "+ packet.getPort());
        printPacketData(packet);
        return packet;
    }
}
