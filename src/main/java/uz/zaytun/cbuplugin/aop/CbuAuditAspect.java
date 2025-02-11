package uz.zaytun.cbuplugin.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.zaytun.cbuplugin.domain.data.RequestLog;
import uz.zaytun.cbuplugin.domain.data.ResponseLog;
import uz.zaytun.cbuplugin.repository.RequestLogRepository;
import uz.zaytun.cbuplugin.repository.ResponseLogRepository;

import java.time.Instant;
import java.util.Enumeration;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CbuAuditAspect {

    private final ObjectMapper objectMapper;

    private final RequestLogRepository requestLogRepository;
    private final ResponseLogRepository responseLogRepository;

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object logRequests(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes
                ? ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
                : null;

        if (request == null) return joinPoint.proceed();

        String url = request.getRequestURI();
        String method = request.getMethod();
        String ip = request.getRemoteAddr();
        String queryParams = extractQueryParams(request);
        String requestBody = null;

        for (Object arg : joinPoint.getArgs()) {
            if (arg != null && arg.getClass().isAnnotationPresent(RequestBody.class)) {
                requestBody = objectMapper.writeValueAsString(arg);
            }
        }

        log.info("AOP request");
        log.info("url: {}", url);
        log.info("method: {}", method);
        log.info("ip: {}", ip);
        log.info("queryParams: {}", queryParams);
        log.info("requestBody: {}", requestBody);

        var requestLog = RequestLog.builder()
                .url(url)
                .method(method)
                .ip(ip)
                .queryParams(queryParams)
                .requestBody(requestBody)
                .createdDate(Instant.now())
                .build();

        requestLog = requestLogRepository.save(requestLog);

        Object response = joinPoint.proceed();

        String responseBody = objectMapper.writeValueAsString(response);
        log.info("AOP response");
        log.info("responseBody: {}", responseBody);

        var responseLog = ResponseLog.builder()
                .response(responseBody)
                .requestLog(requestLog)
                .createdDate(Instant.now())
                .build();
        responseLogRepository.save(responseLog);

        return response;
    }

    private String extractQueryParams(HttpServletRequest request) {
        StringBuilder queryParams = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            if (!queryParams.isEmpty()) {
                queryParams.append("&");
            }
            queryParams.append(paramName).append("=").append(paramValue);
        }
        return queryParams.toString();
    }
}
