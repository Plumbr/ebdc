package eu.plumbr.ebdc.jira

import spock.lang.Specification

/**
 * Created by Nikem on 27/12/14.
 */
class RestJiraClientTest extends Specification {

  def "search should be able to return more than 1K issues"() {
    setup:
    def jiraClient = new JiraClientFactory().build()

    when:
    def issues = jiraClient.search("createdDate < 2014-01-01")

    then:
    issues.size() == 1580
  }

}
