package me.gnoyes.couponapi;

import me.gnoyes.couponcore.CouponCoreConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(CouponCoreConfiguration.class)
@SpringBootApplication
public class CouponApiApplication {

    public static void main(String[] args) {
        System.setProperty("Spring.config.name", "application-core,application-api");

        SpringApplication.run(CouponApiApplication.class, args);
    }

}
