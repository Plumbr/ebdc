package eu.plumbr.ebdc.velocity

import eu.plumbr.ebdc.Utils
import eu.plumbr.ebdc.issue.BacklogIssue
import eu.plumbr.ebdc.storage.JsonLocalStorage
import eu.plumbr.ebdc.storage.LocalStorage
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Created by Nikem on 27/12/14.
 */
@Component
class TeamVelocity {
  private Map<String, Map<Integer, Integer>> personToWeekToIssueCount
  private LocalStorage localStorage

  @Autowired
  TeamVelocity(LocalStorage localStorage) {
    this.localStorage = localStorage
  }

  String toCsv() {
    refreshCounts()
    def personalLines = personToWeekToIssueCount.collect { k, v ->
      ([k] + v.values().collect { it.toString() }).join(';')
    }

    personalLines.add(0, getHeader())
    return personalLines.join('\n')
  }

  String toCumulativeCsv() {
    refreshCounts()
    Map weekCounts = cumulativeWeekCounts()
    String headerRow = weekCounts.keySet().collect { it.toString() }.join(';')
    headerRow + '\n' + weekCounts.values().join(';')
  }

  private Map cumulativeWeekCounts() {
    def weekCounts = new TreeMap<>().withDefault { 0 }
    personToWeekToIssueCount.each { k, v ->
      v.each { week, count ->
        weekCounts[week] = weekCounts[week] + count
      }
    }
    weekCounts.remove(0)
    weekCounts
  }

  String toSmoothCumulativeCsv() {
    refreshCounts()
    def statistics = new DescriptiveStatistics()
    statistics.setWindowSize(12)

    def result = new TreeMap<>().withDefault { 0 }

    Map weekCounts = cumulativeWeekCounts()
    weekCounts.each { k, v ->
      statistics.addValue(v as double)
      result[k] = statistics.getMean() as int
    }

    String headerRow = result.keySet().collect { it.toString() }.join(';')
    headerRow + '\n' + result.values().join(';')
  }

  private String getHeader() {
    String anyKey = personToWeekToIssueCount.keySet().first()
    'Developer;' + personToWeekToIssueCount[anyKey].collect { k, v -> k }.join(';')
  }

  private void refreshCounts() {
    personToWeekToIssueCount = new TreeMap<>().withDefault {
      new TreeMap<>().withDefault { 0 }
    }

    def allIssues = localStorage.all
    allIssues.values().each { BacklogIssue issue ->
      personToWeekToIssueCount[issue.assignee ?: 'unassigned'][issue.weekFixed()] = personToWeekToIssueCount[issue.assignee ?: 'unassigned'][issue.weekFixed()] + issue.size()
    }

    fillAbsentWeeks(allIssues)
  }

  private void fillAbsentWeeks(Map<String, BacklogIssue> allIssues) {
    def allPresentWeeks = allIssues.values().collect { it.weekFixed() }.findAll { it != 0 }
    def allWeeksInBetween = Utils.allWeeksBetween(allPresentWeeks.min(), allPresentWeeks.max())

    personToWeekToIssueCount.values().each { Map personMap ->
      allWeeksInBetween.each {
        personMap.putIfAbsent(it, 0)
      }
    }
  }

  public static void main(String[] args) {
    def teamVelocity = new TeamVelocity(new JsonLocalStorage(null))
//    new File('teamVelocity.csv').text = teamVelocity.toCsv()
//    new File('teamCumulativeVelocity.csv').text = teamVelocity.toCumulativeCsv()
//    new File('teamSmoothCumulativeVelocity.csv').text = teamVelocity.toSmoothCumulativeCsv()

    teamVelocity.refreshCounts()
    teamVelocity.personToWeekToIssueCount.each { String dev, def weekToCount ->
      def total = weekToCount.findAll { k, v -> k > 201400 && k < 201500 }.values().sum()
      println "$dev has done $total last year"
    }
  }
}
