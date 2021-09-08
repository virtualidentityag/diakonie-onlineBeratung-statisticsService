package de.caritas.cob.statisticsservice.api.controller;

import de.caritas.cob.statisticsservice.generated.api.controller.StatisticsApi;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for statistics API requests
 */
@RestController
@Api(tags = "statistics-controller")
@RequiredArgsConstructor
public class StatisticsController implements
    de.caritas.cob.statisticsservice.generated.api.controller.StatisticsApi {

  @Override
  public ResponseEntity<Void> getConsultantStatistics() {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> getConsultantStatisticsCsv() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
