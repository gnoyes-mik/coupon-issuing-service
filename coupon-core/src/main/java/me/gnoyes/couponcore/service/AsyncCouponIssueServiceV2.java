package me.gnoyes.couponcore.service;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponcore.repository.redis.RedisRepository;
import me.gnoyes.couponcore.repository.redis.dto.CouponRedisEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV2 {

    private final RedisRepository redisRepository;
    private final CouponCacheService couponCacheService;

    public void issue(long couponId, long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponCache(couponId);
        coupon.checkIssuableCoupon();
        issueRequest(couponId, userId, coupon.totalQuantity());
    }

    private void issueRequest(long couponId, long userId, Integer totalIssueQuantity) {
        if (totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
            return;
        }
        redisRepository.issueRequest(couponId, userId, totalIssueQuantity);
    }
}
