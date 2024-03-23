package me.gnoyes.couponcore.service;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponcore.model.Coupon;
import me.gnoyes.couponcore.repository.redis.dto.CouponRedisEntity;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponCacheService {

    private final CouponIssueService couponIssueService;

    @Cacheable(cacheNames = "coupon")
    public CouponRedisEntity getCouponCache(long couponId) {
        Coupon coupon = couponIssueService.findCoupon(couponId);
        return new CouponRedisEntity(coupon);
    }

    @Cacheable(cacheNames = "coupon", cacheManager = "localCacheManager")
    public CouponRedisEntity getCouponLocalCache(long couponId) {
        return proxy().getCouponCache(couponId);
    }

    @CachePut(cacheNames = "coupon")
    public void putCouponCache(long couponId) {
        getCouponCache(couponId);
    }

    @CachePut(cacheNames = "coupon", cacheManager = "localCacheManager")
    public void putCouponLocalCache(long couponId) {
        getCouponLocalCache(couponId);
    }

    private CouponCacheService proxy() {
        return ((CouponCacheService) AopContext.currentProxy());
    }
}
