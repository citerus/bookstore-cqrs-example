package se.citerus.cqrs.bookstore.domain.publisher;

import org.junit.Test;
import se.citerus.cqrs.bookstore.publisher.PublisherId;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PublisherContractTest {

  @Test
  public void testRegisterPublisher() {
    PublisherContract contract = new PublisherContract();
    PublisherId publisherId = PublisherId.randomId();
    double fee = 5.0;

    contract.register(publisherId, "Addison Wesley", fee);

    assertThat(contract.fee(), is(5.0));
    assertThat(contract.publisherName(), is("Addison Wesley"));
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotRegisterTwice() {
    PublisherContract contract = new PublisherContract();
    PublisherId publisherId = PublisherId.randomId();

    contract.register(publisherId, "Addison Wesley", 5.0);
    contract.register(publisherId, "Addison Wesley", 5.0);
  }

  @Test
  public void testUpdateFee() {
    PublisherContract contract = new PublisherContract();
    PublisherId publisherId = PublisherId.randomId();
    double initialFee = 5.0;
    contract.register(publisherId, "Addison Wesley", initialFee);

    double newFee = 10.0;
    contract.updateFee(newFee);

    assertThat(contract.fee(), is(10.0));
  }

}
