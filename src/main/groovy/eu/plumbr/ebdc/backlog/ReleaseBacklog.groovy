package eu.plumbr.ebdc.backlog

import eu.plumbr.ebdc.storage.JsonLocalStorage
import eu.plumbr.ebdc.storage.LocalStorage

import static eu.plumbr.ebdc.issue.VersionFactory.currentVersion
import static eu.plumbr.ebdc.issue.VersionFactory.nextVersion

/**
 * Created by Nikem on 27/01/15.
 */
class ReleaseBacklog {
  LocalStorage storage
  String version

  ReleaseBacklog(LocalStorage storage, String version) {
    this.storage = storage
    this.version = version
  }

  int workToDo() {
    storage.all.values()
        .findAll { it.resolution == null && it.currentFixVersion() == version }
        .collect {
      println it.key
      it.size()
    }
    .sum()
  }

  public static void main(String[] args) {
    def backlog = new ReleaseBacklog(new JsonLocalStorage(null), currentVersion())
    println backlog.workToDo()
    backlog = new ReleaseBacklog(new JsonLocalStorage(null), nextVersion())
    println backlog.workToDo()
  }

}
