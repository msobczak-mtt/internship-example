package travel.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import travel.Transportation;
import travel.impl.Kitchen;

import java.util.List;

@Configuration
@ComponentScan("travel")
@Slf4j
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



}
