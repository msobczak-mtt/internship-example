package travel.config;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TravelEventListener implements ApplicationListener<TravelEvent> {

    @Override
    public void onApplicationEvent(TravelEvent event) {
        System.out.println("Travel event : " + event);
    }
}
