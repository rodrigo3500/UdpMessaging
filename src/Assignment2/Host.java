package Assignment2;

import java.net.*;

public class Host extends UdpMessenger {
    private DatagramSocket sendReceiveSocket, receiveSocket;

    public Host(int receivePort, int maxRequestBytes) {
        super(maxRequestBytes);
        System.out.println("Host running ...");
        try {
            // Binds a socket to given port on the local host machine.
            // This socket will be used to receive UDP Datagram packets.
            this.receiveSocket = new DatagramSocket(receivePort);
//            receiveSocket.setSoTimeout(5000);
            System.out.println("Receive Socket initiated on port: " + receiveSocket.getPort());

            // Binds a socket to any available port on the local host machine.
            // This socket will be used to send and receive UDP Datagram packets.
            this.sendReceiveSocket = new DatagramSocket();
//            sendReceiveSocket.setSoTimeout(5000);
            System.out.println("SendReceive Socket initiated on port: " + sendReceiveSocket.getPort() + "\n");


        } catch (SocketException se) {
            closeSocket();
            se.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * Closes sendReceiveSocket and receiveSocket
     */
    private void closeSocket(){
        sendReceiveSocket.close();
        receiveSocket.close();
    }

    public static void main(String[] args) {
        // Set receive ports and max data packet size
        int serverReceivePort = 6969;
        int hostReceivePort = 2323;
        final int MAX_REQUEST_BYTES = 17;

        Host h = new Host(hostReceivePort, MAX_REQUEST_BYTES);

        while(true){
            // The host waits to receive a request and prints the information
            DatagramPacket clientPacket = h.receive(h.receiveSocket);
            // The host forms a packet to send containing exactly what it received and prints it
            h.send(serverReceivePort,new String(clientPacket.getData()), h.sendReceiveSocket);  // Send to port 69

            // Receive from server and prints data, then create packet to send to client
            DatagramPacket hostPacket = h.receive(h.sendReceiveSocket);
            h.send(clientPacket.getPort(), new String(hostPacket.getData()), h.sendReceiveSocket);
        }
    }
}