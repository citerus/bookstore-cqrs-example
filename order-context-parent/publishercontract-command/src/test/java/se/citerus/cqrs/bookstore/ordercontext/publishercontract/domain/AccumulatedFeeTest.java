package se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AccumulatedFeeTest {

  @Test
  public void shouldCapFeeAtLimit() throws Exception {
    long limit = 100;
    long purchaseAmount = 1000;
    AccumulatedFee fee = new AccumulatedFee(0, limit, 10.0).addPurchase(purchaseAmount);
    assertThat(fee.lastPurchaseFee(), is(100L));
  }

  @Test
  public void shouldAddPurchaseFeeUpToLimit() throws Exception {
    long limit = 100;
    long purchaseAmount = 1000;
    AccumulatedFee fee = new AccumulatedFee(20, limit, 10.0).addPurchase(purchaseAmount);
    assertThat(fee.lastPurchaseFee(), is(80L));
  }

  @Test
  public void testMultiplePurchasesAddsUpToLimit() throws Exception {
    long limit = 10000;
    AccumulatedFee fee = new AccumulatedFee(0, limit, 10.0)
        .addPurchase(60000)
        .addPurchase(60000);

    assertThat(fee.accumulatedFee(), is(limit));
    assertThat(fee.lastPurchaseFee(), is(4000L));

    AccumulatedFee limitAlreadyReached = fee.addPurchase(60000);
    assertThat(limitAlreadyReached.accumulatedFee(), is(limit));
    assertThat(limitAlreadyReached.lastPurchaseFee(), is(0L));
  }

  @Test
  public void shouldAddMultiplyPurchaseWithPercentage() throws Exception {
    long limit = 1000;
    long purchaseAmount = 1000;
    AccumulatedFee fee = new AccumulatedFee(200, limit, 10.0).addPurchase(purchaseAmount);
    assertThat(fee.lastPurchaseFee(), is(100L));
    assertThat(fee.accumulatedFee(), is(300L));
  }

  @Test(expected = IllegalArgumentException.class)
  public void feeCannotBeNegative() throws Exception {
    new Fee(-1);
  }

  @Test
  public void shouldReturnZeroIfLimitIsExceededBeforePurchase() throws Exception {
    long limit = 100L;
    long purchaseAmount = 101;
    long currentFee = limit;
    AccumulatedFee fee = new AccumulatedFee(currentFee, limit, 10.0).addPurchase(purchaseAmount);
    assertThat(fee.lastPurchaseFee(), is(0L));
    assertThat(fee.accumulatedFee(), is(limit));
  }

}