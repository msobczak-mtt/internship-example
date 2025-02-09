package travel;

import java.time.LocalDate;

public class Ticket {

    private LocalDate valid;

    public Ticket(LocalDate valid) {
        this.valid = valid;
    }

    public LocalDate getValid() {
        return valid;
    }
}
