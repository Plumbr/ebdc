package eu.plumbr.ebdc.issue

import eu.plumbr.ebdc.Utils
import org.joda.time.DateTime

/**
 * Created by Nikem on 27/12/14.
 */
class ChangeLogEntry {
  String author
  String created
  List<ChangeLogItem> items

  ChangeLogEntry() {}

  ChangeLogEntry(net.rcarz.jiraclient.ChangeLogEntry entry) {
    this.author = entry.author.name
    this.created = new DateTime(entry.created).toString(Utils.DATE_FORMAT)
    this.items = entry.items.collect { new ChangeLogItem(it) }
  }
}
