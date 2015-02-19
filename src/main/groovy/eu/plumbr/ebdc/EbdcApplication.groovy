package eu.plumbr.ebdc

import eu.plumbr.ebdc.jira.JiraClient
import eu.plumbr.ebdc.jira.JiraClientFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
class EbdcApplication {

  static void main(String[] args) {
    SpringApplication.run EbdcApplication, args
  }

  @Bean
  public JiraClient jiraClient(JiraClientFactory factory) {
    factory.build()
  }
}
