package travel;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import travel.config.Cheap;
import travel.config.ExecutionTime;
import travel.config.TravelFinishedEvent;
import travel.config.TravelStartedEvent;

@Component
@ToString
public class Travel {

    private final String name;
    private final Transportation transportation;
    private final Accomodation accomodation;
    private final ApplicationEventPublisher applicationEventPublisher;

    //@Autowired "${travel.name:secret travel}" Spring expression language = SpEL
    public Travel(@Value("#{'${travel.name:secret travel}'.toUpperCase()}") String name, @Cheap(true) Transportation transportation, Accomodation accomodation, ApplicationEventPublisher applicationEventPublisher) {
        this.transportation = transportation;
        this.accomodation = accomodation;
        this.name = name;
        this.applicationEventPublisher = applicationEventPublisher;
        System.out.println("constructing travel object using parametrized constructor...");
    }

   /* public Travel() {
        System.out.println("constructing travel object using default constructor...");
    }*/


    @ExecutionTime
    public void travel(Person p){
        System.out.println("started travel '" + name + "' for a person " + p);
        applicationEventPublisher.publishEvent(new TravelStartedEvent(this, p));
        transportation.transport(p);

        //p.setTicket(null);

        accomodation.host(p);
        transportation.transport(p);
        applicationEventPublisher.publishEvent(new TravelFinishedEvent(this, p));
    }

   /* @Override
    public String toString() {
        return "Travel{" +
                "name='" + name + '\'' +
                ", transportation=" + transportation +
                ", accomodation=" + accomodation +
                '}';
    }*/
}
