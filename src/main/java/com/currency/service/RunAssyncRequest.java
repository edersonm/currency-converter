package com.currency.service;

import com.currency.core.CurrencyException;
import com.currency.util.UpdaterUtil;

/**
 * @author ederson
 * Class responsible for assync calls to the provider in <b>UpdaterUtil.getUpdateTime()</b> seconds.
 */
public class RunAssyncRequest implements Runnable {
	private final CurrencyProcessorService service;
	private static boolean checkerActivated = false;

	/**
     * Default constructor to receive a DAO instance.
     * 
     * @param dao instance to be used.
     */
	public RunAssyncRequest(CurrencyProcessorService service) {
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 *
	 */
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(UpdaterUtil.getUpdateTime() * 1000 * 60);
				service.getCurrentCurrency();
			} catch (InterruptedException | CurrencyException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Starts the check thread if it was not started before.
	 */
	public void checker() {
		if (!checkerActivated)
			checkerActivated = true;
		else
			return;
		
		new Thread(this).start();
	}
}
