package se.citerus.cqrs.bookstore;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.regex.Pattern;

public class GenericId {

  public final String id;

  public static final String ID_PATTERN =
      "^(([0-9a-fA-F]){8}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){4}-([0-9a-fA-F]){12})$";

  private static final Pattern PATTERN = Pattern.compile(ID_PATTERN);

  public GenericId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public String toString() {
    return id;
  }

}

