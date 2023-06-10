package dev.starstruck.util.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author aesthetical
 * @since 06/07/23
 */
public class FileUtils {

    /**
     * The root directory for starstuck.dev
     */
    public static final File root = new File(
            Minecraft.getMinecraft().gameDir, "starstuck.dev");

    /**
     * The GSON class instance for JSON related shit
     */
    public static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * The JsonParser class instance to parse json from files
     */
    public static final JsonParser jsonParser = new JsonParser();

    /**
     * Reads a file
     * @param file the file
     * @return the content of the file
     * @throws IOException if the InputStream fails to read/close
     */
    public static String readFile(File file) throws IOException {
        InputStream is = Files.newInputStream(file.toPath());

        StringBuilder builder = new StringBuilder();
        int b;
        while ((b = is.read()) != -1) {
            builder.append((char) b);
        }

        is.close();
        return builder.toString();
    }

    /**
     * Writes to a file
     * @param file the file
     * @param content the content to write to the file
     * @throws IOException if the OutputStream fails to write/close
     */
    public static void writeFile(File file, String content) throws IOException {
        OutputStream os = Files.newOutputStream(file.toPath());
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        os.write(bytes, 0, bytes.length);
        os.close();
    }
}
