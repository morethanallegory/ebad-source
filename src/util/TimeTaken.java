package util;

import java.util.concurrent.TimeUnit;

/**
 * Create a TimeTaken.
 **/

public class TimeTaken {

	/**
	 * start time.
	 */
	private long start;

	/**
	 * finish time.
	 */
	private long finish;

	/**
	 * identifier.
	 */
	private String identifier;

	/**
	 * Creates a new TimeTaken with identifier set.
	 * 
	 * @param identifier
	 *            for this instance.
	 */
	public TimeTaken(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * initialize the start time.
	 */
	public void start() {
		this.start = System.nanoTime();
	}

	/**
	 * initialize the finish time.
	 */
	public void finish() {
		this.finish = System.nanoTime();
	}

	/**
	 * to string.
	 * 
	 * @return string representation of this instance
	 */
	@Override
	public String toString() {// not entirely accurate will round down to
								// nearest second
		return "TimeTaken: "
				+ this.identifier
				+ " [seconds="
				+ TimeUnit.SECONDS
						.convert(finish - start, TimeUnit.NANOSECONDS) + "]";
	}

}
