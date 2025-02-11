package first;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirstRunner implements CommandLineRunner {

    private final FirstComponent firstComponent;

    @Override
    public void run(String... args) throws Exception {
        log.info("FirstComponent saying hello: {}", firstComponent.sayHello());
    }
}
