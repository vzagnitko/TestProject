package ua.test.util.file;

import com.google.common.base.Strings;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class contains main utils to work with files
 *
 * @author vzagnitko
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Fast read file
     *
     * @param path to read file
     * @return file content
     * @throws IOException if cannot read a file
     */
    public static String fastReadFile(@Nonnull String path) throws IOException {
        if (Strings.isNullOrEmpty(path)) {
            throw new IllegalArgumentException("Path to save is incorrect!");
        }
        if (Files.notExists(Paths.get(path))) {
            throw new IllegalStateException("File with name: " + FilenameUtils.getBaseName(path) + " not found in the server!");
        }
        try (RandomAccessFile raf = new RandomAccessFile(path, "r")) {
            try (FileChannel in = raf.getChannel()) {
                in.force(true);
                MappedByteBuffer mbb = in.map(FileChannel.MapMode.READ_ONLY, 0L, in.size());
                mbb = mbb.force();
                byte[] buffer = new byte[(int) in.size()];
                mbb.get(buffer);
                return IOUtils.toString(buffer, StandardCharsets.UTF_8.name());
            }
        }
    }

    /**
     * Fast save file
     *
     * @param path to save a file
     * @param body to save
     * @throws IOException if cannot save a file
     */
    public static void fastSaveFile(String path, String body) throws IOException {
        if (Strings.isNullOrEmpty(path)) {
            throw new IllegalArgumentException("Path to file is incorrect!");
        }
        if (Strings.isNullOrEmpty(body)) {
            throw new IllegalArgumentException("Body to file is incorrect!");
        }
        try (FileChannel rwChannel = new RandomAccessFile(path, "rw").getChannel()) {
            rwChannel.force(true);
            byte[] buffer = body.getBytes();
            rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, buffer.length).put(buffer);
        }
    }

}
