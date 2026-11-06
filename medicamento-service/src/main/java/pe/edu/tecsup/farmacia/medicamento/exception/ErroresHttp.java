package pe.edu.tecsup.farmacia.medicamento.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ErroresHttp extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException error,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, "Datos invalidos");
        problem.setProperty("campos", error.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage()).toList());
        return handleExceptionInternal(error, problem, headers, status, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflicto() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "El registro viola una restriccion de datos");
    }
}
