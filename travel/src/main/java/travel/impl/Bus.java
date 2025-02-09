package travel.impl;

import travel.Person;
import travel.Transportation;


public class Bus implements Transportation {
    @Override
    public void transport(Person p) {
        System.out.println("person '" + p + "' is being transported by bus");
    }

}
