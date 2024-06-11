## 👨‍👩‍👦 팀원 정보

- [문다훈](https://github.com/ekgns33), 202011288, Server
- [홍승택](https://github.com/redcarrot1), 201911294, Server
- [황재상](https://github.com/jxx-sx), 201911297, Client

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
<img width="600" alt="Message Architecture" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/da95beb1-ce0f-4db5-a83c-75fa6eeb027c">

## 🔐 Object Lock
<img width="300" alt="object lock sequence" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/6588fef9-1c77-4441-bd0f-d448e0085fe2">
<br/>
<img width="600" alt="mechanism" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/0424bf23-507f-4a08-bef9-097d151344dd">

## 💾 Save and Load
<img width="300" alt="Save and Load sequence" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/f6d7f9fe-396e-4706-b67c-6547f201715e">
<br/>
<img width="600" alt="step1" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/ebfa04fb-9fe3-4322-8d2f-be21311a5703">
<br/>
<img width="600" alt="step2" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/cf88a2f1-2887-4d66-a3e1-2ae71e460edf">
<br/>
<img width="600" alt="step3" src="https://github.com/ekgns33/Cooperative-Distributed-System/assets/51076814/a7fcc48c-7eed-40b7-a62b-54ca99a2f6b0">


## 📹 기능별 시연 영상 youtube 링크
[중간 기능 시연 영상](https://youtu.be/4OCEsDhF3GI) <br/>
[기말 기능 시연 영상](https://www.youtube.com/watch?v=CPXYVDVkF8o)
