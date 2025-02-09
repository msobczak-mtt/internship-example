package travel.impl;


import travel.Accomodation;
import travel.Person;


import java.util.List;


public class Hotel implements Accomodation {

    private List<String> meals;

    public void setMeals(List<String> meals) {
        this.meals = meals;
    }

    @Override
    public void host(Person p) {
        System.out.println("person " + p + " is being hosted in hotel. meal: " + meals);
    }
}
