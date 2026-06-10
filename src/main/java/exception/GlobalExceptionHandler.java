package exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", "Richiesta non valida",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex
    ) {
        String message = ex.getMessage();

        if (message != null && message.contains("Errore API meteo")) {
            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of(
                            "error", "Errore API meteo",
                            "message", "Il servizio meteo esterno non è disponibile o ha restituito un errore.",
                            "details", message
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Errore interno del server",
                        "message", message != null ? message : "Errore imprevisto"
                ));
    }
}