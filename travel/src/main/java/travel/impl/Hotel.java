package travel.impl;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import travel.Accomodation;
import travel.Person;
import travel.config.HotelConstructedEvent;


import java.util.List;

@Component
@RequiredArgsConstructor
public class Hotel implements Accomodation {

    private List<String> meals;

    private final ApplicationEventPublisher publisher;

    @Autowired
    public void setMeals(@Qualifier("meals") List<String> meals) {
        this.meals = meals;
    }

    @Override
    public void host(Person p) {
        System.out.println("person " + p + " is being hosted in hotel. meal: " + meals);
    }

    @PostConstruct
    void postConstruct(){
        System.out.println("Hotel constructed. Meals: " + meals);
        publisher.publishEvent(new HotelConstructedEvent(this, meals));

    }

    @PreDestroy
    void preDestroy(){
        System.out.println("Destroying hotel bean");
    }

}
