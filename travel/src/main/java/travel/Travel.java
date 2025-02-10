package travel;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
@RequiredArgsConstructor
public class Travel {

    private final String name;
    private final Transportation transportation;
    private final Accomodation accomodation;

    //@Autowired
  /*  public Travel(String name, Transportation transportation, Accomodation accomodation) {
        this.transportation = transportation;
        this.accomodation = accomodation;
        this.name = name;
        System.out.println("constructing travel object using parametrized constructor...");
    }*/

   /* public Travel() {
        System.out.println("constructing travel object using default constructor...");
    }*/


    public void travel(Person p){
        System.out.println("started travel '" + name + "' for a person " + p);
        transportation.transport(p);
        accomodation.host(p);
        transportation.transport(p);
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
