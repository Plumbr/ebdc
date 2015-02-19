package eu.plumbr.ebdc

import org.joda.time.DateTime

/**
 * Created by Nikem on 11/08/14.
 */
class Utils {

  public static final String DATE_FORMAT = 'yyyy-MM-dd'

  static int weekNumber(DateTime dateTime) {
    Integer.valueOf(dateTime.toString('xxxxww'))
  }

  static List<Integer> allWeeksBetween(int start, int end) {
    DateTime current = weekToDateTime(start)

    def result = [start]

    while (result.last() < end) {
      current = current.plusWeeks(1)
      result << weekNumber(current)
    }
    return result
  }

  private static DateTime weekToDateTime(int weekNumber) {
    int year = weekNumber / 100
    int week = weekNumber % 100

    assert year * 100 + week == weekNumber

    new DateTime(year, 1, 1, 0, 0).withWeekOfWeekyear(week).withDayOfWeek(1)
  }
}
