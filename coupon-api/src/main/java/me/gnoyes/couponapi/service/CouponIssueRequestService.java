package me.gnoyes.couponapi.service;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponapi.controller.dto.CouponIssueRequestDto;
import me.gnoyes.couponcore.component.DistributeLockExecutor;
import me.gnoyes.couponcore.service.CouponIssueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponIssueRequestService {

    private final CouponIssueService couponIssueService;

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public void issueRequestV1(CouponIssueRequestDto requestDto) {
        couponIssueService.issue(requestDto.couponId(), requestDto.userId());
        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }
}
