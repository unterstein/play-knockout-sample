package elastic.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
@Document(indexName = "guestposts", type = "post", shards = 1, replicas = 0)
public class GuestPost {

  @Id
  public String id;

  public String headline;

  public String text;

  public Date postedDate;

  public String author;

}
