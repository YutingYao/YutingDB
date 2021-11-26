package models

import java.util

  case class ActorObj(id:String,`type`:String)
  case class PDataObj(id:String,ver:String,pid:String)
  case class ContextObj(channel:String,pdata:PDataObj,env:String,sid:String,did:String,cdata:util.ArrayList[CDataObj],rollup:util.Map[String,String],uid:String)
  case class CDataObj(id:String,`type`:String)
  case class EventObj(eid:String,ets:Long,ver:String,mid:String, actor:ActorObj,context:ContextObj,`object`:Any,tags:util.ArrayList[String],edata:util.Map[String,AnyRef])




