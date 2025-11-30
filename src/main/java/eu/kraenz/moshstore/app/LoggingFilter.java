package eu.kraenz.moshstore.app;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {
  private final Logger ingressLogger = LoggerFactory.getLogger("ReqIn");
  private final Logger egressLogger = LoggerFactory.getLogger("ResOut");

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var start = Instant.now().toEpochMilli();
    ingressLogger.info("{} {}", request.getMethod(), request.getRequestURI());
    filterChain.doFilter(request, response);
    var end = Instant.now().toEpochMilli();
    var deltaTime = end - start;
    egressLogger.info(
        "{} {} - {} in {} ms",
        request.getMethod(),
        request.getRequestURI(),
        response.getStatus(),
        deltaTime);
  }
}
