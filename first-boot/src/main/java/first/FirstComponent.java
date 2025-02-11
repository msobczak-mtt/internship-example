package first;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FirstComponent {

    private final String name;

    public FirstComponent(@Value("${first.name:Joe}") String name) {
        this.name = name;
    }

    @PostConstruct
    public void init() {
        log.info("first component initialized, name: {}", name);
    }

    public String sayHello(){
        return "Hey " + name;
    }
}
