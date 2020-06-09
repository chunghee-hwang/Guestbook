# 방명록

## 설명
Spring Boot, Spring MVC, Spring JDBC로 Layered Architecture를 구현한 방명록 프로젝트입니다.<br>
view는 Server side rendering을 이용하는 thymeleaf를 이용했습니다.<br>
이 프로젝트는 부스트 코스의 다음 내용을 참고하고 구현했습니다.
* [레이어드 아키텍처 실습1](https://www.edwith.org/boostcourse-web/lecture/16767/)
* [레이어드 아키텍처 실습2](https://www.edwith.org/boostcourse-web/lecture/16772/)
* [RestController를 이용하여 web api 작성하기](https://www.edwith.org/boostcourse-web/lecture/16774/)
* [쿠키를 이용한 상태정보 유지하기](https://www.edwith.org/boostcourse-web/lecture/16800/)
* [Spring MVC에서 Session사용하기](https://www.edwith.org/boostcourse-web/lecture/16803/)
* [인터셉터를 이용해 Controller 공통 로직 처리하기](https://www.edwith.org/boostcourse-web/lecture/16805/)
* [아규먼트 리졸버를 이용해 HTTP Header정보를 Map객체에 담아서 Controller에게 전달하기](https://www.edwith.org/boostcourse-web/lecture/16807/)
* [slf4j를 이용한 로그남기기](https://www.edwith.org/boostcourse-web/lecture/16815/)

## 스크린샷

### Controller를 이용한 방명록
<img src="./screenshot/controller_screenshot.png" alt="Controller를 이용한 결과 사진"></img>

### RestController를 이용한 방명록
<img src="./screenshot/restcontroller_screenshot.png" alt="RestController를 이용한 결과 사진"></img>

### 쿠키를 이용한 재방문 여부 확인
#### 첫방문 시
<img src="./screenshot/visitfirst.png" alt="첫 방문시"></img><br>
쿠키의 value가 false인 이유는,<br> 첫 방문시 value가 true였던 값을 먼저 model에 attribute로 추가한 뒤,<br>
쿠키의 value를 false로 바꿔서 재전송하기 때문이다.

#### 재방문 시 
<img src="./screenshot/visitagain.png" alt="재방문시"></img>
