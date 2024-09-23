package org.botwrap4j.config.handler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.bot4j.telegram.message.MarkdownBuilder;
import org.bot4j.telegram.message.MessageFactory;
import org.bot4j.telegram.model.enums.TelegramIconMode;
import org.botwrap4j.common.BotWrap4j;
import org.botwrap4j.common.annotation.ExecutorSinceTgr;
import org.botwrap4j.service.BotAnnotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.unify4j.common.String4j;
import org.unify4j.common.Time4j;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class ExecutorSinceTgrHandler {
    protected static final Logger logger = LoggerFactory.getLogger(ExecutorSinceTgrHandler.class);

    protected final BotAnnotationService botAnnotationService;

    @Autowired
    public ExecutorSinceTgrHandler(BotAnnotationService botAnnotationService) {
        this.botAnnotationService = botAnnotationService;
    }

    @SuppressWarnings({"SpellCheckingInspection"})
    @Around(value = "@annotation(org.botwrap4j.common.annotation.ExecutorSinceTgr)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Date start = new Date();
        Object proceed = joinPoint.proceed();
        ExecutorSinceTgr annotation = method.getAnnotation(ExecutorSinceTgr.class);
        if (annotation.disabled()) {
            return proceed;
        }
        String key = botAnnotationService.resolveValue(annotation.key());
        String clusterID = botAnnotationService.resolveValue(annotation.clusterId());
        if (String4j.isEmpty(key)) {
            return proceed;
        }
        Date end = new Date();
        String since = Time4j.sinceSmallRecently(end, start);
        String clazz = joinPoint.getTarget().getClass().getSimpleName();
        MarkdownBuilder builder = MessageFactory.markdown();

        builder.icon(TelegramIconMode.BOT)
                .bold("BotWrap4j Executor Since")
                .icon(TelegramIconMode.CLOCK).code(since)
                .line(2)
                .bold("SSID:").code(BotWrap4j.getCurrentSessionId())
                .line()
                .bold("C:")
                .code(clazz)
                .line()
                .bold("MD:")
                .code(signature.getName())
                .line()
                .icon(TelegramIconMode.RIGHT_ARROW_1)
                .bold("RFT:")
                .timestamp(start)
                .line()
                .icon(TelegramIconMode.RIGHT_ARROW_1)
                .bold("RT:")
                .timestamp(end)
                .line()
        ;
        builder.line(1).tag("executed", "execution", "method");
        if (String4j.isNotEmpty(clusterID)) {
            BotWrap4j.telegramProvider().sendMessageSilent(key, clusterID, builder);
        } else {
            BotWrap4j.telegramProvider().sendMessageSilent(key, builder);
        }
        return proceed;
    }
}
