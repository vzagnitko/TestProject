package ua.test.util.string;

import java.util.Optional;

/**
 * This class contains main utils to work with strings
 *
 * @author vzagnitko
 */
public class StringUtils {

    /**
     * Concatenate strings
     *
     * @param args to concatenate
     * @return concatenated string
     */
    public static String concatString(String... args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            if (!Optional.ofNullable(arg).isPresent()) {
                continue;
            }
            sb.append(arg);
        }
        return sb.toString();
    }

}
