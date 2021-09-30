package de.caritas.cob.statisticsservice.api.statistics.repository;

import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.CONSULTANT_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.DATE_FROM;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.DATE_TO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.caritas.cob.statisticsservice.StatisticsServiceApplication;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@DataMongoTest()
@ContextConfiguration(classes = StatisticsServiceApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource(properties = "spring.profiles.active=testing")
public class StatisticsEventRepositoryIT {

  private final Instant dateFromConverted =
      OffsetDateTime.of(DATE_FROM, LocalTime.MIN, ZoneOffset.UTC).toInstant();
  private final Instant dateToConverted =
      OffsetDateTime.of(DATE_TO, LocalTime.MAX, ZoneOffset.UTC).toInstant();
  @Autowired StatisticsEventRepository statisticsEventRepository;
  @Autowired private MongoTemplate mongoTemplate;

  @Before
  public void createDataSet() {
    mongoTemplate.dropCollection("statistics_event");
    try {
      List<Document> myObjects =
          new ObjectMapper().readValue(
              Paths.get("resources/mongodb/StatisticsEvents.json").toFile(),
              new TypeReference<List<Document>>() {});
      mongoTemplate.insert(myObjects, "statistics_event");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void calculateNumberOfAssignedSessionsForUser_Should_ReturnCorrectNumberOfSessions() {

    Query query = new Query();
    query.addCriteria(Criteria.where("eventType").is("ASSIGN_SESSION"));

    System.out.println(mongoTemplate.find(query, StatisticsEvent.class));

    assertThat(
        statisticsEventRepository.calculateNumberOfAssignedSessionsForUser(
            CONSULTANT_ID, dateFromConverted, dateToConverted),
        is(0L));
  }
}
