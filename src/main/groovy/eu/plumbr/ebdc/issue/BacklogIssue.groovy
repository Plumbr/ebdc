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

  List<Version> fixVersions

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
    this.fixVersions = issue.fixVersions.collect { new Version(it) }
  }

  public int weekCompleted() {
    ChangeLogEntry change = changeLog.find { ChangeLogEntry entry ->
      entry.items.find { ChangeLogItem item -> item.toString == 'Closed' || item.toString == 'Resolved' } != null
    } as ChangeLogEntry
    change != null ? Utils.weekNumber(new DateTime(change.created)) : 0
  }

  public int weekFixed() {
    ChangeLogEntry change = changeLog.find { ChangeLogEntry entry ->
      entry.items.find { ChangeLogItem item -> item.field == 'resolution' && item.toString == 'Fixed' } != null
    } as ChangeLogEntry
    change != null ? Utils.weekNumber(DateTime.parse(change.created, DateTimeFormat.forPattern(Utils.DATE_FORMAT))) : 0
  }

  public int weekFirstPlanned() {
    def planned = firstPlanned()
    planned != null ? Utils.weekNumber(planned) : 0
  }

  public DateTime firstPlanned() {
    ChangeLogEntry change = firstFixVersionEntry()
    change != null ? DateTime.parse(change.created, DateTimeFormat.forPattern(Utils.DATE_FORMAT)) : null
  }


  public String firstFixVersion() {
    firstFixVersionEntry()?.items?.find { ChangeLogItem item -> item.field == 'Fix Version' && item.toString != null }?.toString
  }

  public String currentFixVersion() {
    fixVersions.isEmpty() ? null : fixVersions.first().name
  }

  private ChangeLogEntry firstFixVersionEntry() {
    ChangeLogEntry change = changeLog.find { ChangeLogEntry entry ->
      entry.items.find { ChangeLogItem item -> item.field == 'Fix Version' && item.toString != null } != null
    } as ChangeLogEntry
    return change
  }

  private ChangeLogEntry lastFixVersionEntry() {
    def fixVersionChangeEntries = changeLog.findAll { ChangeLogEntry entry ->
      entry.items.find { ChangeLogItem item -> item.field == 'Fix Version' && item.toString != null } != null
    }
    fixVersionChangeEntries.isEmpty() ? null : fixVersionChangeEntries.last()
  }


  public int size() {
    this.type == 'Epic' ? 8 : 1
  }

}
