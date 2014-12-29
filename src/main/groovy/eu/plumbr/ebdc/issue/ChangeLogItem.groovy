package eu.plumbr.ebdc.issue

class ChangeLogItem {
  String field
  String fieldType
  String from
  String fromString
  String to
  String toString

  ChangeLogItem() {}

  ChangeLogItem(net.rcarz.jiraclient.ChangeLogItem item) {
    this.field = item.field
    this.fieldType = item.fieldType
    this.from = item.from
    this.fromString = item.fromString
    this.to = item.to
    this.toString = item.toString
  }
}
