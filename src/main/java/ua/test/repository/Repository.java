package ua.test.repository;

import ua.test.exceptions.RepositoryException;
import ua.test.wrapper.ProcessWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Describe methods to work with repository
 *
 * @author vzagnitko
 */
public interface Repository {

    /**
     * This method used to get object from repository
     *
     * @param processWrapper wrapper for data which will be stored
     * @param clazz          class of backupping file
     * @param <T>            type of backupping file
     * @return response object which will be backuped
     * @throws RepositoryException if cannot backup a data
     */
    @Nullable
    <T> T retrieveBackupObject(@Nonnull ProcessWrapper processWrapper, @Nonnull Class<T> clazz) throws RepositoryException;

    /**
     * This method used to get all backup data results
     *
     * @return results of backup
     * @throws RepositoryException if cannot take a result
     */
    List<ProcessWrapper> retrieveBackupResults() throws RepositoryException;

    /**
     * This method used to save object
     *
     * @param processWrapper
     * @throws RepositoryException if cannot save a object in repository
     */
    void saveBackupObject(@Nonnull ProcessWrapper processWrapper) throws RepositoryException;

}
