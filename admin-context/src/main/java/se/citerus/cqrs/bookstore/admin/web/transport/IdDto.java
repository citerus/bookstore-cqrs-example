package se.citerus.cqrs.bookstore.admin.web.transport;

import se.citerus.cqrs.bookstore.TransportObject;

import java.util.UUID;

public class IdDto extends TransportObject {

  public String id;

  public static IdDto random() {
    IdDto idDto = new IdDto();
    idDto.id = UUID.randomUUID().toString();
    return idDto;
  }
}
