package elastic.services;

import elastic.repositories.GuestPostRepository;
import elasticplugin.ElasticPlugin;
import elasticplugin.ElasticServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Unterstein (unterstein@me.com)
 */
@Component
public class ElasticProvider extends ElasticServiceProvider {

  @Autowired
  public GuestPostRepository guestPostRepository;

  public static ElasticProvider get() {
    return ElasticPlugin.get();
  }
}