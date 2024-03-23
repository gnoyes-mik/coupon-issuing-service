package me.gnoyes.couponcore.service;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponcore.model.Coupon;
import me.gnoyes.couponcore.repository.redis.dto.CouponRedisEntity;
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
}
