package be.itlive.common.utils;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

import be.itlive.common.exceptions.BusinessException;

/**
 * @author vbiertho
 *
 */
public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * Add days to date without take account week end.
     * @param date current date
     * @param workDaysToAdd number of work days to add: SHOULD be a positive number
     * @return date with days added
     * @throws BusinessException if date is null
     */
    public static LocalDate addDaysWithoutWeekEnd(final LocalDate date, final int workDaysToAdd) throws BusinessException {

        if (date == null) {
            throw new BusinessException("date cannot be null.");
        }

        if (workDaysToAdd <= 0) {
            throw new IllegalArgumentException(String.format("The argument workDaysToAdd = [%s] is invalid", workDaysToAdd));
        }

        int dayOfWeek = date.getDayOfWeek();

        // Calculate the total weekends in between the input date and the final calculated date
        int totalWeekendsInBetween;
        if (dayOfWeek > DateTimeConstants.FRIDAY) {
            totalWeekendsInBetween = (workDaysToAdd - 1) / 5;
        } else {
            totalWeekendsInBetween = (workDaysToAdd + (dayOfWeek - 1)) / 5;
        }

        LocalDate tmpDate = date;

        if (dayOfWeek > DateTimeConstants.FRIDAY) {
            tmpDate = tmpDate.plusDays(DateTimeConstants.DAYS_PER_WEEK - dayOfWeek);
        }

        return tmpDate.plusDays(workDaysToAdd + totalWeekendsInBetween * 2);
    }
}
