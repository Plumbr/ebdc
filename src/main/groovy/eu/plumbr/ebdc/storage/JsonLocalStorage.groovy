package eu.plumbr.ebdc.storage

import eu.plumbr.ebdc.issue.BacklogIssue
import eu.plumbr.ebdc.issue.ChangeLogEntry
import eu.plumbr.ebdc.issue.ChangeLogItem
import eu.plumbr.ebdc.jira.JiraClient
import eu.plumbr.ebdc.jira.JiraClientFactory
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.util.regex.Pattern
import org.joda.time.DateTime

/**
 * Created by Nikem on 20/12/14.
 */
class JsonLocalStorage implements LocalStorage {

  private File storageFile
  private Map<String, BacklogIssue> storage
  private JiraClient jiraClient

  JsonLocalStorage(JiraClient jiraClient, File storageFile = new File("local.json")) {
    this.storageFile = storageFile
    this.jiraClient = jiraClient
    this.storage = toPOGO(new JsonSlurper().parse(storageFile) as Map<String, Map>)
  }

  private static Map<String, BacklogIssue> toPOGO(Map<String, Map> rawData) {
    Map<String, BacklogIssue> result = [:]
    rawData.each { k, v ->
      def backlogIssue = new BacklogIssue(v)
      backlogIssue.changeLog = backlogIssue.changeLog.collect { new ChangeLogEntry(it) }
      backlogIssue.changeLog.each {
        it.items = it.items.collect { item -> new ChangeLogItem(item) }
      }
      result[k] = backlogIssue
    }
    result
  }

  @Override
  void refresh() {
    def latestSync = storage.isEmpty() ? new DateTime(2010, 1, 1, 0, 0) : new DateTime(storage.values().max {
      it.updateDate
    }?.updateDate)
    jiraClient.updatedIssues(latestSync.minusDays(2)).each { BacklogIssue issue ->
      storage[issue.key] = issue;
    }
    persist()
  }

  @Override
  Map<String, BacklogIssue> getAll() {
    return storage
  }

  private void persist() {
    storageFile.text = new JsonBuilder(storage).toPrettyString().replaceAll(Pattern.quote("\\\""), "'")
  }

  public static void main(String[] args) {
    new JsonLocalStorage(new JiraClientFactory().build()).refresh()
  }
}
