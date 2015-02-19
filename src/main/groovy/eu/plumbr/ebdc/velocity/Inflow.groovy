package eu.plumbr.ebdc.velocity

import eu.plumbr.ebdc.Utils
import eu.plumbr.ebdc.issue.BacklogIssue
import eu.plumbr.ebdc.issue.Version
import eu.plumbr.ebdc.storage.JsonLocalStorage
import eu.plumbr.ebdc.storage.LocalStorage
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Inflow {
  private LocalStorage localStorage
  Map<BigDecimal, Integer> inflowPerWeek


  @Autowired
  Inflow(LocalStorage localStorage) {
    this.localStorage = localStorage
  }


  private void refreshCounts() {
    inflowPerWeek = new TreeMap<>().withDefault { 0 }

    def allIssues = localStorage.all.findAll { k, BacklogIssue issue -> !(issue.status == 'Closed' && issue.resolution != 'Fixed') }
    allIssues.values().each { BacklogIssue issue ->
      try {
        def firstFixVersion = issue.firstFixVersion()
        def weekFirstPlanned = issue.weekFirstPlanned()

        if (firstFixVersion != null && new Version(firstFixVersion).start() < issue.firstPlanned()) {
          if (weekFirstPlanned == 201503) {
            println "${issue.key} came in $weekFirstPlanned, size ${issue.size()}"
          }
          inflowPerWeek[weekFirstPlanned] = inflowPerWeek[weekFirstPlanned] + issue.size()
        }
      } catch (NumberFormatException ignored) {
      }
    }

    fillAbsentWeeks(allIssues)
    println inflowPerWeek
  }

  String toSmoothCsv() {
    refreshCounts()
    def statistics = new DescriptiveStatistics()
    statistics.setWindowSize(12)

    def result = new TreeMap<>().withDefault { 0 }

    inflowPerWeek.each { k, v ->
      statistics.addValue(v as double)
      result[k] = statistics.getMean() as int
    }

    String headerRow = result.keySet().collect { it.toString() }.join(';')
    headerRow + '\n' + result.values().join(';')
  }


  private void fillAbsentWeeks(Map<String, BacklogIssue> allIssues) {
    def allPresentWeeks = allIssues.values().collect { it.weekFixed() }.findAll { it != 0 }
    def allWeeksInBetween = Utils.allWeeksBetween(allPresentWeeks.min(), allPresentWeeks.max())

    allWeeksInBetween.each {
      inflowPerWeek.putIfAbsent(it, 0)
    }
  }

  public static void main(String[] args) {
    def inflow = new Inflow(new JsonLocalStorage(null))
    println inflow.toSmoothCsv()
  }

}
