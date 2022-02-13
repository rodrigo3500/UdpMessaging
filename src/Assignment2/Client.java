package Assignment2;

import java.net.*;

/**
 * This class is the client side for a simple echo server based on
 * UDP/IP. The client sends a character string to the host, then waits
 * for the host to send it back to the client.
 */
public class Client extends UdpMessenger {

    private DatagramSocket sendReceiveSocket;

    public Client(int maxRequestBytes) {
        super(maxRequestBytes);
        System.out.println("Client running ...");
        try {
            // Binds a sendReceiveSocket to any available port on the local host machine.
            // This sendReceiveSocket will be used to send and receive UDP Datagram packets.
            sendReceiveSocket = new DatagramSocket();
//            sendReceiveSocket.setSoTimeout(5000);
        } catch (SocketException se) {   // Can't create the sendReceiveSocket.
            closeSocket();
            se.printStackTrace();
            System.exit(1);
        }
        System.out.println("Client sendReceiveSocket initiated on port: " + sendReceiveSocket.getPort() + "\n");
    }

    /**
     * Closes socket
     */
    private void closeSocket(){
        sendReceiveSocket.close();
    }

    public static void main(String[] args){
        final int hostPort = 2323;
        final String mode = "octet";
        final String fileName = "fileName";
        final int MAX_REQUEST_BYTES = 17;

        Request request;

        Client c = new Client(MAX_REQUEST_BYTES);

        // Send 5 read and 5 write requests, alternating each time
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                request = new Request(fileName,true,mode, MAX_REQUEST_BYTES);
            } else {
                request = new Request(fileName,false,mode,MAX_REQUEST_BYTES);
            }
            String message = request.getRequestString();
            c.send(hostPort, message, c.sendReceiveSocket);
            c.receive(c.sendReceiveSocket);
        }

        // Send one last invalid request then close socket
        request = new Request();
        String invalidRequest = request.getRequestString();

        c.send(hostPort, invalidRequest, c.sendReceiveSocket);

        c.closeSocket();
        System.out.println("Terminating ...");
    }
}
