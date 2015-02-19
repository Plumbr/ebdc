package eu.plumbr.ebdc.jira

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by Nikem on 27/12/14.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class JiraClientFactory {

  JiraClient build() {
    def props = new Properties()
    props.load(new FileReader(new File('jira.properties')))
    new RestJiraClient(props.getProperty('username'), props.getProperty('password'))
  }
}
