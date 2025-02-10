package travel.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import travel.Accomodation;
import travel.Person;


import java.util.List;

@Component("accomodation")
public class Hotel implements Accomodation {

    @Autowired
    @Qualifier("meals")
    private List<String> meals;

    public void setMeals(List<String> meals) {
        this.meals = meals;
    }

    @Override
    public void host(Person p) {
        System.out.println("person " + p + " is being hosted in hotel. meal: " + meals);
    }
}
