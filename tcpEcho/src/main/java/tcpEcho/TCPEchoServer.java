package tcpEcho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * This is simple TCP echo server which can be connected
 * to using simple telnet client on any OS
 */
public class TCPEchoServer {
    private static final int TELNET_PORT = 8080;
    private static final int BUFLEN = 1024;
    private static final Logger logger = LoggerFactory.getLogger(TCPEchoServer.class);

    public static void main(String[] args) throws IOException {
        // Server Socket creating that will wait a request then starts
        // to act based on that request
        // https://stackoverflow.com/questions/2004531/what-is-the-difference-between-socket-and-serversocket
        ServerSocket serverSocket = new ServerSocket(TELNET_PORT);
        logger.info("Server Started...");

        // Listens for a connection to be made to this socket and accepts
        // it. The method blocks until a connection is made.
        // Client Socket is retrieved on connection which can be used
        // for interacting with the client
        Socket clientSocket = serverSocket.accept();

        // Once connection made, we use the client socket connected
        InetSocketAddress remote = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        logger.info("connection from port=" + remote.getPort() + " host=" + remote.getHostName());
        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();

        // Read as long the socket is connected and write immediately what's
        // being read to socket's OutputStream
        byte[] buffer = new byte[BUFLEN];
        while (clientSocket.isConnected()) {
            int len = in.read(buffer);
            if (len > 0) {
                out.write(buffer, 0, len);
            }
        }
    }
}
