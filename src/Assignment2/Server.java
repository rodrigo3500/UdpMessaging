package Assignment2;

import java.net.*;

public class Server extends UdpMessenger {

    private DatagramSocket sendSocket, receiveSocket;

    public Server(int receivePort, int maxRequestBytes) {
        super(maxRequestBytes);
        System.out.println("Server running ...");
        try {
            // Binds a socket to given port on the local host machine.
            // This socket will be used to receive UDP Datagram packets.
            this.receiveSocket = new DatagramSocket(receivePort);  // 23
//            receiveSocket.setSoTimeout(5000);
            System.out.println("Receive Socket initiated on port: " + receiveSocket.getPort() + "\n");

        } catch (SocketException se) {
            closeReceiveocket();
            se.printStackTrace();
            System.exit(1);
        }
    }

    /**
    * Binds a socket to any available port on the local host machine.
    * This socket will be used to send UDP Datagram packets.
    */
    private void createSendSocket(){
        
        try {
            this.sendSocket = new DatagramSocket();
//            this.sendSocket.setSoTimeout(5000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("Send Socket initiated on port: " + sendSocket.getPort()+ "\n");
    }

    /**
     * Closes sendSocket
     */
    private void closeSendSocket(){
        sendSocket.close();
    }

    /**
     * Closes receiveSocket
     */
    private void closeReceiveocket(){
        receiveSocket.close();
    }
    public static void main(String[] args) throws Exception {
        int serverReceivePort = 6969;
        final int MAX_REQUEST_SIZE = 17;

        Server s = new Server(serverReceivePort, MAX_REQUEST_SIZE);
        Request request;
        boolean validRequest;

        // Message replies
        byte[] writeReplyBytes = {0,4,0,0};
        String writeReplyString = new String(writeReplyBytes);
        byte[] readReplyBytes = {0,3,0,1};
        String readReplyString = new String(readReplyBytes);


        // Loops forever until an invalid request is received
        while(true){
            // The server waits to receive a request  <==
            DatagramPacket hostPacket = s.receive(s.receiveSocket);
            // Server will extract the data and parse it to confirm that the format is valid
            String data = new String(hostPacket.getData());
            request = new Request(data, MAX_REQUEST_SIZE);
            validRequest = request.validateRequestString();


            // Send reply if request is valid ==>
            if (validRequest) {
                s.createSendSocket();  // Create new socket to reply with
                if (request.requestType().equals("read")) {
                    // Send byte array 0 3 0 1 in return
                    s.send(hostPacket.getPort(), readReplyString, s.sendSocket);
                } else if (request.requestType().equals("write")) {
                    // Send byte array 0 3 0 1 in return
                    s.send(hostPacket.getPort(), writeReplyString, s.sendSocket);
                } else {
                    // Throw exception if request invalid
                    System.out.println();
                    throw new Exception("Invalid request header");
                }
            } else {
                System.out.println();
                throw new Exception("Invalid request message received\n\"Terminating ...\"");
            }
            // Close the sendSocket
            s.closeSendSocket();
        }
    }
}
