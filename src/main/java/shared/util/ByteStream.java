package shared.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteStream {
    private final BufferedOutputStream writer;
    private final BufferedInputStream reader;

    public ByteStream(BufferedOutputStream writer, BufferedInputStream reader) {
        this.writer = writer;
        this.reader = reader;
    }

    public void sendByte(String message) throws IOException {
        byte[] res = message.getBytes();
        sendLength(res.length);

        writer.write(res);
        writer.flush();
    }

    public String readByte() throws IOException {
        byte[] length = new byte[4];
        reader.read(length);

        int len = getLength(length);
        int read = 0;
        byte[] req = new byte[len];
        while (read < len) {
            read += reader.read(req, read, len - read);
        }

        return new String(req);
    }

    private void sendLength(int len) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(len);
        byte[] length = buffer.array();

        writer.write(length);
        writer.flush();
    }

    private int getLength(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }
}