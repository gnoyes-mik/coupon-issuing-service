package me.gnoyes.couponcore.component;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponcore.model.event.CouponIssueCompleteEvent;
import me.gnoyes.couponcore.service.CouponCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class CouponEventListener {

    private final CouponCacheService couponCacheService;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void issueComplete(CouponIssueCompleteEvent event) {
        log.info("issue complete. start to refresh coupon cache: %s".formatted(event.couponId()));
        couponCacheService.putCouponCache(event.couponId());
        couponCacheService.putCouponLocalCache(event.couponId());
        log.info("issue complete. end to refresh coupon cache: %s".formatted(event.couponId()));
    }
}
