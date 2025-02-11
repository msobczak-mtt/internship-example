package travel.config;

import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import travel.Person;

@ToString
public abstract class TravelEvent extends ApplicationEvent {

    private final Person person;

    public TravelEvent(Object source, Person person) {
        super(source);
        this.person = person;
    }
}
