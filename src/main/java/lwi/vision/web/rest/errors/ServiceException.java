package lwi.vision.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class ServiceException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super(ErrorConstants.SERVICE_EXCEPTION_TYPE, "Service Exception", Status.BAD_REQUEST);
    }
}
