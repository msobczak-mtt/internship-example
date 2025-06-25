package stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import stock.dto.TransactionEventDto;
import stock.model.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/transaction-events")
@RequiredArgsConstructor
@Slf4j
public class TransactionEventController {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> {
            synchronized (emitters) {
                emitters.remove(emitter);
            }
            log.info("SSE connection closed");
        });

        emitter.onTimeout(() -> {
            synchronized (emitters) {
                emitters.remove(emitter);
            }
            log.info("SSE connection timed out");
        });

        emitter.onError(e -> {
            synchronized (emitters) {
                emitters.remove(emitter);
            }
            log.error("SSE error: {}", e.getMessage());
        });

        synchronized (emitters) {
            emitters.add(emitter);
        }

        log.info("New SSE connection established");
        return emitter;
    }

    public void sendTransactionEvent(Transaction transaction) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        TransactionEventDto eventDto = TransactionEventDto.fromTransaction(transaction);

        emitters.forEach(emitter -> {
            try {
                emitter.send(eventDto, MediaType.APPLICATION_JSON);
                log.info("Transaction event sent: {}", eventDto);
            } catch (IOException e) {
                deadEmitters.add(emitter);
                log.error("Error sending transaction event: {}", e.getMessage());
            }
        });

        emitters.removeAll(deadEmitters);
    }
}
