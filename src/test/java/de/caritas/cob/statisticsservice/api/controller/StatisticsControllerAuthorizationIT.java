package de.caritas.cob.statisticsservice.api.controller;

import static de.caritas.cob.statisticsservice.api.authorization.Authority.CONSULTANT;
import static de.caritas.cob.statisticsservice.api.testhelper.PathConstants.PATH_GET_CONSULTANT_STATISTICS;
import static de.caritas.cob.statisticsservice.api.testhelper.PathConstants.PATH_GET_CONSULTANT_STATISTICS_CSV;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.caritas.cob.statisticsservice.api.authorization.Authority;
import de.caritas.cob.statisticsservice.api.authorization.Authority.AuthorityValue;
import javax.servlet.http.Cookie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = "spring.profiles.active=testing")
@SpringBootTest
@AutoConfigureMockMvc
public class StatisticsControllerAuthorizationIT {

  private final String CSRF_COOKIE = "csrfCookie";
  private final String CSRF_HEADER = "csrfHeader";
  private final String CSRF_VALUE = "test";
  private final Cookie csrfCookie = new Cookie(CSRF_COOKIE, CSRF_VALUE);

  @Autowired
  private MockMvc mvc;

  @Test
  @WithMockUser(authorities = {AuthorityValue.CONSULTANT_DEFAULT})
  public void getConsultantStatistics_Should_ReturnOK_When_ProperlyAuthorizedWithConsultantAuthority()
      throws Exception {
    this.mvc.perform(get(PATH_GET_CONSULTANT_STATISTICS)
            .cookie(csrfCookie)
            .header(CSRF_HEADER, CSRF_VALUE)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  public void getConsultantStatistics_Should_ReturnForbidden_When_NoConsultantDefaultAuthority()
      throws Exception {
    this.mvc.perform(get(PATH_GET_CONSULTANT_STATISTICS)
            .cookie(csrfCookie)
            .header(CSRF_HEADER, CSRF_VALUE)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {AuthorityValue.CONSULTANT_DEFAULT})
  public void getConsultantStatisticsCsv_Should_ReturnOK_When_ProperlyAuthorizedWithConsultantAuthority()
      throws Exception {
    this.mvc.perform(get(PATH_GET_CONSULTANT_STATISTICS_CSV)
            .cookie(csrfCookie)
            .header(CSRF_HEADER, CSRF_VALUE)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  public void getConsultantStatisticsCsv_Should_ReturnForbidden_When_NoConsultantDefaultAuthority()
      throws Exception {
    this.mvc.perform(get(PATH_GET_CONSULTANT_STATISTICS_CSV)
            .cookie(csrfCookie)
            .header(CSRF_HEADER, CSRF_VALUE)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

}
