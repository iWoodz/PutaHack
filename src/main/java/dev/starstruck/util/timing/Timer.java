package dev.starstruck.util.timing;

/**
 * @author aesthetical
 * @since 06/04/23
 *
 * Measures time and shit
 */
public class Timer {

    /**
     * One millisecond in nanoseconds
     */
    private static final long ONE_MS_NS = 1_000_000L;

    /**
     * The time this timer was started
     */
    private long timeStarted;

    public Timer() {
        resetTime();
    }

    /**
     * Checks if time has passed in milliseconds
     * @param ms the millisecond time period
     * @param reset if to reset the timer if the time has passed
     * @return if the time has passed
     */
    public boolean hasPassed(long ms, boolean reset) {
        boolean passed = getDurationMS() > ms;
        if (passed && reset) resetTime();
        return passed;
    }

    /**
     * Checks if time has passed in ticks
     * @param ticks the tick time period
     * @param reset if to reset the timer if the time has passed
     * @return if the time has passed
     */
    public boolean hasPassed(int ticks, boolean reset) {
        return hasPassed(ticks * 50L, reset);
    }

    /**
     * Gets the duration from now to when this timer was last reset
     * @return the duration in nanoseconds
     */
    public long getDuration() {
        return System.nanoTime() - timeStarted;
    }

    /**
     * Gets the duration from now to when this timer was last reset
     * @return the duration in milliseconds
     */
    public long getDurationMS() {
        return getDuration() / ONE_MS_NS;
    }

    /**
     * Resets the timer to the current nano time
     */
    public void resetTime() {
        timeStarted = System.nanoTime();
    }
}
