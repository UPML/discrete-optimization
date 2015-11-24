/**
 * General class to time tasks
 */
public class Timer {
	private long start;
	private long stop;
	/**
	 * Start the timer
	 */
	public void start() {
		start = System.nanoTime();
	}

	/**
	 * Stop the timer
	 */
	public void stop() {
		stop = System.nanoTime();
	}

	public long getTime() {
		return stop - start;
	}
}
