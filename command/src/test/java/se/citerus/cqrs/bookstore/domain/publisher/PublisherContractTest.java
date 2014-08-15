package se.citerus.cqrs.bookstore.domain.publisher;

import org.junit.Test;
import se.citerus.cqrs.bookstore.book.BookId;
import se.citerus.cqrs.bookstore.publisher.PublisherContractId;
import se.citerus.cqrs.bookstore.publisher.event.PublisherRegisteredEvent;
import se.citerus.cqrs.bookstore.publisher.event.PurchaseRegisteredEvent;

import java.util.Iterator;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PublisherContractTest {

  private static final long LIMIT = 100_000;

  @Test
  public void testRegisterPublisher() {
    PublisherContract contract = new PublisherContract();
    PublisherContractId publisherContractId = PublisherContractId.randomId();
    double feePercentage = 5.0;

    contract.register(publisherContractId, "Addison Wesley", feePercentage, LIMIT);

    PublisherRegisteredEvent event = getOnlyElement(filter(contract.getUncommittedEvents(),
        PublisherRegisteredEvent.class));

    assertThat(event.feePercentage, is(5.0));
    assertThat(event.publisherName, is("Addison Wesley"));
  }

  @Test(expected = IllegalStateException.class)
  public void testCannotRegisterTwice() {
    PublisherContract contract = new PublisherContract();
    PublisherContractId publisherContractId = PublisherContractId.randomId();

    contract.register(publisherContractId, "Addison Wesley", 5.0, LIMIT);
    contract.register(publisherContractId, "Addison Wesley", 5.0, LIMIT);
  }

  @Test
  public void testRegisterPurchase() {
    PublisherContract contract = new PublisherContract();
    PublisherContractId publisherContractId = PublisherContractId.randomId();
    contract.register(publisherContractId, "Addison Wesley", 10.0, 100);

    contract.registerPurchase(BookId.randomId(), 600);
    contract.registerPurchase(BookId.randomId(), 600);
    contract.registerPurchase(BookId.randomId(), 600);

    Iterator<PurchaseRegisteredEvent> purchases = filter(contract.getUncommittedEvents(),
        PurchaseRegisteredEvent.class).iterator();
    assertThat(purchases.next().purchaseAmount, is(600L));
    assertThat(purchases.next().purchaseAmount, is(600L));
    assertThat(purchases.next().purchaseAmount, is(600L));

    // TODO: Verify fee!
  }

}
