package extensions

import model.Session

fun Session.getTimeSlot() = this.start.printDayHour() + " - " + this.end.printHour()

fun Session.getShortDescription() = description.getShortString()