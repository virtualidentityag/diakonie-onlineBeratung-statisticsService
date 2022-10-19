package de.caritas.cob.statisticsservice.api.helper;

import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.AGENCY_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.ASKER_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.CONSULTING_TYPE_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import de.caritas.cob.statisticsservice.api.model.EventType;
import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsResponseDTO;
import de.caritas.cob.statisticsservice.api.model.UserRole;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.Agency;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.ConsultingType;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.User;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.ArchiveMetaData;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.RegistrationMetaData;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationStatisticsDTOConverterTest {

  @InjectMocks
  RegistrationStatisticsDTOConverter registrationStatisticsDTOConverter;

  private StatisticsEvent testEvent;

  private Map<Long, StatisticsEvent> archiveSessionLookup;

  @AfterEach
  void teardownEach() {
    testEvent = null;
    archiveSessionLookup = null;
  }

  @Test
  void convertStatisticsEvent_Should_convertToRegistrationStatisticResponse() {
    // given
    givenValidStatisticEvent(1L);

    // when
    RegistrationStatisticsResponseDTO result = registrationStatisticsDTOConverter.convertStatisticsEvent(
        testEvent, null);

    // then
    assertThat(result.getUserId(), is(ASKER_ID));
    assertThat(result.getRegistrationDate(),
        is("2022-09-15T09:14:45Z"));
    assertThat(result.getAge(), is(26));
    assertThat(result.getGender(), is("FEMALE"));
    assertThat(result.getMainTopicInternalAttribute(),
        is("alk"));
    assertThat(result.getTopicsInternalAttributes(),
        is(List.of("alk", "drogen")));
    assertThat(result.getPostalCode(), is("12345"));
    assertThat(result.getCounsellingRelation(),
        is("SELF_COUNSELLING"));
  }

  @Test
  void convertStatisticsEvent_Should_notFail_When_archiveSessionLookupIsNull() {
    // given
    givenValidStatisticEvent(1L);

    // when
    RegistrationStatisticsResponseDTO result = registrationStatisticsDTOConverter.convertStatisticsEvent(
        testEvent, null);

    // then
    assertThat(result.getEndDate(), is(nullValue()));
  }

  @Test
  void convertStatisticsEvent_Should_addArchiveSessionEndDate() {
    // given
    givenValidStatisticEvent(1L);
    givenValidArchiveStatisticEvents();

    // when
    RegistrationStatisticsResponseDTO result = registrationStatisticsDTOConverter.convertStatisticsEvent(
        testEvent, archiveSessionLookup);

    // then
    assertThat(result.getEndDate(), is("end date 1"));
  }

  @Test
  void convertStatisticsEvent_Should_notAddArchiveSessionEndDate_When_noMatchingArchiveSessionIsAvailable() {
    // given
    givenValidStatisticEvent(2L);
    givenValidArchiveStatisticEvents();

    // when
    RegistrationStatisticsResponseDTO result = registrationStatisticsDTOConverter.convertStatisticsEvent(
        testEvent, archiveSessionLookup);

    // then
    assertThat(result.getEndDate(), is(nullValue()));
  }

  private void givenValidStatisticEvent(Long sessionId) {
    Object metaData = RegistrationMetaData.builder()
        .registrationDate("2022-09-15T09:14:45Z")
        .age(26)
        .gender("FEMALE")
        .mainTopicInternalAttribute("alk")
        .topicsInternalAttributes(List.of("alk", "drogen"))
        .postalCode("12345")
        .tenantId(1L)
        .counsellingRelation("SELF_COUNSELLING")
        .build();
    testEvent = StatisticsEvent.builder()
        .sessionId(sessionId)
        .eventType(EventType.REGISTRATION)
        .user(User.builder().userRole(UserRole.ASKER).id(ASKER_ID).build())
        .consultingType(ConsultingType.builder().id(CONSULTING_TYPE_ID).build())
        .agency(Agency.builder().id(AGENCY_ID).build())
        .timestamp(Instant.now())
        .metaData(metaData)
        .build();
  }

  private void givenValidArchiveStatisticEvents() {
    archiveSessionLookup = Map.of(1L, archiveEvent(1L, "end date 1"),
        99L, archiveEvent(99L, "end date 2"));
  }

  private StatisticsEvent archiveEvent(Long sessionId, String endDate) {
    Object metaData = ArchiveMetaData.builder().endDate(endDate).build();
    return StatisticsEvent.builder().sessionId(sessionId).metaData(metaData).build();
  }
}
