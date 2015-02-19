package eu.plumbr.ebdc.storage

import eu.plumbr.ebdc.issue.BacklogIssue
import eu.plumbr.ebdc.issue.ChangeLogEntry
import eu.plumbr.ebdc.issue.ChangeLogItem
import eu.plumbr.ebdc.issue.Version
import eu.plumbr.ebdc.jira.JiraClient
import eu.plumbr.ebdc.jira.JiraClientFactory
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import java.util.regex.Pattern
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Created by Nikem on 20/12/14.
 */
@Component
@Slf4j
class JsonLocalStorage implements LocalStorage {

  private File storageFile = new File("local.json")
  private Map<String, BacklogIssue> storage
  private JiraClient jiraClient

  @Autowired
  JsonLocalStorage(JiraClient jiraClient) {
    this.jiraClient = jiraClient
    if (!storageFile.exists()) {
      storageFile.createNewFile()
      this.storage = new HashMap<String, BacklogIssue>()
    } else {
      this.storage = toPOGO(new JsonSlurper().parse(storageFile) as Map<String, Map>)
    }
  }

  private static Map<String, BacklogIssue> toPOGO(Map<String, Map> rawData) {
    Map<String, BacklogIssue> result = [:]
    rawData.each { k, v ->
      def backlogIssue = new BacklogIssue(v)
      backlogIssue.changeLog = backlogIssue.changeLog.collect { new ChangeLogEntry(it) }
      backlogIssue.changeLog.each {
        it.items = it.items.collect { item -> new ChangeLogItem(item) }
      }
      backlogIssue.fixVersions = backlogIssue.fixVersions.collect { new Version(it) }
      result[k] = backlogIssue
    }
    result
  }

  @Override
  @Scheduled(fixedRate = 3600000L)
  void refresh() {
    def latestSync = storage.isEmpty() ? new DateTime(2010, 1, 1, 0, 0) : new DateTime(storage.values().max {
      it.updateDate
    }?.updateDate)
    def syncStart = latestSync.minusDays(2)

    log.info("Refreshing issues since {}", syncStart)

    jiraClient.updatedIssues(syncStart).each { BacklogIssue issue ->
      storage[issue.key] = issue;
    }
    persist()
  }

  @Override
  Map<String, BacklogIssue> getAll() {
    return storage
  }

  private void persist() {
    log.info("Persisting {} issues", storage.size())
    storageFile.text = new JsonBuilder(storage).toPrettyString().replaceAll(Pattern.quote("\\\""), "'")
  }

  public static void main(String[] args) {
    def localStorage = new JsonLocalStorage(new JiraClientFactory().build())
    localStorage.refresh()
    def issue = localStorage.storage.get('DTS-1107')
//    issue.fixVersions = null
    println issue.fixVersions
    println new JsonBuilder(issue.fixVersions).toPrettyString()
  }
}
