package eu.plumbr.ebdc

import org.joda.time.DateTime

/**
 * Created by Nikem on 11/08/14.
 */
class Utils {

  public static final String DATE_FORMAT = 'yyyy-MM-dd'

  static int weekNumber(DateTime dateTime) {
    Integer.valueOf(dateTime.toString('xxxxww'))
//    dateTime.year * 100 + dateTime.weekOfWeekyear
  }

//  static int weekAdded(Issue issue, Version version) {
//    def changelog = issue.changelog
//    ChangelogGroup change = changelog.find { ChangelogGroup cl ->
//      cl.items.find { ChangelogItem item -> item.toString == version.name } != null
//    } as ChangelogGroup
//    if (change != null) {
//      return weekNumber(change.created)
//    }
//    return issue.fixVersions.find { it.name == version.name } ? weekNumber(issue.creationDate) : 0
//  }
//
//  static int weekPlanned(Issue issue) {
//    def changelog = issue.changelog
//    ChangelogGroup change = changelog.find { ChangelogGroup cl ->
//      def addedFixVersionChange = cl.items.find { ChangelogItem item ->
//        item.field == 'Fix Version' && item.to != null && item.from == null
//      }
//      def removedFixVersionChange = cl.items.find { ChangelogItem item ->
//        item.field == 'Fix Version' && item.to == null && item.from != null
//      }
//      addedFixVersionChange != null && removedFixVersionChange == null
//    } as ChangelogGroup
//    if (change != null) {
//      return weekNumber(change.created)
//    }
//    return issue.fixVersions.find() ? weekNumber(issue.creationDate) : 0
//  }
//
//  static int weekCompleted(Issue issue) {
//    def changelog = issue.changelog
//    ChangelogGroup change = changelog.find { ChangelogGroup cl ->
//      cl.items.find { ChangelogItem item -> item.toString == 'Closed' } != null
//    } as ChangelogGroup
//    change != null ? weekNumber(change.created) : 0
//  }
//
//  static int size(Issue issue) {
//    issue.issueType.name == 'Epic' ? 8 : 1
//  }
//
//  static def convertWeekToTimestamp(int yearWeek){
//    int year = yearWeek / 100
//    int week = yearWeek % 100
//
//    assert year * 100 + week == yearWeek
//
//    new DateTime().withYear(year).withWeekOfWeekyear(week).millis
//  }

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
