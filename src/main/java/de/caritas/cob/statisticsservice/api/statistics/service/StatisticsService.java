package de.caritas.cob.statisticsservice.api.statistics.service;

import de.caritas.cob.statisticsservice.api.exception.httpresponses.BadRequestException;
import de.caritas.cob.statisticsservice.api.helper.AuthenticatedUser;
import de.caritas.cob.statisticsservice.api.model.ConsultantStatisticsResponseDTO;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for statistical requests.
 */
@Service
@RequiredArgsConstructor
public class StatisticsService {

  private @NonNull StatisticsEventRepository statisticsEventRepository;
  private @NonNull AuthenticatedUser authenticatedUser;

  /**
   * Returns statistical data for a consultant.
   *
   * @param dateFrom the start date of the period
   * @param dateTo the end date of the period
   * @return a {@link ConsultantStatisticsResponseDTO} instance with the statistical data.
   */
  public ConsultantStatisticsResponseDTO fetchStatisticsData(LocalDate dateFrom, LocalDate dateTo) {
    validateDates(dateFrom, dateTo);
    return buildResponseDTO(dateFrom, dateTo);
  }

  private void validateDates(LocalDate dateFrom, LocalDate dateTo) {
    if (dateTo.isBefore(dateFrom)) {
      throw new BadRequestException("dateFrom must be before dateTo");
    }
  }

  private ConsultantStatisticsResponseDTO buildResponseDTO(LocalDate dateFrom, LocalDate dateTo) {

    Instant dateFromConverted = convertDateFrom(dateFrom);
    Instant dateToConverted = convertDateTo(dateTo);

    return new ConsultantStatisticsResponseDTO()
        .numberOfAssignedSessions(statisticsEventRepository
            .calculateNumberOfAssignedSessionsForUser(
                authenticatedUser.getUserId(),
                dateFromConverted,
                dateToConverted))
        .numberOfSentMessages(statisticsEventRepository
            .calculateNumberOfSentMessagesForUser(
                authenticatedUser.getUserId(),
                dateFromConverted,
                dateToConverted))
        .numberOfSessionsWhereConsultantWasActive(statisticsEventRepository
            .calculateNumbersOfSessionsWhereUserWasActive(
                authenticatedUser.getUserId(),
                dateFromConverted,
                dateToConverted))
        .videoCallDuration(statisticsEventRepository
            .calculateTimeInVideoCallsForUser(
                authenticatedUser.getUserId(),
                dateFromConverted,
                dateToConverted))
        .dateFrom(dateFrom)
        .dateTo(dateTo);
  }

  private Instant convertDateFrom(LocalDate dateFrom) {
    return OffsetDateTime.of(dateFrom, LocalTime.MIN, ZoneOffset.UTC).toInstant();
  }

  private Instant convertDateTo(LocalDate dateTo) {
    return OffsetDateTime.of(dateTo, LocalTime.MAX, ZoneOffset.UTC).toInstant();
  }

}
