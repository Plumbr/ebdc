package eu.plumbr.ebdc.jira

import eu.plumbr.ebdc.issue.BacklogIssue
import org.joda.time.DateTime

/**
 * Created by Nikem on 21/12/14.
 */
interface JiraClient {

  List<BacklogIssue> search(String jql);

  List<BacklogIssue> updatedIssues(DateTime since)
}