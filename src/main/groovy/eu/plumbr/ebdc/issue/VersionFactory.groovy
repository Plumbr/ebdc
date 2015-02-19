package eu.plumbr.ebdc.issue

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormatterBuilder

/**
 * Created by Nikem on 27/01/15.
 */
class VersionFactory {

  private static
  final DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder().appendTwoDigitYear(2000).appendLiteral('.').appendMonthOfYear(2)
  private static DateTimeFormatter versionFormatter = formatBuilder.toFormatter()

  public static String currentVersion() {
    return new DateTime().toString(versionFormatter)
  }

  public static String nextVersion() {
    return new DateTime().plusMonths(1).toString(versionFormatter)
  }

  public static void main(String[] args) {
    println currentVersion()
    println nextVersion()
    println versionFormatter.parseDateTime('15.02')
  }
}
