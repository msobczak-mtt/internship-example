package travel.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.event.EventListener;
import travel.Transportation;

import java.util.List;

@Configuration
@ComponentScan("travel")
// @PropertySource("classpath:/application.properties")
@EnableAspectJAutoProxy
public class TravelConfig {

    @Bean
    String travelName(){
        return "Summer Holiday 2025";
    }

    @Bean
    List<String> meals(@Value("${meals.gratis:water}") String mealsGratis){
        return List.of("ramen", "sushi", "sake", mealsGratis);
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
