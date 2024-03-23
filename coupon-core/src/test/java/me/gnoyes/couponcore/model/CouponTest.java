package me.gnoyes.couponcore.model;

import me.gnoyes.couponcore.exception.CouponIssueException;
import me.gnoyes.couponcore.exception.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("쿠폰 발급 최대 수량의 여유가 있다면 true를 반환한다")
    void availableIssueQuantity_1() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();

        // then
        Assertions.assertTrue(result);
    }


    @Test
    @DisplayName("쿠폰 발급 최대 수량의 모두 소진되었다면 false를 반환한다")
    void availableIssueQuantity_2() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();

        // then
        Assertions.assertFalse(result);
    }


    @Test
    @DisplayName("쿠폰 발급 최대 수량이 설정되지 않았다면 true를 반환한다")
    void availableIssueQuantity_3() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(99)
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();

        // then
        Assertions.assertTrue(result);
    }


    @Test
    @DisplayName("쿠폰 발급 기간이 아니라면 false를 반환한다")
    void availableIssueDate_1() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .dateIssueStart(now.plusDays(1))
                .dateIssueEnd(now.plusDays(2))
                .build();

        // when
        boolean result = coupon.availableIssueDate();

        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("쿠폰 발급 기간이라면 true를 반환한다")
    void availableIssueDate_2() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .dateIssueStart(now.minusDays(1))
                .dateIssueEnd(now.plusDays(2))
                .build();

        // when
        boolean result = coupon.availableIssueDate();

        // then
        Assertions.assertTrue(result);
    }


    @Test
    @DisplayName("쿠폰 발급 기간이 종료되었다면 false를 반환한다")
    void availableIssueDate_3() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .dateIssueStart(now.minusDays(2))
                .dateIssueEnd(now.minusDays(1))
                .build();

        // when
        boolean result = coupon.availableIssueDate();

        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("발급 수량과 발급 기간이 유효하다면 발급에 성공한다")
    void issue_1() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(now.minusDays(1))
                .dateIssueEnd(now.plusDays(2))
                .build();

        // when
        coupon.issue();

        // then
        Assertions.assertEquals(coupon.getIssuedQuantity(), 100);
    }

    @Test
    @DisplayName("발급 수량을 초과하면 예외를 반환한다")
    void issue_2() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(now.minusDays(1))
                .dateIssueEnd(now.plusDays(2))
                .build();

        // when & then
        CouponIssueException exception = assertThrows(CouponIssueException.class, coupon::issue);
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
    }


    @Test
    @DisplayName("발급 기간이 아니라면 예외를 반환한다")
    void issue_3() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(now.plusDays(1))
                .dateIssueEnd(now.plusDays(2))
                .build();

        // when & then
        CouponIssueException exception = assertThrows(CouponIssueException.class, coupon::issue);
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }

    @Test
    @DisplayName("발급 기간이 종료되면 true를 반환한다")
    void isIssueComplete_1() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .dateIssueStart(now.minusDays(2))
                .dateIssueEnd(now.minusDays(1))
                .build();

        // when
        boolean result = coupon.isIssueComplete();

        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("잔여 발급 가능 수량이 없다면 true를 반환한다")
    void isIssueComplete_2() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(now.minusDays(2))
                .dateIssueEnd(now.plusDays(10))
                .build();

        // when
        boolean result = coupon.isIssueComplete();

        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급 기한과 수량이 유효하면 false를 반환한다")
    void isIssueComplete_3() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(now.minusDays(2))
                .dateIssueEnd(now.plusDays(10))
                .build();

        // when
        boolean result = coupon.isIssueComplete();

        // then
        Assertions.assertFalse(result);
    }
}