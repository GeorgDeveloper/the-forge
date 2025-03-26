package ru.georgdeveloper.myapp.aop.logging;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import tech.jhipster.config.JHipsterConstants;

/**
 * Аспект для логирования выполнения сервисных и репозиторных Spring компонентов.
 *
 * По умолчанию, работает только с профилем "dev".
 */
@Aspect
public class LoggingAspect {

    private final Environment env;

    public LoggingAspect(Environment env) {
        this.env = env;
    }

    /**
     *  Точка среза, которая соответствует всем репозиториям, сервисам и Web REST endpoints.
     */
    @Pointcut(
        "within(@org.springframework.stereotype.Repository *)" +
        " || within(@org.springframework.stereotype.Service *)" +
        " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {
        // Метод пустой, так как это просто точка среза, реализации находятся в advice-ах.
    }

    /**
     * Точка среза, которая соответствует всем Spring бинам в основных пакетах приложения.
     */
    @Pointcut(
        "within(ru.georgdeveloper.myapp.repository..*)" +
        " || within(ru.georgdeveloper.myapp.service..*)" +
        " || within(ru.georgdeveloper.myapp.web.rest..*)"
    )
    public void applicationPackagePointcut() {
        // Метод пустой, так как это просто точка среза, реализации находятся в advice-ах.
    }

    /**
     * Возвращает {@link Logger}, связанный с данной {@link JoinPoint}.
     *
     * @param joinPoint точка соединения, для которой нужен логгер.
     * @return {@link Logger}, связанный с данной {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    /**
     * Advice, который логирует методы, выбрасывающие исключения.
     *
     * @param joinPoint точка соединения для advice.
     * @param e исключение.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Profiles.of(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT))) {
            logger(joinPoint).error(
                "Исключение в {}() с причиной = '{}' и исключением = '{}'",
                joinPoint.getSignature().getName(),
                e.getCause() != null ? e.getCause() : "NULL",
                e.getMessage(),
                e
            );
        } else {
            logger(joinPoint).error(
                "Исключение в {}() с причиной = {}",
                joinPoint.getSignature().getName(),
                e.getCause() != null ? String.valueOf(e.getCause()) : "NULL"
            );
        }
    }

    /**
     * Advice, который логирует вход и выход из метода.
     *
     * @param joinPoint точка соединения для advice.
     * @return результат.
     * @throws Throwable выбрасывает {@link IllegalArgumentException}.
     */
    @Around("applicationPackagePointcut() && springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger log = logger(joinPoint);
        if (log.isDebugEnabled()) {
            log.debug("Вход: {}() с аргументом[ами] = {}", joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Выход: {}() с результатом = {}", joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Недопустимый аргумент: {} в {}()", Arrays.toString(joinPoint.getArgs()), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
