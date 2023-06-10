package dev.starstruck.util.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author aesthetical
 * @since 06/04/23
 */
public class MathUtils {

    /**
     * Round a double to an amount of decimal places
     * @param value the value
     * @param scale the scale
     * @return the rounded double
     */
    public static double round(double value, int scale) {
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_DOWN).doubleValue();
    }
}
