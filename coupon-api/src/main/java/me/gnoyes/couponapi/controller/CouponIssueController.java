package me.gnoyes.couponapi.controller;

import lombok.RequiredArgsConstructor;
import me.gnoyes.couponapi.controller.dto.CouponIssueRequestDto;
import me.gnoyes.couponapi.controller.dto.CouponIssueResponseDto;
import me.gnoyes.couponapi.service.CouponIssueRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CouponIssueController {
    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/coupon")
    public CouponIssueResponseDto issueV1(@RequestBody CouponIssueRequestDto request) {
        couponIssueRequestService.issueRequestV1(request);
        return new CouponIssueResponseDto(true, null);
    }

    @PostMapping("/v2/coupon")
    public CouponIssueResponseDto asyncIssueV1(@RequestBody CouponIssueRequestDto request) {
        couponIssueRequestService.asyncIssueRequestV1(request);
        return new CouponIssueResponseDto(true, null);
    }

    @PostMapping("/v3/coupon")
    public CouponIssueResponseDto asyncIssueV2(@RequestBody CouponIssueRequestDto request) {
        couponIssueRequestService.asyncIssueRequestV2(request);
        return new CouponIssueResponseDto(true, null);
    }
}
