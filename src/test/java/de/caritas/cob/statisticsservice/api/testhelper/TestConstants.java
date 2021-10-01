package de.caritas.cob.statisticsservice.api.testhelper;

import de.caritas.cob.statisticsservice.api.model.ConsultantStatisticsResponseDTO;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class TestConstants {

  private TestConstants() {}

  public static final String RC_GROUP_ID = "yhT3444kk";
  public static final Long SESSION_ID = 34344L;
  public static final String CONSULTANT_ID = "d63f4cc0-215d-40e2-a866-2d3e910f0590";
  public static final Long AGENCY_ID = 23447L;
  public static final int CONSULTING_TYPE_ID = 1;
  public static final String VIDEO_CALL_UUID = "123e4567-e89b-12d3-a456-556642440000";
  public static final String MONGO_ID = "614dc580932dd4444c3d0ddc";
  public static final LocalDate DATE_FROM = LocalDate.of(2021, Month.MAY, 1);
  public static final LocalDate DATE_TO = LocalDate.of(2021, Month.MAY, 31);
  public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
  public static final String DATE_FROM_FORMATTED = DATE_FROM.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));
  public static final String DATE_TO_FORMATTED = DATE_TO.format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN));

  public static ConsultantStatisticsResponseDTO CONSULTANT_STATISTICS_RESPONSE_DTO =
      new ConsultantStatisticsResponseDTO()
          .dateFrom(DATE_FROM)
          .dateTo(DATE_TO)
          .numberOfSessionsWhereConsultantWasActive(5L)
          .numberOfSentMessages(10L)
          .numberOfAssignedSessions(2L)
          .videoCallDuration(9800L);
}
