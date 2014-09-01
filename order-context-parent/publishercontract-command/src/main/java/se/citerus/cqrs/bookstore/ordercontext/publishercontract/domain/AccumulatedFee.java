package se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

import java.math.BigDecimal;

public class AccumulatedFee extends ValueObject {

  private static final BigDecimal PER_CENT_DIVISOR = new BigDecimal(100);
  private static final BigDecimal TO_FROM_CENTS = new BigDecimal(100);

  private final BigDecimal accumulatedFee;
  private final BigDecimal limit;
  private final BigDecimal percentageMultiplier;
  private final BigDecimal feePercentage;
  private final Fee lastPurchaseFee;

  public AccumulatedFee(long accumulatedFee, long limit, double feePercentage) {
    this(new BigDecimal(accumulatedFee).divide(TO_FROM_CENTS), new BigDecimal(limit).divide(TO_FROM_CENTS),
        new BigDecimal(feePercentage), Fee.ZERO);
  }

  public AccumulatedFee(BigDecimal accumulatedFee, BigDecimal limit, BigDecimal feePercentage, Fee lastPurchaseFee) {
    this.accumulatedFee = accumulatedFee;
    this.limit = limit;
    this.feePercentage = feePercentage;
    this.percentageMultiplier = feePercentage.divide(PER_CENT_DIVISOR);
    this.lastPurchaseFee = lastPurchaseFee;
  }

  public AccumulatedFee addPurchase(long purchaseAmount) {
    if (limitReached()) {
      return new AccumulatedFee(accumulatedFee, limit, feePercentage, Fee.ZERO);
    } else {
      return getFee(purchaseAmount);
    }
  }

  private AccumulatedFee getFee(long purchaseAmount) {
    BigDecimal feeAmount = new BigDecimal(purchaseAmount).divide(TO_FROM_CENTS).multiply(percentageMultiplier);
    BigDecimal newAccumulatedFee = accumulatedFee.add(feeAmount);
    if (exceedsLimit(newAccumulatedFee)) {
      return new AccumulatedFee(limit, limit, feePercentage, new Fee(limit.subtract(accumulatedFee).doubleValue()));
    } else {
      return new AccumulatedFee(newAccumulatedFee, limit, feePercentage, new Fee(newAccumulatedFee.subtract(accumulatedFee).doubleValue()));
    }
  }

  public long lastPurchaseFee() {
    return lastPurchaseFee.feeAmount().multiply(TO_FROM_CENTS).longValue();
  }

  public long accumulatedFee() {
    return accumulatedFee.multiply(TO_FROM_CENTS).longValue();
  }

  private boolean exceedsLimit(BigDecimal add) {
    return add.compareTo(limit) >= 0;
  }

  private boolean limitReached() {
    return accumulatedFee.compareTo(limit) >= 0;
  }

}
