package eu.plumbr.ebdc

import org.joda.time.DateTime
import spock.lang.Specification

/**
 * Created by Nikem on 28/12/14.
 */
class UtilsTest extends Specification {

  def "should generate all weeks between two weeks"() {
    expect:
    Utils.allWeeksBetween(201402, 201410) == [201402, 201403, 201404, 201405, 201406, 201407, 201408, 201409, 201410]
  }

  def "should handle change of year"() {
    expect:
    Utils.allWeeksBetween(201350, 201402) == [201350, 201351, 201352, 201401, 201402]
  }

  def "we understand Joda API"() {
    expect:
    new DateTime(2014, 1, 1, 0, 0).withWeekOfWeekyear(2).withDayOfWeek(1) == new DateTime(2014, 1, 6, 0, 0)
  }
}
