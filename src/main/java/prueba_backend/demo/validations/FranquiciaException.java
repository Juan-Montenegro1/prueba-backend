package prueba_backend.demo.validations;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FranquiciaException extends RuntimeException {
    public FranquiciaException(String message) {
        super(message);
        log.error("FranquiciaException creada: {}", message);
    }
}