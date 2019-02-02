package ThreadPoolTcpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Same as {@link tcpMultiThread} but with pool of fixed
 * number of threads handling the clients
 */
public class ThreadPoolTcpServer {
    private static final int TELNET_PORT = 8080;
    private static final int BUFLEN = 1024;
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolTcpServer.class);

    public static void main(String[] args) throws IOException {

        // Server Socket creating that will wait a request then starts
        // to act based on that request
        // https://stackoverflow.com/questions/2004531/what-is-the-difference-between-socket-and-serversocket
        final ServerSocket serverSocket = new ServerSocket(TELNET_PORT);

        // Creating fixed thread pool
        final ExecutorService service = Executors.newFixedThreadPool(2);

        while (true) {

            // Listens for a connection to be made to this socket and accepts
            // it. The method blocks until a connection is made.
            // Client Socket is retrieved on connection which can be used
            // for interacting with the client
            final Socket clientSocket = serverSocket.accept();

            // Once connection made, we use the client socket connected
            final InetSocketAddress remote = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
            logger.info("connection from port=" + remote.getPort() + " host=" + remote.getHostName());
            service.submit(
                    () -> {
                        try {
                            final InputStream in = clientSocket.getInputStream();
                            final OutputStream out = clientSocket.getOutputStream();

                            // Read as long the socket is connected and write immediately what's
                            // being read to socket's OutputStream
                            byte[] buffer = new byte[BUFLEN];
                            while (clientSocket.isConnected()) {
                                int len = in.read(buffer);
                                if (len > 0) {
                                    out.write(buffer);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
        }
    }
}
