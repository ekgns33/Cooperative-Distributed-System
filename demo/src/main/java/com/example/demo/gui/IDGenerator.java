package com.example.demo.gui;

import java.util.concurrent.*;

public class IDGenerator {
	int curID, nextID;

	public IDGenerator() {
		// TODO: get ID from server
		curID = 0;
		nextID = 0;
	}

	public synchronized int getID() {
		int ret = curID;
		curID += 1;
		if ((curID & 1023) == 896)
			getNextID();
		if ((curID & 1023) == 0)
			curID = nextID;
		return ret;
	}

	private void getNextID() {
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			nextID += 1024;
		});
	}
}
