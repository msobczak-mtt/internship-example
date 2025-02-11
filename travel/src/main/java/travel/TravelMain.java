package travel;

import org.springframework.context.ConfigurableApplicationContext;
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

        // try-with-resources
        try(ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(TravelConfig.class);) {

            Travel travel = (Travel) context.getBean("travel");

            // service use
            travel.travel(doe);
        }


        System.out.println("done.");
    }
}
