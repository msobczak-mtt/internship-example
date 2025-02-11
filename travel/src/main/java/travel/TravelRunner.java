package travel;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class TravelRunner implements CommandLineRunner {

    private final Travel travel;

    @Override
    public void run(String... args) throws Exception {

        Person doe = new Person("Joe", "Doe");
        doe.setTicket(new Ticket(LocalDate.now().plusDays(0)));

        travel.travel(doe);
    }
}
