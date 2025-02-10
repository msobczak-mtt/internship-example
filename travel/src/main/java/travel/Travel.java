package travel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Travel {

    private String name;
    private Transportation transportation;
    private Accomodation accomodation;

    //@Autowired
    public Travel(String name, Transportation transportation, Accomodation accomodation) {
        this.transportation = transportation;
        this.accomodation = accomodation;
        this.name = name;
        System.out.println("constructing travel object using parametrized constructor...");
    }

   /* public Travel() {
        System.out.println("constructing travel object using default constructor...");
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public void setAccomodation(Accomodation accomodation) {
        this.accomodation = accomodation;
    }

    public void travel(Person p){
        System.out.println("started travel '" + name + "' for a person " + p);
        transportation.transport(p);
        accomodation.host(p);
        transportation.transport(p);
    }

    @Override
    public String toString() {
        return "Travel{" +
                "name='" + name + '\'' +
                ", transportation=" + transportation +
                ", accomodation=" + accomodation +
                '}';
    }
}
