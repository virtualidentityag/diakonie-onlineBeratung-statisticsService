package de.caritas.cob.statisticsservice.api.statistics.service;

import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.CONSULTANT_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.DATE_FROM;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.DATE_TO;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.caritas.cob.statisticsservice.api.exception.httpresponses.BadRequestException;
import de.caritas.cob.statisticsservice.api.helper.AuthenticatedUser;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventRepository;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceTest {

  @InjectMocks StatisticsService statisticsService;
  @Mock StatisticsEventRepository statisticsEventRepository;
  @Mock AuthenticatedUser authenticatedUser;

  @Test(expected = BadRequestException.class)
  public void fetchStatisticsData_Should_ThrowBadRequestException_WhenDateFromIsAfterDateTo() {
    statisticsService.fetchStatisticsData(DATE_TO, DATE_FROM);
  }

  @Test
  public void fetchStatisticsData_Should_RetrieveStatisticsDataViaRepository() {

    Instant dateFromConverted = OffsetDateTime.of(DATE_FROM, LocalTime.MIN, ZoneOffset.UTC).toInstant();
    Instant dateToConverted = OffsetDateTime.of(DATE_TO, LocalTime.MAX, ZoneOffset.UTC).toInstant();

    when(authenticatedUser.getUserId()).thenReturn(CONSULTANT_ID);
    statisticsService.fetchStatisticsData(DATE_FROM, DATE_TO);

    verify(statisticsEventRepository, times(1))
        .calculateNumberOfAssignedSessionsForUser(CONSULTANT_ID, dateFromConverted, dateToConverted);
    verify(statisticsEventRepository, times(1))
        .calculateNumberOfSentMessagesForUser(CONSULTANT_ID, dateFromConverted, dateToConverted);
    verify(statisticsEventRepository, times(1))
        .calculateNumbersOfSessionsWhereUserWasActive(CONSULTANT_ID, dateFromConverted, dateToConverted);
    verify(statisticsEventRepository, times(1))
        .calculateTimeInVideoCallsForUser(CONSULTANT_ID, dateFromConverted, dateToConverted);
  }

}
