package eu.plumbr.ebdc.issue

import org.joda.time.DateTime
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Nikem on 28/01/15.
 */
class VersionTest extends Specification {

//  @Unroll
//  def "version #version started on #week"(String version, int week) {
//    expect:
//    new Version(version).startWeek == week
//
//    where:
//    version | week
//    '4.5'   | 201440
//    '4.7'   | 201449
//    '15.01' | 201501
//    '15.02' | 201506
//  }

  @Unroll
  def "version #version started on #date"(String version, DateTime date) {
    expect:
    new Version(version).start() == date

    where:
    version | date
    '4.5'   | new DateTime().withYear(2014).withMonthOfYear(10).withDayOfMonth(8).withTimeAtStartOfDay()
    '4.7'   | new DateTime().withYear(2014).withMonthOfYear(11).withDayOfMonth(30).withTimeAtStartOfDay()
    '15.01' | new DateTime().withYear(2015).withMonthOfYear(1).withDayOfMonth(1).withTimeAtStartOfDay()
    '15.02' | new DateTime().withYear(2015).withMonthOfYear(2).withDayOfMonth(1).withTimeAtStartOfDay()
  }
}
