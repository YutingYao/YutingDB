package hr.fer.stflink.core.data_types

import java.sql.Timestamp
import java.util.Date


case class TimeInterval(override var start: Timestamp, override var end: Timestamp, lc: Boolean, rc: Boolean) extends TimePeriod (start, end){

  private var _leftClosed: Boolean = lc
  private var _rightClosed: Boolean = rc
  
  def leftClosed = _leftClosed
  def rightClosed = _rightClosed

  
  def leftClosed_= (value: Boolean):Unit = _leftClosed = value
  def rightClosed_= (value: Boolean):Unit = _rightClosed = value


  def this() = {
    this(new Timestamp((new Date()).getTime()),new Timestamp((new Date((new Date()).getTime()+ (1000 * 60 * 60 * 24))).getTime()),true,false)
  }

}
