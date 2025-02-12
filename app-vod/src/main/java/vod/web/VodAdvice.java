package vod.web;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class VodAdvice {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    private final MovieValidator movieValidator;

    @InitBinder("movieDto")
    void initBinder(WebDataBinder binder) {
        binder.addValidators(movieValidator);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    List<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        log.error("argument not valid exception", exception);

        Locale locale = localeResolver.resolveLocale(request);
                //new Locale("it", "IT");
                //Locale.getDefault();

        // messages_en_GB
        // messages_en
        // messages_<OS_LANG>_<OS_COUNTRY>
        // messages_<OS_LANG>
        // messages

        return exception.getAllErrors().stream()
                .map(oe->messageSource.getMessage(oe.getCode(), oe.getArguments(), locale))
                .collect(Collectors.toList());
    }
}
