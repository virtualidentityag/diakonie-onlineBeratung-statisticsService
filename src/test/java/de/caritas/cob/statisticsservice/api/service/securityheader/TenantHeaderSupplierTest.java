package de.caritas.cob.statisticsservice.api.service.securityheader;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class TenantHeaderSupplierTest {

  @InjectMocks
  TenantHeaderSupplier tenantHeaderSupplier;

  @Test
  void addTechnicalTenantHeaderIfMultitenancyEnabled_Should_AddHeaderIfMultitenancyEnabled() {
    // given
    tenantHeaderSupplier.setMultitenancy(true);
    HttpHeaders headers = new HttpHeaders();
    // when, then
    tenantHeaderSupplier.addTechnicalTenantHeaderIfMultitenancyEnabled(headers);
    assertThat(headers.get("tenantId").get(0)).isEqualTo("0");
  }

  @Test
  void addTechnicalTenantHeaderIfMultitenancyEnabled_Should_Not_AddHeaderIfMultitenancyNotEnabled() {
    // given
    tenantHeaderSupplier.setMultitenancy(false);
    HttpHeaders headers = new HttpHeaders();
    // when, then
    tenantHeaderSupplier.addTechnicalTenantHeaderIfMultitenancyEnabled(headers);
    assertThat(headers.get("tenantId")).isNull();
  }



}