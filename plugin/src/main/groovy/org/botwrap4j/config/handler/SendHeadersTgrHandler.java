package org.botwrap4j.config.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.bot4j.telegram.message.MarkdownBuilder;
import org.bot4j.telegram.message.MessageFactory;
import org.bot4j.telegram.model.enums.TelegramIconMode;
import org.botwrap4j.common.BotWrap4j;
import org.botwrap4j.common.annotation.SendHeadersTgr;
import org.botwrap4j.service.BotAnnotationService;
import org.botwrap4j.service.TelegramWrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unify4j.common.Request4j;
import org.unify4j.common.String4j;

import java.lang.reflect.Method;
import java.util.Map;

@Aspect
@Component
public class SendHeadersTgrHandler {
    protected static final Logger logger = LoggerFactory.getLogger(SendHeadersTgrHandler.class);

    protected final TelegramWrapService telegramWrapService;
    protected final BotAnnotationService botAnnotationService;

    @Autowired
    public SendHeadersTgrHandler(TelegramWrapService telegramWrapService,
                                 BotAnnotationService botAnnotationService) {
        this.telegramWrapService = telegramWrapService;
        this.botAnnotationService = botAnnotationService;
    }

    @SuppressWarnings({"SpellCheckingInspection"})
    @Around(value = "@annotation(org.botwrap4j.common.annotation.SendHeadersTgr)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SendHeadersTgr annotation = method.getAnnotation(SendHeadersTgr.class);
        if (annotation.disabled()) {
            return proceed;
        }
        String key = botAnnotationService.resolveValue(annotation.key());
        String clusterID = botAnnotationService.resolveValue(annotation.clusterId());
        if (String4j.isEmpty(key)) {
            return proceed;
        }
        Map<String, Object> headers = Request4j.getHeaders(BotWrap4j.getRequest());
        MarkdownBuilder builder = MessageFactory.markdown();

        builder.icon(TelegramIconMode.BOT)
                .bold("BotWrap4j Header Extractor")
                .line(1)
                .icon(TelegramIconMode.CLOCK).timestamp()
                .line(2)
                .bold("SSID:").code(BotWrap4j.getCurrentSessionId())
                .line()
                .bold(BotWrap4j.getRequest().getMethod())
                .code(BotWrap4j.getRequest().getRequestURI())
                .line()
                .bold("H:").code(headers.size())
                .line();
        headers.forEach((k, v) -> {
            builder
                    .bold("::")
                    .code(k).text(":").code(v.toString()).line();
        });
        builder.line(1).tag("request", "header", "method");
        if (String4j.isNotEmpty(clusterID)) {
            BotWrap4j.telegramProvider().sendMessageSilent(key, clusterID, builder);
        } else {
            BotWrap4j.telegramProvider().sendMessageSilent(key, builder);
        }
        return proceed;
    }
}
