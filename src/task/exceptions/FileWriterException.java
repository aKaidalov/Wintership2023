package task.exceptions;

public class FileWriterException extends RuntimeException {
    public FileWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
