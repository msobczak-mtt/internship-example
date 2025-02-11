package travel.config;

import travel.Person;

public class TravelFinishedEvent extends TravelEvent{
    public TravelFinishedEvent(Object source, Person person) {
        super(source, person);
    }
}
