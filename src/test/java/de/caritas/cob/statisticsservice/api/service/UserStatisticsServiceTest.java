package de.caritas.cob.statisticsservice.api.service;


import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.RC_GROUP_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.SESSION_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.caritas.cob.statisticsservice.api.service.securityheader.SecurityHeaderSupplier;
import de.caritas.cob.statisticsservice.userstatisticsservice.generated.web.UserStatisticsControllerApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserStatisticsServiceTest {

  @InjectMocks
  UserStatisticsService userStatisticsService;
  @Mock
  UserStatisticsControllerApi userStatisticsControllerApi;
  @Mock
  SecurityHeaderSupplier securityHeaderSupplier;

  @Test
  public void retrieveSessionViaRcGroupId_Should_RetrieveSessionViaUserStatisticsControllerApi() {

    when(securityHeaderSupplier.getCsrfHttpHeaders()).thenReturn(new HttpHeaders());
    userStatisticsService.retrieveSessionViaRcGroupId(RC_GROUP_ID);
    verify(userStatisticsControllerApi, times(1)).getSession(null, RC_GROUP_ID);
  }

  @Test
  public void retrieveSessionViaSessionId_Should_RetrieveSessionViaUserStatisticsControllerApi() {

    when(securityHeaderSupplier.getCsrfHttpHeaders()).thenReturn(new HttpHeaders());
    userStatisticsService.retrieveSessionViaSessionId(SESSION_ID);
    verify(userStatisticsControllerApi, times(1)).getSession(SESSION_ID, null);
  }

}
