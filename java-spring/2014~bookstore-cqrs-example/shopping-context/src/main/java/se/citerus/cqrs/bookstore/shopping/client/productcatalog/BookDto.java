package se.citerus.cqrs.bookstore.shopping.client.productcatalog;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import se.citerus.cqrs.bookstore.TransportObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDto extends TransportObject {

  public String title;

  public String isbn;

}
