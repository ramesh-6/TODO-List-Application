package taskService.Exception;

public class NoTasksFoundException extends RuntimeException {
    public NoTasksFoundException(String message) {
        super(message);
    }
}
