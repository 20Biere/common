package be.itlive.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.junit.Test;

public class DateTimeUtilTest {
    @Test
    public void testGetActualNumberOfDaysToAdd() throws Exception {
        LocalDate seedDate = new LocalDate(2018, 1, 1);

        // take all the days of a week
        for (int i = 0; i < 7; ++i) {
            LocalDate tmpDate = seedDate.plusDays(i);

            // the number of work days to add
            for (int daysToAdd = 1; daysToAdd <= 30; ++daysToAdd) {
                assertThat(DateTimeUtil.addDaysWithoutWeekEnd(tmpDate, daysToAdd)).isEqualTo(addDaysWithoutWeekend(tmpDate, daysToAdd));
            }
        }
    }

    private LocalDate addDaysWithoutWeekend(final LocalDate date, final int workdays) {
        LocalDate ret = date;

        int addedDays = 0;
        while (addedDays < workdays) {
            ret = ret.plusDays(1);
            if (ret.getDayOfWeek() <= DateTimeConstants.FRIDAY) {
                ++addedDays;
            }
        }

        return ret;
    }
}
