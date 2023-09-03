package com.lwk.wochat.account.aspect;


import com.lwk.wochat.account.utils.StringConstant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 对 {@link com.lwk.wochat.api.clients.RedisClient} 所有的 key 添加前缀 "accountService:"
 */
@Aspect
@Deprecated
@Component
public class RedisClientKeyPrefixAspect {
    @Pointcut(value = "execution(public * com.lwk.wochat.api.clients.RedisClient.*(..))")
    public void allMethodPointcut() {}

    @Around(value = "allMethodPointcut()")
    public Object addKeyPrefixAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String[] argsNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String && "key".equals(argsNames[i])) {
                args[i] = StringConstant.ACCOUNT_REDIS_KEY_PREFIX + args[i];

                break;
            }
        }
        return joinPoint.proceed(args);
    }
}
