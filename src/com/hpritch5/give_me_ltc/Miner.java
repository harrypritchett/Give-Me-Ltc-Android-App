package com.hpritch5.give_me_ltc;


// Probably don't need this, but leaving for now.
public class Miner {
	
	private final String username;
	private final double confirmedRewards;
	private final double roundEstimate;
	private final int totalHashrate;
	private final double payoutHistory;
	private final int roundShares;
	
	public Miner(String username, double confirmedRewards, double roundEstimate,
			int totalHashrate, double payoutHistory, int roundShares) {
		this.username = username;
		this.confirmedRewards = confirmedRewards;
		this.roundEstimate = roundEstimate;
		this.totalHashrate = totalHashrate;
		this.payoutHistory = payoutHistory;
		this.roundShares = roundShares;
	}

	public String getUsername() {
		return username;
	}

	public double getConfirmedRewards() {
		return confirmedRewards;
	}

	public double getRoundEstimate() {
		return roundEstimate;
	}

	public double getTotalHashrate() {
		return totalHashrate;
	}
	public double getPayoutHistory() {
		return payoutHistory;
	}

	public int getRoundShares() {
		return roundShares;
	}
	
}
