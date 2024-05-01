## 👨‍👩‍👦 팀원 정보

- [문다훈](https://github.com/ekgns33) 202011288, Server
- [홍승택](https://github.com/redcarrot1) 201911294, Server
- [황재상](https://github.com/jxx-sx) 201911297, Client

## 🛠️ 구현환경

- Client
    - JDK 17
    - IntelliJ IDEA
    - Swing
    - 프레임워크 : Spring 6.1.5, Spring-boot 3.2.4
    - 외부 라이브러리
        - Spring Web
        - Spring WebSocket
- Server
    - JDK 17
    - IntelliJ IDEA
    - 프레임워크 : Spring 6.1.5, Spring-boot 3.2.4
    - 외부 라이브러리
        - Spring Web
        - Spring WebSocket
        - Spring Data Redis
        - Lombok
        - it.ozimov:embedded-redis
      
## ⚙️ 컴파일 및 실행 방법

- Client
    - **`DemoApplication` 클래스에 연결할 서버의 ip와 port가 작성되어 있다. 해당 정보를 적절하게 변경 후 실행해야 한다.**
        - Default : ip=117.16.137.190, port=8080
    - Gradle을 사용
        - Default : port=8090
        
        ```bash
        # project directory내에서 작성
        ./gradlew bootRun --args='--server.port={클라이언트가 실행될 포트번호}'
        ```
        
- Server
    - Embedded Redis 사용
        - Default : server.port=8080
        
        ```bash
        java -jar \
        -Dspring.profiles.active="embedded" \
        -Dserver.port="{value}" \
        Server/whiteboard-1.0.jar
        ```
        
    - Remote Redis 사용
        - 주의: 프로그램 시작 시, redis에 “chatroom” key를 가진 데이터는 모두 삭제됩니다.
        - Default : server.port=8080, redis.host=localhost, redis.port=6379, no password
        
        ```bash
        java -jar \
        -Dserver.port="{value}" \
        -Dspring.data.redis.host="{value}" \
        -Dspring.data.redis.port="{value}" \
        -Dspring.data.redis.password="{value}" \
        Server/whiteboard-1.0.jar
        ```

## ✉️ Message Architecture
<img width="600" alt="Message Architecture" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/df2d9b46-06d3-445c-b26b-127b3ebdc3c5">

![login_sequence](https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/1d4dfadc-c28c-4832-b764-a4c2dcac8004)

## 📹 기능별 시연 영상 youtube 링크
[협동분산시스템 2팀 중간 기능 시연 영상](https://youtu.be/4OCEsDhF3GI)
