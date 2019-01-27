package udpEcho.udp.echo.project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/** {@link UDPEchoServer} will listen and re-send
 * received datagram, check the following url for info about datagram
 * https://stackoverflow.com/questions/11636405/definition-of-network-units-fragment-segment-packet-frame-datagram
 *
 */
public class UDPEchoServer {
    public static void main(String[] args) throws IOException {
        // Start the receiver
        UDPReceiver receiver = new UDPReceiver(PortUsed.SERVER_PORT);

        // Wait something to receive it as String
        String sentence = receiver.receive();

        // Convert received String to DatagramPacket
        // Here the destination port & address are specified
        DatagramPacket packet  = new DatagramPacket(sentence.getBytes(), sentence.length(),InetAddress.getLocalHost(),PortUsed.CLIENT_PORT);

        // Create a socket for passing datagram packets
        // DatagramSocket vs Socket
        // https://stackoverflow.com/a/23390439/7054574
        DatagramSocket datagramSocket = new DatagramSocket();

        // Send the datagram
        datagramSocket.send(packet);
    }
}
