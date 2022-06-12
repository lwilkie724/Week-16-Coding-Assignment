package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;
import lombok.Getter;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, config = @SqlConfig(encoding = "utf-8"))

class CreateOrderTest {

  @Autowired
  @Getter
  private TestRestTemplate restTemplate;
  
  @LocalServerPort
  private int serverPort;
  
  
  
  @Test
  void testCreateOrderReturnsSuccess201() {
    String body = createOrderBody();
    String uri = String.format("http://localhost:%d/orders", serverPort);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity<String> bodyEntity= new HttpEntity<>(body, headers);
    
    ResponseEntity<Order> response = getRestTemplate().exchange(uri, HttpMethod.POST, bodyEntity,
        Order.class);
    
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    
    Order order = response.getBody();
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("MAYNARD_TORBJORG");
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.GRAND_CHEROKEE);
    assertThat(order.getModel().getTrimLevel()).isEqualTo("Limited X");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
    assertThat(order.getColor().getColorId()).isEqualTo("EXT_TECHNO_GREEN");
    assertThat(order.getEngine().getEngineId()).isEqualTo("6_4_GAS");
    assertThat(order.getTire().getTireId()).isEqualTo("265_MICHELIN");
    assertThat(order.getOptions()).hasSize(6);
  }
  
  protected String createOrderBody() {
    //@formatter:off
    
    String body = "{\n"
        + "  \"customer\":\"MAYNARD_TORBJORG\",\n"
        + "  \"model\":\"GRAND_CHEROKEE\",\n"
        + "  \"trim\":\"Limited X\",\n"
        + "  \"doors\":4,\n"
        + "  \"color\":\"EXT_TECHNO_GREEN\",\n"
        + "  \"engine\":\"6_4_GAS\",\n"
        + "  \"tire\":\"265_MICHELIN\",\n"
        + "  \"options\":[\n"
        + "    \"DOOR_BODY_FRONT\",\n"
        + "    \"EXT_TACTIK_FRONT_WINCH\",\n"
        + "    \"INT_MOPAR_MATS\",\n"
        + "    \"INT_MOPAR_UCONNECT\",\n"
        + "    \"WHEEL_MOPAR_SPARE\",\n"
        + "    \"EXT_MOPAR_TRAILER\"\n"
        + "  ]\n"
        + "}";
  
        //@formatter:on
    return body;
  }

}
