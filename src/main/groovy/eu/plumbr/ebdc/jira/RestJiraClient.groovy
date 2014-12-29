package eu.plumbr.ebdc.jira

import eu.plumbr.ebdc.issue.BacklogIssue
import net.rcarz.jiraclient.BasicCredentials
import net.rcarz.jiraclient.Issue
import net.rcarz.jiraclient.JiraClient as RemoteJira
import org.joda.time.DateTime

/**
 * Created by Nikem on 21/12/14.
 */
class RestJiraClient implements JiraClient {
  private RemoteJira remoteJira

  RestJiraClient(String username, String password) {
    BasicCredentials creds = new BasicCredentials(username, password);
    remoteJira = new RemoteJira("https://plumbr.atlassian.net", creds)
  }

  @Override
  List<BacklogIssue> updatedIssues(DateTime since) {
    return search("updatedDate >= ${since.toString('yyyy-MM-dd')} and 'Epic Link' is null")
  }

  public List<BacklogIssue> search(String jql) {
    List<Issue> result = []
    def searchResult = new Issue.SearchResult(max: Integer.MAX_VALUE, total: Integer.MAX_VALUE)

    while (result.size() < searchResult.total) {
      searchResult = fetch(jql, searchResult.max, result.size())
      result.addAll(searchResult.issues)
    }

    return result.collect { new BacklogIssue(it) }
  }

  private Issue.SearchResult fetch(String jql, int maxResults, int startAt) {
    remoteJira.searchIssues(jql, null, "changelog", maxResults, startAt)
  }
}
