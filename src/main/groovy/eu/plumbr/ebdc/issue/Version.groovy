package eu.plumbr.ebdc.issue

import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormatterBuilder

/**
 * Created by Nikem on 28/01/15.
 */
@CompileStatic
@ToString
class Version implements Comparable<Version> {

  private
  static DateTimeFormatter versionFormatter = new DateTimeFormatterBuilder().appendTwoDigitYear(2000).appendLiteral('.').appendMonthOfYear(2).toFormatter()
  private static Map<String, DateTime> versionStart = ['4.1': new DateTime(2014, 6, 3, 0, 0),
                                                       '4.2': new DateTime(2014, 7, 3, 0, 0),
                                                       '4.3': new DateTime(2014, 8, 4, 0, 0),
                                                       '4.4': new DateTime(2014, 9, 1, 0, 0),
                                                       '4.5': new DateTime(2014, 10, 8, 0, 0),
                                                       '4.6': new DateTime(2014, 11, 3, 0, 0),
                                                       '4.7': new DateTime(2014, 11, 30, 0, 0)]

  String name
  boolean oldVersion

  Version() {}

  public Version(net.rcarz.jiraclient.Version version) {
    this(version.name)
  }

  public Version(String name) {
    this.name = name
    this.oldVersion = !this.name.startsWith('15')
  }

  public DateTime start() {
    if (oldVersion) {
      return versionStart[name]
    }
    versionFormatter.parseDateTime(name)
  }


  @Override
  int compareTo(Version other) {
    return this.start().compareTo(other.start())
  }
}
