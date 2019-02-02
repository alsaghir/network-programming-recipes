package tcpMultiThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/** Multi-Thread Echo server
 * to serve multiple clients
 * in different threads
 */
public class TCPMultiThreadEchoServer
{
    private static final int TELNET_PORT = 8080;
    private static final int BUFFER_LENGTH = 1024;
    private static int sessions = 0;
    private static Logger logger = LoggerFactory.getLogger(TCPMultiThreadEchoServer.class);

    public static void main( String[] args ) throws IOException
    {
        try {

            // Server Socket creating that will wait a request then starts
            // to act based on that request
            // https://stackoverflow.com/questions/2004531/what-is-the-difference-between-socket-and-serversocket
            ServerSocket serverSocket = new ServerSocket(TELNET_PORT);

            while (true) {

                // Listens for a connection to be made to this socket and accepts
                // it. The method blocks until a connection is made.
                // Client Socket is retrieved on connection which can be used
                // for interacting with the client
                final Socket clientSocket = serverSocket.accept();

                // Once connection made, we use the client socket connected
                InetSocketAddress remote = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
                logger.info("connection from port=" + remote.getPort() + " host=" + remote.getHostName());

                new Thread(
                        () -> {
                            sessions += 1;
                            InputStream in;
                            try {
                                logger.info("session " + sessions + " Started...");
                                in = clientSocket.getInputStream();
                                OutputStream out = clientSocket.getOutputStream();

                                // Read as long the socket is connected and write immediately what's
                                // being read to socket's OutputStream
                                byte[] bufer = new byte[BUFFER_LENGTH];
                                while (clientSocket.isConnected() && in.transferTo(out) > 0) { }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                ).start();
            }
        }
        catch (SocketException e){
            logger.info("Client exit..");
        }
    }
}
