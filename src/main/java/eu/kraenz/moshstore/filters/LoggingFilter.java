package eu.kraenz.moshstore.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var start = Instant.now().toEpochMilli();
    System.out.println("In: %s %s".formatted(request.getMethod(), request.getRequestURI()));
    filterChain.doFilter(request, response);
    var end = Instant.now().toEpochMilli();
    var deltaTime = end - start;
    var requestLog =
        "Out: %s %s - %d - %d ms"
            .formatted(
                request.getMethod(), request.getRequestURI(), response.getStatus(), deltaTime);
    System.out.println(requestLog);
  }
}
