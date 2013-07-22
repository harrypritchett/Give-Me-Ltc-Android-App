package com.hpritch5.give_me_ltc;

// TODO Implement each worker of a user's account
public class MiningWorker {
	
	private String workerName;
	private int hashrate;
	private boolean alive;
	private String lastShareTimestamp;
	
	public MiningWorker(String workerName, int hashrate, boolean alive, String lastShareTimestamp) {
		this.workerName = workerName;
		this.hashrate = hashrate;
		this.alive = alive;
		this.lastShareTimestamp = lastShareTimestamp;
	}

	public String getWorkerName() {
		return workerName;
	}
	
	public int getHashrate() {
		return hashrate;
	}

	public boolean isAlive() {
		return alive;
	}

	public String getLastShareTimestamp() {
		return lastShareTimestamp;
	}
	
}
