package vod.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class VodAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    List<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.error("argument not valid exception", exception);

        Locale locale = Locale.getDefault();

        return exception.getAllErrors().stream()
                .map(oe->messageSource.getMessage(oe.getCode(), oe.getArguments(), locale))
                .collect(Collectors.toList());
    }
}
