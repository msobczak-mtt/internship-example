package travel.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import travel.Person;
import travel.Transportation;

@Component
@Qualifier("cheap")
public class Bus implements Transportation {
    @Override
    public void transport(Person p) {
        System.out.println("person '" + p + "' is being transported by bus");
    }

}
