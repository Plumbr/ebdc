package eu.plumbr.ebdc.issue

import eu.plumbr.ebdc.Utils
import groovy.transform.ToString
import net.rcarz.jiraclient.Issue
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by Nikem on 10/08/14.
 */
@ToString
class BacklogIssue {
  String key
  String status
  String type
  String summary
  String assignee
  String resolution

  String createdDate
  String updateDate
  String priority

  List<ChangeLogEntry> changeLog

  BacklogIssue() {}

  BacklogIssue(Issue issue) {
    this.key = issue.key
    this.status = issue.status.name
    this.type = issue.issueType.name
    this.summary = issue.summary
    this.assignee = issue.assignee?.name
    this.resolution = issue.resolution?.name
    this.createdDate = new DateTime(issue.createdDate).toString(Utils.DATE_FORMAT)
    this.updateDate = new DateTime(issue.updatedDate).toString(Utils.DATE_FORMAT)
    this.priority = issue.priority?.name
    this.changeLog = issue.changeLog.entries.collect { new ChangeLogEntry(it) }
  }

  public int weekCompleted() {
    ChangeLogEntry change = changeLog.find { ChangeLogEntry entry ->
      entry.items.find { ChangeLogItem item -> item.toString == 'Closed' } != null
    } as ChangeLogEntry
    change != null ? Utils.weekNumber(new DateTime(change.created)) : 0
  }

  public int weekFixed() {
    ChangeLogEntry change = changeLog.find { ChangeLogEntry entry ->
      entry.items.find { ChangeLogItem item -> item.field == 'resolution' && item.toString == 'Fixed' } != null
    } as ChangeLogEntry
    change != null ? Utils.weekNumber(DateTime.parse(change.created, DateTimeFormat.forPattern(Utils.DATE_FORMAT))) : 0
  }


  public int size() {
    this.type == 'Epic' ? 8 : 1
  }

}
