package be.itlive.common.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import be.itlive.common.exceptions.BusinessException;

public class BigDecimalUtilTest {

    @Test
    public void testEquals() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);
        BigDecimal value2 = new BigDecimal(5.0);

        assertTrue(BigDecimalUtil.isEquals(value1, value2));

        value1 = BigDecimal.ZERO;
        value2 = new BigDecimal(0.000);

        assertTrue(BigDecimalUtil.isEquals(value1, value2));
    }

    @Test
    public void testNotEquals() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);
        BigDecimal value2 = new BigDecimal(10);

        assertFalse(BigDecimalUtil.isEquals(value1, value2));
    }

    @Test
    public void testGreaterThan() throws BusinessException {

        BigDecimal value1 = new BigDecimal(10);
        BigDecimal value2 = new BigDecimal(5);

        assertTrue(BigDecimalUtil.isGreatherThan(value1, value2));
    }

    @Test
    public void testGreaterOrEqualsThan() throws BusinessException {

        BigDecimal value1 = new BigDecimal(10);
        BigDecimal value2 = new BigDecimal(5);
        BigDecimal value3 = new BigDecimal(5);

        assertTrue(BigDecimalUtil.isGreatherOrEqualsThan(value1, value2));
        assertTrue(BigDecimalUtil.isGreatherOrEqualsThan(value3, value2));
    }

    @Test
    public void testLesserThan() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);
        BigDecimal value2 = new BigDecimal(10);

        assertTrue(BigDecimalUtil.isLesserThan(value1, value2));
    }

    @Test
    public void testLesserOrEqualsThan() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);
        BigDecimal value2 = new BigDecimal(10);
        BigDecimal value3 = new BigDecimal(10);

        assertTrue(BigDecimalUtil.isLesserOrEqualsThan(value1, value2));
        assertTrue(BigDecimalUtil.isLesserOrEqualsThan(value3, value2));
    }

    @Test
    public void testIsZero() throws BusinessException {

        BigDecimal value = BigDecimal.ZERO;
        BigDecimal value1 = new BigDecimal(0.0);
        BigDecimal value2 = new BigDecimal(0.000);

        assertTrue(BigDecimalUtil.isZero(value));
        assertTrue(BigDecimalUtil.isZero(value1));
        assertTrue(BigDecimalUtil.isZero(value2));
    }

    @Test
    public void testIsPositive() throws BusinessException {

        BigDecimal positive = BigDecimal.ONE;
        BigDecimal negative = BigDecimal.ONE.negate();

        assertTrue(BigDecimalUtil.isPositive(positive));
        assertFalse(BigDecimalUtil.isPositive(negative));
    }

    @Test
    public void testIsNegative() throws BusinessException {

        BigDecimal positive = BigDecimal.ONE;
        BigDecimal negative = BigDecimal.ONE.negate();

        assertTrue(BigDecimalUtil.isNegative(negative));
        assertFalse(BigDecimalUtil.isNegative(positive));
    }

    @Test
    public void testIsPositiveOrZero() throws BusinessException {

        BigDecimal positive = BigDecimal.ONE;
        BigDecimal negative = BigDecimal.ONE.negate();
        BigDecimal zero = BigDecimal.ZERO;

        assertTrue(BigDecimalUtil.isPositiveOrZero(positive));
        assertTrue(BigDecimalUtil.isPositiveOrZero(zero));
        assertFalse(BigDecimalUtil.isPositiveOrZero(negative));
    }

    @Test
    public void testIsNegativeOrZero() throws BusinessException {

        BigDecimal positive = BigDecimal.ONE;
        BigDecimal negative = BigDecimal.ONE.negate();
        BigDecimal zero = BigDecimal.ZERO;

        assertTrue(BigDecimalUtil.isNegativeOrZero(negative));
        assertTrue(BigDecimalUtil.isNegativeOrZero(zero));
        assertFalse(BigDecimalUtil.isNegativeOrZero(positive));
    }

    /** Exception .*/
    @Test(expected = BusinessException.class)
    public void testEquals_exception() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);

        BigDecimalUtil.isEquals(value1, null);
        BigDecimalUtil.isEquals(null, value1);
    }

    @Test(expected = BusinessException.class)
    public void testGreaterThan_exception() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);

        BigDecimalUtil.isGreatherThan(value1, null);
        BigDecimalUtil.isGreatherThan(null, value1);
    }

    @Test(expected = BusinessException.class)
    public void testGreaterOrEqualsThan_exception() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);

        BigDecimalUtil.isGreatherOrEqualsThan(value1, null);
        BigDecimalUtil.isGreatherOrEqualsThan(null, value1);
    }

    @Test(expected = BusinessException.class)
    public void testLesserOrEquals_exception() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);

        BigDecimalUtil.isLesserOrEqualsThan(value1, null);
        BigDecimalUtil.isLesserOrEqualsThan(null, value1);
    }

    @Test(expected = BusinessException.class)
    public void testLesser_exception() throws BusinessException {

        BigDecimal value1 = new BigDecimal(5);

        BigDecimalUtil.isLesserThan(value1, null);
        BigDecimalUtil.isLesserThan(null, value1);
    }

    @Test(expected = BusinessException.class)
    public void testIsPositive_exception() throws BusinessException {
        BigDecimalUtil.isPositive(null);
    }

    @Test(expected = BusinessException.class)
    public void testIsNegative_exception() throws BusinessException {
        BigDecimalUtil.isNegative(null);
    }

    @Test(expected = BusinessException.class)
    public void testIsZero_exception() throws BusinessException {
        BigDecimalUtil.isZero(null);
    }

}
