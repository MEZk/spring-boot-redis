package io.github.mezk.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.persistence.EntityManagerFactory;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheMonitor {
    private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CacheMonitor.class);
    private final static NumberFormat NF = new DecimalFormat("0.0###");

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private CacheManager cacheManager;

    @Around("execution(* io.github.mezk.repository..*.*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        if (!LOG.isDebugEnabled()) {
            return pjp.proceed();
        }

        Statistics statistics = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        statistics.setStatisticsEnabled(true);

        long hit0 = statistics.getQueryCacheHitCount();
        long miss0 = statistics.getSecondLevelCacheMissCount();

        Object result = pjp.proceed();

        long hit1 = statistics.getQueryCacheHitCount();
        long miss1 = statistics.getQueryCacheMissCount();

        double ratio = (double) hit1 / (hit1 + miss1);

        if (hit1 > hit0) {
            LOG.debug(String.format("CACHE HIT; Ratio=%s; Signature=%s#%s()", NF.format(ratio), pjp.getTarget().getClass().getName(), pjp.getSignature().toShortString()));
        }
        else if (miss1 > miss0){
            LOG.debug(String.format("CACHE MISS; Ratio=%s; Signature=%s#%s()", NF.format(ratio), pjp.getTarget().getClass().getName(), pjp.getSignature().toShortString()));
        }
        else {
            LOG.debug("query cache not used");
        }

        return result;
    }
}