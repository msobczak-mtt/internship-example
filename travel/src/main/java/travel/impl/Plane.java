package travel.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import travel.Person;
import travel.Transportation;
import travel.config.Cheap;

@Component
@Primary
@Cheap(value = false)
public class Plane implements Transportation {

    @Override
    public void transport(Person p) {
        System.out.println("person '" + p + "' is being transported by plane");
    }

    
}
