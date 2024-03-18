# 선착순 쿠폰 발급 서비스(First come, First served)

## About

이 프로젝트는 유저에게 선착순으로 쿠폰을 발급해주는 기능이 구현되어 있다. <br>
한정된 수량의 쿠폰을 유저에게 '신속하고', '정확하게' 발급하기란 쉬운 일이 아니다. <br>
아무런 고려 없이 무작정 개발을 하다보면 필연적으로 '동시성 이슈' 또는 '성능 이슈'를 겪게 된다. <br>

## To do

이 프로젝트에서는 '동시성 이슈'와 '성능 이슈'를 개선해나가는 과정을 정리 한다. <br> 

## Project Setting

- Version Info
  - Java 17
  - SpringBoot 3.1.9
  - MySql 8.0.36
  - Redis 7.2.4

- Module Info
    - coupon-core : 쿠폰 발급 로직 
    - coupon-api : 쿠폰 발급 API 
    - coupon-consumer : 

- Test
  - Unit Test : JUnit 
  - API Test : Intellij Http Client
  - Perform Test : Locust


## 자세한 내용은 아래 Notion을 통해 확인 할 수 있습니다.

[Notion으로 보기](https://gnoyes.oopy.io/5cc38f49-1ea4-4f86-afe7-6a3f4b17cb88)