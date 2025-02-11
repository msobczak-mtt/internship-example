package first;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FirstController {

    private final FirstComponent firstComponent;

    @RequestMapping(method= RequestMethod.GET, path = "/greetings")
    @ResponseBody String greetings(){
        log.info("Greetings from FirstController");
        return firstComponent.sayHello();
    }

}
