package prueba_backend.demo.validations;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import prueba_backend.demo.models.Franquicia;

@Slf4j
public class FranquiciaValidation {

    public static void validateField(String field, String name, boolean isRequired, String formatoRegex, Integer min, Integer max) throws FranquiciaException {
        log.info("Validando campo '{}' con valor: {}", name, field);
        if (field == null || field.trim().isEmpty()) {
            if (isRequired) {
                log.warn("Campo '{}' es requerido pero está vacío", name);
                throwRequired(name);
            }
        } else {
            if (StringUtils.hasText(formatoRegex) && !field.matches(formatoRegex)) {
                log.warn("Campo '{}' no cumple con el formato regex: {}", name, formatoRegex);
                throwInvalid(name);
            }

            if (min != null && min > 0 && field.length() < min) {
                log.warn("Campo '{}' tiene longitud {} menor al mínimo {}", name, field.length(), min);
                throwMinLength(name, min);
            }

            if (max != null && max > 0 && field.length() > max) {
                log.warn("Campo '{}' tiene longitud {} mayor al máximo {}", name, field.length(), max);
                throwMaxLength(name, max);
            }
        }
        log.info("Campo '{}' validado correctamente", name);
    }

    public static void validateFranquicia(Franquicia franquicia) throws FranquiciaException {
        log.info("Validando franquicia: {}", franquicia != null ? franquicia.getNombre() : "null");
        if (franquicia == null) {
            log.error("La franquicia es nula");
            throw new FranquiciaException("La franquicia no puede ser nula.");
        }
        validateField(franquicia.getNombre(), "nombre", true, null, 2, 100);
        log.info("Franquicia validada correctamente");
    }

    private static void throwRequired(String fieldName) throws FranquiciaException {
        throw new FranquiciaException("El campo '" + fieldName + "' es requerido.");
    }

    private static void throwInvalid(String fieldName) throws FranquiciaException {
        throw new FranquiciaException("El campo '" + fieldName + "' tiene un formato inválido.");
    }

    private static void throwMinLength(String fieldName, int min) throws FranquiciaException {
        throw new FranquiciaException("El campo '" + fieldName + "' debe tener al menos " + min + " caracteres.");
    }

    private static void throwMaxLength(String fieldName, int max) throws FranquiciaException {
        throw new FranquiciaException("El campo '" + fieldName + "' no puede tener más de " + max + " caracteres.");
    }
}
