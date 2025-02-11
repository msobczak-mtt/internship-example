package travel.impl;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import travel.Accomodation;
import travel.Person;


import java.util.List;

@Component
public class Hotel implements Accomodation {

    private List<String> meals;

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
    }

    @PreDestroy
    void preDestroy(){
        System.out.println("Destroying hotel bean");
    }

}
