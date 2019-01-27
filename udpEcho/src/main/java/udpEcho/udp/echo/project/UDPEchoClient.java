package udpEcho.udp.echo.project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/** {@link UDPEchoClient} used as client
 * to connect with {@link UDPEchoServer}
 * sending datagram to that server
 * then receives same exact message from same server
 */
public class UDPEchoClient {

    private static final String MESSAGE = "Hello, World!";

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        // byte array representation to our message
        byte[] buffer = MESSAGE.getBytes();

        // Convert our message to DatagramPacket
        // Here the destination port & address are specified
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length,InetAddress.getLocalHost(), PortUsed.SERVER_PORT);

        // Create a socket for passing datagram packets
        // DatagramSocket vs Socket
        // https://stackoverflow.com/a/23390439/7054574
        DatagramSocket datagramSocket = new DatagramSocket();

        // Start the receiver
        final UDPReceiver receiver = new UDPReceiver(PortUsed.CLIENT_PORT);

        /*
        Create a separated thread to perform async operation
        In this case it's receiving a response
        This thread will wait until response comes, see {@link UDPReceiver.receive()}
        */
        CompletableFuture<String> response = CompletableFuture.supplyAsync(receiver::receive);

        // While receiver is NOT bound to specific address, do nothing !
        int i = 1;
        while (!receiver.isBound()){
            System.out.println("" + i + ". not bound yet");
            i++;
            Thread.sleep(100);
        }

        // Send the message on the socket after being bound to specific address
        System.out.println("SENDING: '" + MESSAGE);
        datagramSocket.send(packet);

        /*
        Get received String or wait if it's
        not received yet. See {@link UDPReceiver.receive()}
        */
        String received = response.get();
        System.out.println("SEND '" + MESSAGE + "' RECEIVED '" + received + "'");

    }
}
