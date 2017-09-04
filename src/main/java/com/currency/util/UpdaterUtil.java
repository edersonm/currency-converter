package com.currency.util;

import java.util.concurrent.Semaphore;

public class UpdaterUtil {
	private static int updateTime = 1;
	private static int newRequestTime = 3;
	private static final Semaphore STOP_LIGHT_THREAD_CURRENCIES = new Semaphore(1);
	private static final Semaphore STOP_LIGHT_GO_TO_DB = new Semaphore(1);

	/**
     * Change the time, in minutes, on which the server is suppose to make request to the provider.
     * 
     * @param minutes minutes interval.
     * @return true if the procedure was executed correctly, false otherwise.
     */
	public static Boolean updateTimer(int minutes) {
		try {
			STOP_LIGHT_THREAD_CURRENCIES.acquire();
			if(minutes >= 1)
				updateTime = minutes;
			else 
				return false;
			return true;
		} catch (InterruptedException e) {
			return false;
		}finally {
		    STOP_LIGHT_THREAD_CURRENCIES.release();
        }
	}
	
	/**
     * Change the maximum interval of cache look-up to current time - seconds.ii
     * 
     * @param seconds value in seconds.
     * @return  true if the procedure was executed correctly, false otherwise.
     */
	public static Boolean updateRequestTimer(int seconds) {
		try {
			STOP_LIGHT_GO_TO_DB.acquire();
			newRequestTime = seconds;
			return true;
		} catch (InterruptedException e) {
			return false;
		}finally {
		    STOP_LIGHT_GO_TO_DB.release();
        }
	}
	
	/**
	 * @return the interval on which the assync requests to the provider are to be made.
	 */
	public static int getUpdateTime(){
		return updateTime;
	}
	
	/**
	 * @return the number of seconds in the past we can look on cache before create a new request to the currency provider.
	 */
	public static int getGoToDbTimer(){
		return newRequestTime;
	}
	
}
