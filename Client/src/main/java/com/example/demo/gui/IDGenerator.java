package com.example.demo.gui;

import com.example.demo.stomp_client.EnterService;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class IDGenerator {
    int curID, nextID;
    String ip;
    int port;
    EnterService enterService;

    public IDGenerator(String ip, int port) throws IOException, InterruptedException {
        this.ip = ip;
        this.port = port;
        this.enterService = new EnterService();
        curID = enterService.reissueId(ip, port);
        nextID = -1;
    }

    public synchronized int getID() throws RuntimeException {
        int ret = curID;
        if (ret == -1)
            throw new RuntimeException();
        curID += 1;
        if ((curID & 1023) == 896)
            getNextID();
        if ((curID & 1023) == 0) {
            curID = nextID;
            nextID = -1;
        }
        return ret;
    }

    private void getNextID() {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            while (nextID == -1) {
                try {
                    nextID = enterService.reissueId(ip, port);
                } catch (Exception ignored) {
                }
            }
        });
    }
}
