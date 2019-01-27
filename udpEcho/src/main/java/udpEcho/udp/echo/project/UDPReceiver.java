package udpEcho.udp.echo.project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class UDPReceiver {

    private static final int BUFLEN = 2000;
    final private int port;
    private DatagramSocket serverSocket = null;
    UDPReceiver(int port){
        this.port = port;
    }

    /** Specifies the socket is bound to specific address or not
     * @return {@link boolean} if socket is bound to specific address
     */
    boolean isBound(){
        return serverSocket != null && serverSocket.isBound();
    }

    /** Receives a message on
     * the socket opened on specified port
     *
     * @return received message
     */
    String receive(){
        try {
            // Wait a bit !
           Thread.sleep(1000);

             /*
             Create a socket for passing datagram packets
             DatagramSocket vs Socket
             https://stackoverflow.com/a/23390439/7054574
             Notice port is used here to listen on that port
             */
            serverSocket = new DatagramSocket(port);

            // A space to receive data into
            byte[] receiveData = new byte[BUFLEN];

            // No destination here because it's to be received
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            /*
            Receive the packet on socket, this will wait until something being received
            It receives a datagram packet from this socket. When this method returns,
            the {@code DatagramPacket}'s buffer is filled with the data received.
            The datagram packet also contains the sender's IP address, and
            the port number on the sender's machine.
            */
            serverSocket.receive(receivePacket);

            // Return received data
            return new String(receivePacket.getData());
        }catch (IOException | InterruptedException e){
            return e.toString();
        }
    }
}
