package de.caritas.cob.statisticsservice.api.controller;

import de.caritas.cob.statisticsservice.api.model.ConsultantStatisticsResponseDTO;
import de.caritas.cob.statisticsservice.api.statistics.service.StatisticsService;
import de.caritas.cob.statisticsservice.generated.api.controller.StatisticsApi;
import io.swagger.annotations.Api;
import java.time.LocalDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller for statistics API requests */
@RestController
@Api(tags = "statistics-controller")
@RequiredArgsConstructor
public class StatisticsController implements StatisticsApi {

  private final @NonNull StatisticsService statisticsService;

  /**
   * Returns statistical data for a consultant.
   *
   * @param dateFrom start of the period (inclusive)
   * @param dateTo end of the period (inclusive)
   * @return a {@link ConsultantStatisticsResponseDTO} instance with the statistical data.
   */
  @Override
  public ResponseEntity<ConsultantStatisticsResponseDTO> getConsultantStatistics(
      @RequestParam LocalDate dateFrom, @RequestParam LocalDate dateTo) {
    return new ResponseEntity<>(
        statisticsService.fetchStatisticsData(dateFrom, dateTo),
        HttpStatus.OK);
  }

}
