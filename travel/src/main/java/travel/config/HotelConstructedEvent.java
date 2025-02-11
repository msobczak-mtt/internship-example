package travel.config;

import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@ToString
public class HotelConstructedEvent extends ApplicationEvent {

    private final List<String> meals;

    public HotelConstructedEvent(Object source, List<String> meals) {
        super(source);
        this.meals = meals;
    }
}
