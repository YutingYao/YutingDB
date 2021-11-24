package hr.fer.stflink.core.data_types

import java.sql.Timestamp
import java.util.Date;

class TimePeriod(var st: Timestamp, var en: Timestamp) {
	var _start: java.sql.Timestamp = st
	var _end: java.sql.Timestamp = en
  
    def start = _start
    def end = _end
    
    def start_= (value: Timestamp):Unit = _start = value
    def end_= (value: Timestamp):Unit = _end = value
    
    def this() = {
      this(new Timestamp((new Date()).getTime()),new Timestamp((new Date((new Date()).getTime()+ (1000 * 60 * 60 * 24))).getTime()))
	}

}