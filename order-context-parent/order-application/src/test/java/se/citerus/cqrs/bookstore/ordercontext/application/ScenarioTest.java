package se.citerus.cqrs.bookstore.ordercontext.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;

import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

public class ScenarioTest {

  public static final String SERVER_ADDRESS = "http://localhost:8080/service";

  @ClassRule
  public static final DropwizardAppRule<OrderApplicationConfiguration> RULE =
      new DropwizardAppRule<>(OrderApplication.class, resourceFilePath("test.yml"));

  private Client client = new Client();

  public static String resourceFilePath(String resourceClassPathLocation) {
    try {
      return new File(Resources.getResource(resourceClassPathLocation).toURI()).getAbsolutePath();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Before
  public void setUp() throws Exception {
    ObjectMapper objectMapper = RULE.getEnvironment().getObjectMapper();
    objectMapper.enable(INDENT_OUTPUT);
    objectMapper.enable(WRITE_DATES_AS_TIMESTAMPS);
  }


  @Test
  public void testPlaceOrder() throws InterruptedException {
//    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
//    OrderId orderId = addPlaceOrder(customer);
//
//    Thread.sleep(500);
//
//    OrderProjection order = getOrder(orderId);
//    assertThat(order.getOrderId(), is(orderId));
//    assertThat(order.getCustomerName(), is(customer.customerName));
//    assertThat(order.getStatus(), is(OrderStatus.PLACED));
//    assertThat(order.getOrderAmount(), is(1000L));
  }

  @Test
  public void testGetOrders() throws InterruptedException {
//    int initialSize = getOrders().size();
//
//    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
//
//    OrderId orderId1 = addPlaceOrder(customer);
//    OrderId orderId2 = addPlaceOrder(customer);
//
//    Thread.sleep(500);
//
//    Collection<OrderProjection> orders = getOrders();
//    assertThat(orders.size(), is(initialSize + 2));
//
//    Iterator<OrderProjection> iterator = orders.iterator();
//    assertThat(iterator.next().getOrderId(), is(orderId2));
//    assertThat(iterator.next().getOrderId(), is(orderId1));
  }

  @Test
  public void testActivateOrder() throws Exception {
//    String publisherContractId = UUID.randomUUID().toString();
//    registerContract(publisherContractId, "Addison-Wesley", 10.0, 1000);
//
//    CustomerInformation customer = new CustomerInformation("John Doe", "john@acme.com", "Highway street 1");
//    OrderId orderId = addPlaceOrder(customer);
//
//    // TODO: Add await instead of sleep?
//    Thread.sleep(200);
//
//    activateOrder(orderId);
//
//    Thread.sleep(200);
//
//    OrderProjection order = getOrder(orderId);
//    assertThat(order.getStatus(), is(ACTIVATED));
  }

}
