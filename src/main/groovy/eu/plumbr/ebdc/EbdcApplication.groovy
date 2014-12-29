package eu.plumbr.ebdc

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
@EnableAutoConfiguration
class EbdcApplication {

  static void main(String[] args) {
    SpringApplication.run EbdcApplication, args
  }
}
