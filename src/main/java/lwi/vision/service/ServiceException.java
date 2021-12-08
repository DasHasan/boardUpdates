package lwi.vision.service;

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public static ServiceException fileNotFound() {
        return new ServiceException("Datei konnte nicht gefunden werden");
    }
}
