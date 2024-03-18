package me.gnoyes.couponcore.repository.mysql;

import me.gnoyes.couponcore.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {
}
