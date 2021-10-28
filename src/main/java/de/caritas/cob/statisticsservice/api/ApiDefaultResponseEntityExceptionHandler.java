package de.caritas.cob.statisticsservice.api;

import de.caritas.cob.statisticsservice.api.service.LogService;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@NoArgsConstructor
public class ApiDefaultResponseEntityExceptionHandler {

  /**
   * "Catch all" respectively fallback for all controller error messages that are not specifically
   * retained by {@link ApiResponseEntityExceptionHandler}. For the caller side does not need to
   * know the exact error stack trace, this method catches the trace and logs it.
   *
   * @param ex      the thrown {@link RuntimeException}
   * @param request the {@link WebRequest}
   * @return a {@link ResponseEntity}
   */
  @ExceptionHandler(value = RuntimeException.class)
  public ResponseEntity<Object> handleInternal(final RuntimeException ex,
      final WebRequest request) {
    LogService.logInternalServerError(ex);

    return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
