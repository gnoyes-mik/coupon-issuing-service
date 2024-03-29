package me.gnoyes.couponcore.service;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponcore.exception.CouponIssueException;
import me.gnoyes.couponcore.model.Coupon;
import me.gnoyes.couponcore.model.CouponIssue;
import me.gnoyes.couponcore.model.event.CouponIssueCompleteEvent;
import me.gnoyes.couponcore.repository.mysql.CouponIssueJpaRepository;
import me.gnoyes.couponcore.repository.mysql.CouponIssueRepository;
import me.gnoyes.couponcore.repository.mysql.CouponJpaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static me.gnoyes.couponcore.exception.ErrorCode.COUPON_NOT_EXIST;
import static me.gnoyes.couponcore.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;

@RequiredArgsConstructor
@Service
public class CouponIssueService {
    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void issue(long couponId, long userId) {
        Coupon coupon = findCoupon(couponId);
        coupon.issue();
        saveCouponIssue(couponId, userId);
        // 쿠폰 수량만큼 발행 시 만료 이벤트룰 발행하여 추가적인 불필요 리소스를 줄인다.
        publishCouponIssueCompleteEvent(coupon);
    }

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId) {
        return couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponIssueException(COUPON_NOT_EXIST, "쿠폰 정책이 존재하지 않습니다. couponId: %s".formatted(couponId)));
    }

    public CouponIssue saveCouponIssue(long couponId, long userId) {
        checkAlreadyIssuance(couponId, userId);
        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();
        return couponIssueJpaRepository.save(issue);
    }

    private void checkAlreadyIssuance(long couponId, long userId) {
        CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);
        if (issue != null) {
            throw new CouponIssueException(DUPLICATED_COUPON_ISSUE, "이미 발급된 쿠폰입니다. userId: %s, couponId: %s".formatted(userId, couponId));
        }
    }

    private void publishCouponIssueCompleteEvent(Coupon coupon) {
        if (coupon.isIssueComplete()) {
            applicationEventPublisher.publishEvent(new CouponIssueCompleteEvent(coupon.getId()));
        }
    }
}
