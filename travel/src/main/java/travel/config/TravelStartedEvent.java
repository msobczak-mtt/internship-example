package travel.config;

import travel.Person;

public class TravelStartedEvent extends TravelEvent {
    public TravelStartedEvent(Object source, Person person) {
        super(source, person);
    }
}
