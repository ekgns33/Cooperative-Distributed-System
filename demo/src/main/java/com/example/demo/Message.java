package com.example.demo;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static Message enterRoom(String nickname) {
        return new Message(1, nickname, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
    }

    public static Message exitRoom(String nickname) {
        return new Message(2, nickname, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, null);
    }

    public static Message figure(int type, int time, int id, int lineWidth, int fillColor, int drawColor, int x, int y, int x2, int y2) {
        return new Message(3, null, type, time, id, lineWidth, fillColor, drawColor, x, y, x2, y2, null);
    }

    public static Message text(int type, int time, int id, int lineWidth, int fillColor, int drawColor, int x, int y, int x2, int y2, String text) {
        return new Message(3, null, type, time, id, lineWidth, fillColor, drawColor, x, y, x2, y2, text);
    }
}

/*
{
    "status":3,
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