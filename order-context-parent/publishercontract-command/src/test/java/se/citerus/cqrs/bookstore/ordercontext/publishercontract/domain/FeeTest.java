package se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain.Fee.calculatePurchaseFee;

public class FeeTest {

  @Test
  public void shouldCapFeeAtLimit() throws Exception {
    double limit = 100;
    double purchaseAmount = 1000;
    Fee fee = calculatePurchaseFee(purchaseAmount, limit, 10.0, Fee.ZERO);
    assertThat(fee.feeAmount(), is(100.0));
  }

  @Test
  public void shouldAddPurchaseFeeUpToLimit() throws Exception {
    double limit = 100;
    double purchaseAmount = 1000;
    Fee fee = calculatePurchaseFee(purchaseAmount, limit, 10.0, new Fee(20));
    assertThat(fee.feeAmount(), is(80.0));
  }

  @Test
  public void shouldAddMultiplyPurchaseWithPercentage() throws Exception {
    double limit = 100;
    double purchaseAmount = 100;
    Fee fee = calculatePurchaseFee(purchaseAmount, limit, 10.0, new Fee(20));
    assertThat(fee.feeAmount(), is(30.0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void feeCannotBeNegative() throws Exception {
    new Fee(-1);
  }

  @Test
  public void shouldReturnZeroIfLimitIsExceededBeforePurchase() throws Exception {
    double limit = 100;
    double purchaseAmount = 101;
    Fee fee = calculatePurchaseFee(purchaseAmount, limit, 5.5, new Fee(1000));
    assertThat(fee.feeAmount(), is(0.0));
  }


}