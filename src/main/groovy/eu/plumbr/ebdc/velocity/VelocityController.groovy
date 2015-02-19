package eu.plumbr.ebdc.velocity

import eu.plumbr.ebdc.backlog.ReleaseBacklog
import eu.plumbr.ebdc.storage.LocalStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

import static eu.plumbr.ebdc.issue.VersionFactory.currentVersion
import static eu.plumbr.ebdc.issue.VersionFactory.nextVersion

/**
 * Created by Nikem on 21/12/14.
 */
@Controller
class VelocityController {

  @Autowired
  TeamVelocity teamVelocity
  @Autowired
  Inflow inflow
  @Autowired
  LocalStorage storage


  @RequestMapping('/')
  def index() {
    def currentCycleBacklog = new ReleaseBacklog(storage, currentVersion()).workToDo()
    def nextCycleBacklog = new ReleaseBacklog(storage, nextVersion()).workToDo()

    new ModelAndView('index', [
        velocity           : teamVelocity.toSmoothCumulativeCsv(),
        inflow             : inflow.toSmoothCsv(),
        currentCycleBacklog: currentCycleBacklog,
        nextCycleBacklog   : nextCycleBacklog,
        totalBacklog       : currentCycleBacklog + nextCycleBacklog
    ])
  }
}
