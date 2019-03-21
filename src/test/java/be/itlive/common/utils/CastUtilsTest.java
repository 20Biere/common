package be.itlive.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for CastUtils.
 *
 * @author vbiertho
 *
 */
public class CastUtilsTest {

    private static final String NO_NULL_POINTER_EXCEPTION = "Expected test to throw an instance of java.lang.NullPointerException";

    private static final String NO_CLASS_CAST_EXCEPTION = "Expected test to throw an instance of java.lang.ClassCastException";

    @Test
    public void testCastList() throws Exception {
        List<Object> list = new ArrayList<Object>();
        Class<String> type = String.class;
        List<String> expectedResult = new ArrayList<String>();

        assertEquals(expectedResult, CastUtils.cast(list, type));

        list.add("abc");
        list.add("def");
        expectedResult.add("abc");
        expectedResult.add("def");

        assertEquals(expectedResult, CastUtils.cast(list, type));
    }

    @Test
    public void testCastList_nullParameters() throws Exception {
        List<Object> list = new ArrayList<Object>();
        Class<String> type = String.class;

        try {
            CastUtils.cast((List<Object>) null, type);
            fail(NO_NULL_POINTER_EXCEPTION);
        } catch (final NullPointerException e) {
        }
        assertEquals(0, CastUtils.cast(list, null).size());
        try {
            CastUtils.cast((List<Object>) null, null);
            fail(NO_NULL_POINTER_EXCEPTION);
        } catch (final NullPointerException e) {
        }
    }

    @Test
    public void testCastList_invalidParameters() throws Exception {
        List<Object> list = new ArrayList<Object>();
        Class<String> type = String.class;

        list.add("abc");
        list.add("def");
        list.add(123);
        list.add("ghi");

        try {
            CastUtils.cast(list, type);
            fail(NO_CLASS_CAST_EXCEPTION);
        } catch (final ClassCastException e) {
        }
    }

    @Test
    public void testCastObject() throws Exception {
        String string = "abc";
        Object object = string;
        Class<String> type = String.class;

        assertEquals("abc", CastUtils.cast(object, type));
    }

    @Test
    public void testCastObject_nullParameters() throws Exception {
        Object object = "abc";
        Class<String> type = String.class;

        assertNull(CastUtils.cast((Object) null, type));
        try {
            CastUtils.cast(object, null);
            fail(NO_NULL_POINTER_EXCEPTION);
        } catch (final NullPointerException e) {
        }
        try {
            CastUtils.cast((Object) null, null);
            fail(NO_NULL_POINTER_EXCEPTION);
        } catch (final NullPointerException e) {
        }
    }

    @Test
    public void testCastObject_invalidParameters() throws Exception {
        String string = "abc";
        Object object = string;
        Class<Integer> type = Integer.class;

        try {
            CastUtils.cast(object, type);
            fail(NO_CLASS_CAST_EXCEPTION);
        } catch (final ClassCastException e) {
        }
    }
}
