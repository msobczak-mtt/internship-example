package travel;

import lombok.*;

//@ToString(exclude = "ticket")
//@Getter
//@RequiredArgsConstructor
@Data
public class Person {

    private final String firstName;
    private final String lastName;
    private Ticket ticket;

 /*   public Person(String firstName, String lastName, Ticket ticket) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ticket = ticket;
    }*/

   /* public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Ticket getTicket() {
        return ticket;
    }
*/
    /*@Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }*/
}
