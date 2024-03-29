package me.gnoyes.couponcore.service;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponcore.exception.CouponIssueException;
import me.gnoyes.couponcore.repository.redis.RedisRepository;
import me.gnoyes.couponcore.repository.redis.dto.CouponRedisEntity;
import org.springframework.stereotype.Service;

import static me.gnoyes.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;
import static me.gnoyes.couponcore.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;
import static me.gnoyes.couponcore.util.CouponRedisUtils.getIssueRequestKey;

@RequiredArgsConstructor
@Service
public class CouponIssueRedisService {

    private final RedisRepository redisRepository;

    public boolean isAvailableTotalIssueQuantity(Integer totalQuantity, long couponId) {
        if (totalQuantity == null) {
            return true;
        }
        String key = getIssueRequestKey(couponId);
        return totalQuantity > redisRepository.sCard(key);
    }

    public boolean isAvailableUser(long couponId, long userId) {
        String key = getIssueRequestKey(couponId);
        return !redisRepository.sIsMember(key, String.valueOf(userId));
    }

    public void checkCouponIssueQuantity(CouponRedisEntity coupon, long userId) {
        if (!isAvailableTotalIssueQuantity(coupon.totalQuantity(), coupon.id())) {
            throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY, "발급 가능한 수량을 초과합니다. couponId : %s, userId : %s".formatted(coupon.id(), userId));
        }
        if (!isAvailableUser(coupon.id(), userId)) {
            throw new CouponIssueException(DUPLICATED_COUPON_ISSUE, "이미 처리된 발급 요청입니다. couponId : %s, userId : %s".formatted(coupon.id(), userId));
        }
    }
}
