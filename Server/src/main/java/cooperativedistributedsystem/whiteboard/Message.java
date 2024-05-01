package cooperativedistributedsystem.whiteboard;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Message {
    int status; // 1: 입장, 2: 퇴장, 3: 메시지
    String nickname; // 유저 닉네임

    int type; // 도형 종류
    long time; // 생성 시간
    int id; // object id
    int lineWidth; // 선두께
    int fillColor; // 채우기 색상
    int drawColor; // 선 색상
    int x; // 좌측 x 좌표
    int y; // 상단 y 좌표
    int x2; // 우측 x 좌표
    int y2; // 하단 y 좌표
    String text; // 글자

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

/*
{
    "status": 3,
    "type":1,
    "time":123,
    "id":1,
    "lineWidth":1,
    "fillColor":1,
    "drawColor":1,
    "x":1,
    "y":1,
    "x2":1,
    "y2":1,
    "text":"test"
}
 */