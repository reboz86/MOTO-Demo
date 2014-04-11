// @SOURCE:/home/filippo/git/MOTO-Demo/MotoOffloading/conf/routes
// @HASH:292e628b74063d37634ef15e3b0a5a1abd43d056
// @DATE:Tue Apr 08 10:21:27 CEST 2014

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString


// @LINE:13
// @LINE:11
// @LINE:9
// @LINE:6
package controllers {

// @LINE:13
// @LINE:11
// @LINE:6
class ReverseApplication {
    


 
// @LINE:13
def update() = {
   Call("GET", "/update")
}
                                                        
 
// @LINE:6
def index() = {
   Call("GET", "/")
}
                                                        
 
// @LINE:11
def offload() = {
   Call("POST", "/handler.php")
}
                                                        

                      
    
}
                            

// @LINE:9
class ReverseAssets {
    


 
// @LINE:9
def at(file:String) = {
   Call("GET", "/assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                        

                      
    
}
                            
}
                    


// @LINE:13
// @LINE:11
// @LINE:9
// @LINE:6
package controllers.javascript {

// @LINE:13
// @LINE:11
// @LINE:6
class ReverseApplication {
    


 
// @LINE:13
def update = JavascriptReverseRoute(
   "controllers.Application.update",
   """
      function() {
      return _wA({method:"GET", url:"/update"})
      }
   """
)
                                                        
 
// @LINE:6
def index = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"/"})
      }
   """
)
                                                        
 
// @LINE:11
def offload = JavascriptReverseRoute(
   "controllers.Application.offload",
   """
      function() {
      return _wA({method:"POST", url:"/handler.php"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:9
class ReverseAssets {
    


 
// @LINE:9
def at = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"/assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                                                        

                      
    
}
                            
}
                    


// @LINE:13
// @LINE:11
// @LINE:9
// @LINE:6
package controllers.ref {

// @LINE:13
// @LINE:11
// @LINE:6
class ReverseApplication {
    


 
// @LINE:13
def update() = new play.api.mvc.HandlerRef(
   controllers.Application.update(), HandlerDef(this, "controllers.Application", "update", Seq())
)
                              
 
// @LINE:6
def index() = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq())
)
                              
 
// @LINE:11
def offload() = new play.api.mvc.HandlerRef(
   controllers.Application.offload(), HandlerDef(this, "controllers.Application", "offload", Seq())
)
                              

                      
    
}
                            

// @LINE:9
class ReverseAssets {
    


 
// @LINE:9
def at(path:String, file:String) = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]))
)
                              

                      
    
}
                            
}
                    
                