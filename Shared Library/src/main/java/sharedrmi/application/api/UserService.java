package sharedrmi.application.api;

import sharedrmi.application.exceptions.UserNotFoundException;

import javax.ejb.Remote;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Remote
public interface UserService extends Serializable  {

    List<String> getAllTopics();

    List<String> getSubscribedTopicsForUser(String username);

    void changeLastViewed(String username, LocalDateTime lastViewed) throws UserNotFoundException;

    LocalDateTime getLastViewedForUser(String username) throws UserNotFoundException;

    boolean subscribe(String topic, String username);

    boolean unsubscribe(String topic, String username);
}
