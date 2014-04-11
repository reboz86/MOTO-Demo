// @SOURCE:/home/filippo/git/MOTO-Demo/MotoOffloading/conf/routes
// @HASH:292e628b74063d37634ef15e3b0a5a1abd43d056
// @DATE:Tue Apr 08 10:21:27 CEST 2014

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString

object Routes extends Router.Routes {


// @LINE:6
val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart("/"))))
                    

// @LINE:9
val controllers_Assets_at1 = Route("GET", PathPattern(List(StaticPart("/assets/"),DynamicPart("file", """.+"""))))
                    

// @LINE:11
val controllers_Application_offload2 = Route("POST", PathPattern(List(StaticPart("/handler.php"))))
                    

// @LINE:13
val controllers_Application_update3 = Route("GET", PathPattern(List(StaticPart("/update"))))
                    
def documentation = List(("""GET""","""/""","""controllers.Application.index()"""),("""GET""","""/assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""),("""POST""","""/handler.php""","""controllers.Application.offload()"""),("""GET""","""/update""","""controllers.Application.update()"""))
             
    
def routes:PartialFunction[RequestHeader,Handler] = {        

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Nil))
   }
}
                    

// @LINE:9
case controllers_Assets_at1(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(_root_.controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String])))
   }
}
                    

// @LINE:11
case controllers_Application_offload2(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.offload(), HandlerDef(this, "controllers.Application", "offload", Nil))
   }
}
                    

// @LINE:13
case controllers_Application_update3(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.update(), HandlerDef(this, "controllers.Application", "update", Nil))
   }
}
                    
}
    
}
                