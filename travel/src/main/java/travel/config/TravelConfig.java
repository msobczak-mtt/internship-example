package travel.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import travel.Transportation;
import travel.impl.Kitchen;

import java.util.List;

@Configuration
@ComponentScan("travel")
@PropertySource("classpath:/travel/travel.properties")
public class TravelConfig {

    @Bean
    String travelName(){
        return "Summer Holiday 2025";
    }

    @Bean
    List<String> meals(Environment environment){
        return List.of("ramen", "sushi", "sake", environment.getProperty("meals.gratis"));
    }

    @Bean
    FactoryBean<List<String>> mealsFromKitchen(){
        return new Kitchen();
    }

    @Bean
    Transportation teleportation(){
        return passenger -> System.out.println("teleporting passenger " + passenger);
    }

    @EventListener(TravelEvent.class)
    void onTravelEvent(TravelEvent travelEvent){
        System.out.println("[annotated method] Travel event: " + travelEvent);
    }


}
