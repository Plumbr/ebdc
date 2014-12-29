package eu.plumbr.ebdc.velocity

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by Nikem on 21/12/14.
 */
@Controller
class VelocityController {

  @RequestMapping('/')
  public String index() {
    return "index"
  }
}
