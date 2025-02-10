package travel;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import travel.config.TravelConfig;
import travel.impl.Bus;
import travel.impl.Hotel;

import java.time.LocalDate;

import org.springframework.context.ApplicationContext;

public class TravelMain {

    public static void main(String[] args) {

        Person doe = new Person("Joe", "Doe", new Ticket(LocalDate.now()));

        // service preparation
  /*      Transportation transportation = new Bus();
        Accomodation accomodation = new Hotel();
        String travelName = "Holiday 2023";

        Travel travel = new Travel();
        travel.setName(travelName);
        travel.setTransportation(transportation);
        travel.setAccomodation(accomodation);*/

        ApplicationContext context = new AnnotationConfigApplicationContext(TravelConfig.class);
                //new ClassPathXmlApplicationContext("applicationContext.xml.bak");
        Travel travel = (Travel)context.getBean("travel");

        // service use
        travel.travel(doe);

        System.out.println("done.");
    }
}
