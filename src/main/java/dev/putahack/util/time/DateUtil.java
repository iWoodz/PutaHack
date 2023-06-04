package dev.putahack.util.time;

import java.util.Calendar;

import static java.util.Calendar.*;

/**
 * @author aesthetical
 * @since 06/04/23
 * <p>
 * Mostly used for joke related things
 * seriously if ur on anarchy on Christmas day kys
 * </p>
 */
public class DateUtil {

    /**
     * Checks if it is april fools
     * @return if it is april fools
     */
    public static boolean isAprilFools() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(MONTH) == APRIL
                && calendar.get(DAY_OF_MONTH) == 1;
    }

    /**
     * Checks if it is Christmas
     * @return if it is Christmas
     */
    public static boolean isChristmas() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(MONTH) == DECEMBER
                && calendar.get(DAY_OF_MONTH) == 25;
    }
}
