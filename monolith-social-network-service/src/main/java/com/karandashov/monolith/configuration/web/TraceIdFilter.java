package com.karandashov.monolith.configuration.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class TraceIdFilter implements Filter {

    public static final String MDC_TRACE_ID = "traceId";

    public static final String HEADER_TRACE_ID = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        String traceId = request.getHeader(HEADER_TRACE_ID);

        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString();
        }

        MDC.put(MDC_TRACE_ID, traceId);
        log.debug("Inject traceId: {}", traceId);

        chain.doFilter(req, res);
    }
}