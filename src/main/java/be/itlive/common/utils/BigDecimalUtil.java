package be.itlive.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import be.itlive.common.exceptions.BusinessException;

/**
 * Utility class exposing frequent operations to work BigDecimal.
 *
 * @author vbiertho
 */
public final class BigDecimalUtil {

    private BigDecimalUtil() {
        throw new AssertionError("Instantiating utility class.");
    }

    /**
     * @param value {@link BigDecimal}
     * @param toCompare {@link BigDecimal}
     * @return true if value is equals than toCompare
     * @throws BusinessException if one of the given values is null
     */
    public static boolean isEquals(final BigDecimal value, final BigDecimal toCompare) throws BusinessException {
        if (value == null || toCompare == null) {
            throw new BusinessException("Compare data with null value");
        }

        return value.compareTo(toCompare) == 0;
    }

    /**
     * @param value {@link BigDecimal}
     * @param toCompare {@link BigDecimal}
     * @return true if value is greater than toCompare
     * @throws BusinessException if one of the given values is null
     */
    public static boolean isGreatherThan(final BigDecimal value, final BigDecimal toCompare) throws BusinessException {
        if (value == null || toCompare == null) {
            throw new BusinessException("Compare data with null value");
        }

        return value.compareTo(toCompare) == 1;
    }

    /**
     * @param value {@link BigDecimal}
     * @param toCompare {@link BigDecimal}
     * @return true if value is greater or equals than toCompare
     * @throws BusinessException if one of the given values is null
     */
    public static boolean isGreatherOrEqualsThan(final BigDecimal value, final BigDecimal toCompare) throws BusinessException {
        return isEquals(value, toCompare) || isGreatherThan(value, toCompare);
    }

    /**
     * @param value {@link BigDecimal}
     * @param toCompare {@link BigDecimal}
     * @return true if value is lesser than toCompare
     * @throws BusinessException if one of the given values is null
     */
    public static boolean isLesserThan(final BigDecimal value, final BigDecimal toCompare) throws BusinessException {
        if (value == null || toCompare == null) {
            throw new BusinessException("Compare data with null value");
        }

        return value.compareTo(toCompare) == -1;
    }

    /**
     * @param value {@link BigDecimal}
     * @param toCompare {@link BigDecimal}
     * @return true if value is lesser than toCompare
     * @throws BusinessException if one of the given values is null
     */
    public static boolean isLesserOrEqualsThan(final BigDecimal value, final BigDecimal toCompare) throws BusinessException {
        return isLesserThan(value, toCompare) || isEquals(value, toCompare);
    }

    /**
     * @param value {@link BigDecimal}
     * @return true if = 0
     * @throws BusinessException if given value is null
     */
    public static boolean isZero(final BigDecimal value) throws BusinessException {
        if (value == null) {
            throw new BusinessException("value can't be null");
        }

        return value.signum() == 0;
    }

    /**
     * @param value {@link BigDecimal}
     * @return true if > 0
     * @throws BusinessException if given value is null
     */
    public static boolean isPositive(final BigDecimal value) throws BusinessException {
        if (value == null) {
            throw new BusinessException("value can't be null");
        }

        return value.signum() == 1;
    }

    /**
     * @param value {@link BigDecimal}
     * @return true if >= 0
     * @throws BusinessException if given value is null
     */
    public static boolean isPositiveOrZero(final BigDecimal value) throws BusinessException {
        return isPositive(value) || isZero(value);
    }

    /**
     * @param value {@link BigDecimal}
     * @return true if < 0
     * @throws BusinessException if given value is null
     */
    public static boolean isNegative(final BigDecimal value) throws BusinessException {
        if (value == null) {
            throw new BusinessException("value can't be null");
        }

        return value.signum() == -1;
    }

    /**
     * @param value {@link BigDecimal}
     * @return true if <= 0
     * @throws BusinessException if given value is null
     */
    public static boolean isNegativeOrZero(final BigDecimal value) throws BusinessException {
        return isNegative(value) || isZero(value);
    }

    /**
     * Rounding value. (Example : value = 100; increment = 0.5; roundingMode = RoundingMode.UP; => result = 13.5)
     * @param value value to rounding
     * @param increment increment
     * @param roundingMode rounding mode
     * @return value rounding
     * @throws BusinessException if given value or increment null
     */
    public static BigDecimal roundingValue(final BigDecimal value, final BigDecimal increment, final RoundingMode roundingMode)
            throws BusinessException {
        if (isZero(value) || isZero(increment) || roundingMode == null) {
            return value;
        } else {
            BigDecimal divided = value.divide(increment, 0, roundingMode);
            return divided.multiply(increment);
        }
    }
}
