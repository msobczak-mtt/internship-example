package recommendation.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @GetMapping
    public Map<String, String> getRecommendations() {
        return Map.of("message", "Recommendation API is working!", "status", "OK");
    }
}