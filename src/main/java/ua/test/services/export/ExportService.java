package ua.test.services.export;

import ua.test.exceptions.BusinessLogicException;

import javax.annotation.Nonnull;
import java.io.InputStream;

/**
 * Describe methods to work with CSV
 *
 * @author vzagnitko
 */
public interface ExportService {

    /**
     * Create a CSV report
     *
     * @param backupId to find a CSV report
     * @return object of created file
     * @throws BusinessLogicException if cannot save a file
     */
    @Nonnull
    InputStream createReport(@Nonnull String backupId) throws BusinessLogicException;

}
