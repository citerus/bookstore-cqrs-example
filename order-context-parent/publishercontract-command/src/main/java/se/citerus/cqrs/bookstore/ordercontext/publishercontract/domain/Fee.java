package se.citerus.cqrs.bookstore.ordercontext.publishercontract.domain;

import se.citerus.cqrs.bookstore.domain.ValueObject;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

public class Fee extends ValueObject {

  public static final Fee ZERO = new Fee(BigDecimal.ZERO);

  private final BigDecimal feeAmount;

  public Fee(double feeAmount) {
    this(new BigDecimal(feeAmount));
  }

  private Fee(BigDecimal feeAmount) {
    checkArgument(feeAmount.compareTo(BigDecimal.ZERO) >= 0, "Fee cannot be negative");
    this.feeAmount = feeAmount;
  }

  public BigDecimal feeAmount() {
    return feeAmount;
  }
}
