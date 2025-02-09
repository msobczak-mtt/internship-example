package travel;

import travel.impl.Bus;
import travel.impl.Hotel;

import java.time.LocalDate;

public class TravelMain {

    public static void main(String[] args) {

        Person doe = new Person("Joe", "Doe", new Ticket(LocalDate.now()));

        // service preparation
        Transportation transportation = new Bus();
        Accomodation accomodation = new Hotel();
        String travelName = "Holiday 2023";

        Travel travel = new Travel();
        travel.setName(travelName);
        travel.setTransportation(transportation);
        travel.setAccomodation(accomodation);

        // service use
        travel.travel(doe);

        System.out.println("done.");
    }
}
