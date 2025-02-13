package vod.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TimeWindowInterceptor implements HandlerInterceptor {

    @Value("${vod.opening:10}")
    private int openingTime;

    @Value("${vod.closing:15}")
    private int closingTime;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        int currentHour = LocalTime.now().getHour();

        if(currentHour < openingTime || currentHour >= closingTime) {
            log.warn("service unavailable, current hout: {}, opening {}, closing {}", currentHour, openingTime, closingTime);
            response.setHeader("vod-current-hour", currentHour + "");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return false;
        } else {
            log.info("service available, current hout: {}, opening {}, closing {}", currentHour, openingTime, closingTime);
            return true;
        }

    }
}
