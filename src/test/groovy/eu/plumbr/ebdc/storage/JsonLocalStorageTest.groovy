package eu.plumbr.ebdc.storage

import eu.plumbr.ebdc.issue.BacklogIssue
import spock.lang.Specification

/**
 * Created by Nikem on 27/12/14.
 */
class JsonLocalStorageTest extends Specification {

  def "should read data as correct eu.plumbr.ebdc.issue.BacklogIssue instance"() {
    setup:
    def storage = new JsonLocalStorage(null)

    when:
    def backlogIssue = storage.all["DTS-1"]

    then:
    backlogIssue instanceof BacklogIssue
    backlogIssue.assignee == 'nikita'

    then: "ChangeLog is sorted"
    backlogIssue.changeLog.created == backlogIssue.changeLog.created.sort()

    then: "ChangeLog's structure is preserved"
    backlogIssue.changeLog[6].items[0].toString == 'Closed'
    backlogIssue.changeLog[6].items[1].toString == 'Fixed'
  }
}
