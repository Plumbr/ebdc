package eu.plumbr.ebdc.storage

import eu.plumbr.ebdc.issue.BacklogIssue

/**
 * Created by Nikem on 20/12/14.
 */
interface LocalStorage {
  void refresh()

  Map<String, BacklogIssue> getAll()
}