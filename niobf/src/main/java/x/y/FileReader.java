package x.y;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileReader {
    public static void main(String[] args) throws IOException, URISyntaxException {
        final Logger logger = LoggerFactory.getLogger(FileReader.class);
        logger.info("Started");
        URL url = FileReader.class.getClassLoader().getResource("sample.txt");
        URI newUri = url.toURI().resolve("./anotherSampleFile.txt");
        StandardOpenOption[] options = {StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE, StandardOpenOption.WRITE};
        FileChannel newFileChannel = FileChannel.open(Path.of(newUri), options);
        final FileChannel channel = FileChannel.open(Path.of(url.toURI()));
        channel.transferTo(0, channel.size(), newFileChannel);
        final ByteBuffer buf = ByteBuffer.allocate(4098);
        int nrBytes = channel.read(buf);
        while(nrBytes != -1){
            logger.info("Read " + nrBytes);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            nrBytes = channel.read(buf);
        }
        channel.close();
    }
}
