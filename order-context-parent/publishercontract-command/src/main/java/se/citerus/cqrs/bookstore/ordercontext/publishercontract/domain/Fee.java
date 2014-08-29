package se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

public class Fee extends ValueObject {

  public static final Fee ZERO = new Fee(BigDecimal.ZERO);
  private static final BigDecimal PER_CENT_DIVISOR = new BigDecimal(100);

  private final BigDecimal feeAmount;

  public Fee(double feeAmount) {
    this(new BigDecimal(feeAmount));
  }

  private Fee(BigDecimal feeAmount) {
    checkArgument(feeAmount.compareTo(BigDecimal.ZERO) >= 0, "Fee cannot be negative");
    this.feeAmount = feeAmount;
  }

  public static Fee calculatePurchaseFee(double purchaseAmount, double limit, double feePercentage, Fee currentFee) {
    return currentFee.calculateNextPurchaseFee(purchaseAmount, limit, feePercentage);
  }

  private Fee diff(double limit) {
    return new Fee(new BigDecimal(limit).subtract(feeAmount));
  }

  public Fee add(Fee fee) {
    return new Fee(feeAmount.add(fee.feeAmount));
  }

  public Fee calculateNextPurchaseFee(double purchaseAmount, double limit, double feePercentage) {
    if (exceeds(limit)) {
      return new Fee(0);
    }
    BigDecimal percentageMultiplier = new BigDecimal(feePercentage).divide(PER_CENT_DIVISOR);
    Fee newFee = new Fee(feeAmount.add(new BigDecimal(purchaseAmount).multiply(percentageMultiplier)));
    if (newFee.exceeds(limit)) {
      return diff(limit);
    } else {
      return newFee;
    }
  }

  private boolean exceeds(double limit) {
    return feeAmount.compareTo(new BigDecimal(limit)) > 0;
  }

  public double feeAmount() {
    return feeAmount.doubleValue();
  }
}
