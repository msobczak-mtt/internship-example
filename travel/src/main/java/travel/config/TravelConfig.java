package travel.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import travel.Transportation;
import travel.impl.Kitchen;

import java.util.List;

@Configuration
@ComponentScan("travel")
public class TravelConfig {

    @Bean
    String travelName(){
        return "Summer Holiday 2025";
    }

    @Bean
    List<String> mealsOfList(){
        return List.of("ramen", "sushi", "sake");
    }

    @Bean
    FactoryBean<List<String>> meals(){
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
