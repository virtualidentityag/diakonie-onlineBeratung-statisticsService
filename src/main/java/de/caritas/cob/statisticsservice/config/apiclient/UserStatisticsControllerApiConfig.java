package de.caritas.cob.statisticsservice.config.apiclient;

import de.caritas.cob.statisticsservice.userstatisticsservice.generated.ApiClient;
import de.caritas.cob.statisticsservice.userstatisticsservice.generated.web.UserStatisticsControllerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserStatisticsControllerApiConfig {

  @Value("${user.statistics.service.api.url}")
  private String userStatisticsServiceApiUrl;

  /**
   * UserStatisticsService controller bean.
   *
   * @param apiClient {@link ApiClient}
   * @return the UserStatisticsService controller {@link UserStatisticsControllerApi}
   */
  @Bean
  public UserStatisticsControllerApi userStatisticsControllerApi(
      ApiClient apiClient) {
    return new UserStatisticsControllerApi(apiClient);
  }

  /**
   * ConsultingTypeService API client bean.
   *
   * @param restTemplate {@link RestTemplate}
   * @return the ConsultingTypeService {@link ApiClient}
   */
  @Bean
  @Primary
  public ApiClient userStatisticsApiClient(RestTemplate restTemplate) {
    return new ApiClient(restTemplate).setBasePath(this.userStatisticsServiceApiUrl);
  }

}
