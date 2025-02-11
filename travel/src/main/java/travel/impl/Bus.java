package travel.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import travel.Person;
import travel.Transportation;
import travel.config.Cheap;

@Component
@Qualifier("cheap")
@Cheap
public class Bus implements Transportation {
    @Override
    public void transport(Person p) {
        System.out.println("person '" + p + "' is being transported by bus");
    }

}
