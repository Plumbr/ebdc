yieldUnescaped '<!DOCTYPE html>'
html {
  head {
    title('Team velocity')
  }
  body {
    yield "Velocity: $velocity"
    newLine()
    yield "Inflow: $inflow"
    table(border: 1) {
      thead {
        th {}
        th { yield "Current cycle" }
        th { yield "Next cycle" }
        th { yield "Total for 2 cycle" }
      }
      tr {
        td { yield "Work to do" }
        td(align: 'right') { yield "${currentCycleBacklog}" }
        td(align: 'right') { yield "${nextCycleBacklog}" }
        td(align: 'right') { yield "${totalBacklog}" }
      }
    }
  }
}