package se.citerus.cqrs.bookstore.admin.web.transport;

public class OrderLine {

  public IdDto bookId;
  public String title;
  public int quantity;
  public long unitPrice;
  public IdDto publisherContractId;

}
