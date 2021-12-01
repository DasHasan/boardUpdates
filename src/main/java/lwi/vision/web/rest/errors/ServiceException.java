package lwi.vision.web.rest.errors;

import java.util.HashMap;
import java.util.Map;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ServiceException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super(ErrorConstants.SERVICE_EXCEPTION_TYPE, "ServiceException", Status.BAD_REQUEST, null, null, null);
    }

    public ServiceException(String message) {
        super(ErrorConstants.SERVICE_EXCEPTION_TYPE, "ServiceException", Status.BAD_REQUEST, null, null, null, getAlertParameters(message));
    }

    private static Map<String, Object> getAlertParameters(String message) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", message);
        return parameters;
    }
}
