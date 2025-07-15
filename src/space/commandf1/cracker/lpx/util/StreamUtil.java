package space.commandf1.cracker.lpx.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static byte[] readAllBytesManual(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] buf = new byte[4096];

        while ((read = in.read(buf)) > 0) {
            out.write(buf, 0, read);
        }

        return out.toByteArray();
    }
}
