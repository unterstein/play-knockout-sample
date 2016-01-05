package elastic.repositories;

import elastic.models.GuestPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
public interface GuestPostRepository extends ElasticsearchRepository<GuestPost, String> {

  public List<GuestPost> findByAuthor(String author);
}
