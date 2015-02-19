package eu.plumbr.ebdc.velocity

import eu.plumbr.ebdc.issue.BacklogIssue
import eu.plumbr.ebdc.storage.LocalStorage
import spock.lang.Specification

/**
 * Created by Nikem on 27/12/14.
 */
class TeamVelocityTest extends Specification {

  def "csv should return each person on separate line"() {
    setup:
    def issue1 = Mock(BacklogIssue)
    issue1.weekFixed() >> 201402
    issue1.assignee >> 'nikita'

    def issue2 = Mock(BacklogIssue)
    issue2.weekFixed() >> 201302
    issue2.assignee >> 'marten'

    def storage = Mock(LocalStorage)
    storage.all >> [
        "Issue-1": issue1,
        "Issue-2": issue2
    ]

    def velocity = new TeamVelocity(storage)

    when:
    def csv = velocity.toCsv()
    def lines = csv.readLines()

    then:
    lines.size() == 2 + 1
    lines[1].startsWith('marten')
    lines[2].startsWith('nikita')
  }

  def "each csv line should be sorted by week number"() {
    setup:
    def storage = Mock(LocalStorage)
    storage.all >> [
        "Issue-1": issue(),
        "Issue-2": issue(201401),
        "Issue-3": issue(201401)
    ]

    def velocity = new TeamVelocity(storage)

    when:
    def csv = velocity.toCsv()

    then:
    csv.readLines()[1] == 'nikita;2;1'
  }

  def "each in-between week should be present in csv with 0 value"() {
    setup:
    def issue1 = Mock(BacklogIssue)
    issue1.weekFixed() >> 201402
    issue1.assignee >> 'nikita'
    issue1.size() >> 1

    def issue2 = Mock(BacklogIssue)
    issue2.weekFixed() >> 201405
    issue2.assignee >> 'nikita'
    issue2.size() >> 1

    def storage = Mock(LocalStorage)
    storage.all >> [
        "Issue-1": issue1,
        "Issue-2": issue2
    ]

    def velocity = new TeamVelocity(storage)

    when:
    def csv = velocity.toCsv()

    then:
    csv.readLines()[1] == 'nikita;1;0;0;1'
  }

  def "csv should have header row with all weeks"() {
    setup:
    def storage = Mock(LocalStorage)
    storage.all >> [
        "Issue-1": issue(),
        "Issue-2": issue(201405)
    ]

    def velocity = new TeamVelocity(storage)

    when:
    def csv = velocity.toCsv()

    then:
    csv.readLines()[0] == 'Developer;201402;201403;201404;201405'
  }

  def "csv should return cumulative velocity"() {
    setup:
    def storage = Mock(LocalStorage)
    storage.all >> [
        "Issue-1": issue(201402, 'nikita', 8),
        "Issue-2": issue(201402, 'marten'),
        "Issue-3": issue(201403),
        "Issue-4": issue(201403, 'marten')
    ]

    def velocity = new TeamVelocity(storage)

    when:
    def csv = velocity.toCumulativeCsv()

    then:
    csv == '201402;201403\n9;2'
  }

  BacklogIssue issue(int weekFixed = 201402, String assignee = 'nikita', int size = 1) {
    def issue = Mock(BacklogIssue)
    issue.weekFixed() >> weekFixed
    issue.assignee >> assignee
    issue.size() >> size
    return issue
  }

}
