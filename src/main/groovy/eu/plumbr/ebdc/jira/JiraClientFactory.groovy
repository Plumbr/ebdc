package eu.plumbr.ebdc.jira

/**
 * Created by Nikem on 27/12/14.
 */
class JiraClientFactory {

  JiraClient build() {
    def props = new Properties()
    props.load(new FileReader(new File('jira.properties')))
    new RestJiraClient(props.getProperty('username'), props.getProperty('password'))
  }
}
