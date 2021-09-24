package de.caritas.cob.statisticsservice.api.controller;

import static de.caritas.cob.statisticsservice.api.testhelper.PathConstants.PATH_GET_CONSULTANT_STATISTICS;
import static de.caritas.cob.statisticsservice.api.testhelper.PathConstants.PATH_GET_CONSULTANT_STATISTICS_CSV;
import static org.powermock.reflect.Whitebox.setInternalState;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.caritas.cob.statisticsservice.StatisticsServiceApplication;
import de.caritas.cob.statisticsservice.api.authorization.RoleAuthorizationAuthorityMapper;
import de.caritas.cob.statisticsservice.api.service.LogService;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = StatisticsServiceApplication.class)
public class StatisticsControllerIT {

  @Autowired
  private MockMvc mvc;
  @MockBean
  private RoleAuthorizationAuthorityMapper roleAuthorizationAuthorityMapper;
  @MockBean
  MongoTemplate mongoTemplate;
  @MockBean
  StatisticsEventRepository statisticsEventRepository;
  @Mock
  private Logger logger;

  @Before
  public void setup() {
    setInternalState(LogService.class, "LOGGER", logger);
  }

  @Test
  public void getConsultantStatistics_Should_ReturnOK() throws Exception {

    mvc.perform(
        get(PATH_GET_CONSULTANT_STATISTICS)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void getConsultantStatisticsCsv_Should_ReturnOK() throws Exception {

    mvc.perform(
            get(PATH_GET_CONSULTANT_STATISTICS_CSV)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}
