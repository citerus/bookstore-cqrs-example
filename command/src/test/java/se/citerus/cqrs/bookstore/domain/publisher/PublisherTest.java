package se.citerus.cqrs.bookstore.domain.publisher;

import org.junit.Test;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PublisherTest {

  @Test
  public void testRegisterPublisher() {
    Publisher publisher = new Publisher();
    PublisherId publisherId = PublisherId.randomId();
    double fee = 5.0;

    publisher.register(publisherId, "Addison Wesley", fee);

    assertThat(publisher.fee(), is(5.0));
    assertThat(publisher.name(), is("Addison Wesley"));
  }

  @Test
  public void testUpdateFee() {
    Publisher publisher = new Publisher();
    PublisherId publisherId = PublisherId.randomId();
    double initialFee = 5.0;
    publisher.register(publisherId, "Addison Wesley", initialFee);

    double newFee = 10.0;
    publisher.updateFee(newFee);

    assertThat(publisher.fee(), is(10.0));
  }

}
