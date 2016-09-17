package ua.test.util.file;

import com.google.common.base.Strings;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
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
    @Nonnull
    public static InputStream fastReadFile(@Nonnull String path) throws IOException {
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
                return new ByteArrayInputStream(buffer);
            }
        }
    }

}
