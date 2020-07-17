package pl.cichy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//przed javą 8 używamy tego interceptora (adaptera) z extends
//po javie 8+ używamy implements HandlerInterceptor i przeciążyć metody pre/post after compi
//jak zwraca true idziemy dalej z procesowaniem
@Component
public class LoggerInterceptor implements HandlerInterceptor {

    public static final Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final Object handler) throws Exception {

        logger.info("[preHandle]" + request.getMethod() + " " + request.getRequestURI());
        return true;
    }
}
