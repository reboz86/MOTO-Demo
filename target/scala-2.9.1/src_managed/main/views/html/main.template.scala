
package views.html

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.api.templates.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import com.avaje.ebean._
import play.mvc.Http.Context.Implicit._
import views.html._
/**/
object main extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Html,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(title: String)(content: Html):play.api.templates.Html = {
        _display_ {

Seq(format.raw/*1.32*/("""

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="description" content="Offloading APP">
    <meta name="author" content="Thales Communications & Security / UPMC Sorbonne Universités">

    <!-- Note there is no responsive meta tag here -->

    	<link rel="shortcut icon" href=""""),_display_(Seq(/*13.39*/routes/*13.45*/.Assets.at("images/MOTOLogo.png"))),format.raw/*13.78*/("""">
        
        <title>"""),_display_(Seq(/*15.17*/title)),format.raw/*15.22*/("""</title>
        
         <!-- Bootstrap core CSS -->
    	<link href=""""),_display_(Seq(/*18.19*/routes/*18.25*/.Assets.at("stylesheets/bootstrap.min.css"))),format.raw/*18.68*/("""" media="screen" type="text/css" rel="stylesheet">
    	
    	<script src=""""),_display_(Seq(/*20.20*/routes/*20.26*/.Assets.at("javascripts/jquery-1.7.1.min.js"))),format.raw/*20.71*/("""" type="text/javascript"></script>

    	<!-- Custom styles for this template -->
    	 <!--<link href=""""),_display_(Seq(/*23.24*/routes/*23.30*/.Assets.at("stylesheets/jumbotron-narrow.css"))),format.raw/*23.76*/("""" rel="stylesheet"-->
    </head>
    <body>
       <div class="container">
      <div class="header">
        <nav class="navbar navbar-default " role="navigation">
        <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a font size="16" class="navbar-brand" href="http://www.fp7-moto.eu/"><font size="5">FP7 MOTO</font></a>
    </div>
    
     <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" style="line-height:45px; height:45px;">
      <ul class="nav navbar-nav">
         <li class="active"><a href="#"><font size="4">Home</a></font></li>
         <li><a href="#"><font size="4">About</a></font></li>
         <li><a href="#"><font size="4">Contact</a></font></li>
         </ul>
        
  <ul class="nav navbar-nav navbar-right" >
           <li><a href="http://www.fp7-moto.eu/"><img src=""""),_display_(Seq(/*49.61*/routes/*49.67*/.Assets.at("images/MOTOLogo.png"))),format.raw/*49.100*/("""" height="30" ></a></li>
         </nav>
        </ul>
      </div>

<p>
    <!-- Main jumbotron for a primary marketing message or call to action -->
    <div class="jumbotron">
      <div class="container" align="left">
        <h1>Offloading Demo</h1>
        <p>   
</p>
<p>



<!-- upload image -->

<form id="file-form" action="handler.php" enctype="multipart/form-data" method="POST">
   <div class="row">
      <label for="fileToUpload">Select Files to Upload</label><br />
      
      <input type="file" id="file-select"  name="photo" onchange="handleFileSelect()" accept="image/*" />
      <output id="filesInfo"></output>
      
     <div class="row">
     	<div class="col-md-2">
       	<label for="timeLife">Offloading time</label>
	       <select class="form-control" style = "width:120px">
		  		<option>30 s</option>
		  		<option>60 s</option>
		  		<option>90 s</option>
		  		<option>120 s</option>
			</select>
			<br>
			<div class="col-md-2;centered">
      			<input type="submit" id="upload-button" class="btn btn-primary" value="Offload !!!" disabled="disabled"  onclick="upload(event); updateProgressBar()" style = "width:120px"</input>
     		</div>
		</div>
	
		<div class="col-md-2">
	       <label for="algo">Algorithm</label>
	      	<select class="form-control" style = "width:120px">
	  			<option>Initial</option>
	  			<option>Derivative</option>
	  			<option>Linear</option>
	  			<option>Infra</option>
			</select>
			<br>
			<div class="col-md-2;centered">
      			<input type="submit" id="panic-button" class="btn btn-danger" value="Panic !!!" disabled="disabled" onclick="panic(event)" style = "width:120px" />
      		</div>
		</div>
      
	</div>
    
     
</form>


<!-- Handle upload -->
<script>
  function handleFileSelect() """),format.raw("""{"""),format.raw/*111.32*/("""
        var oFReader = new FileReader();
        oFReader.readAsDataURL(document.getElementById("file-select").files[0]);

		 oFReader.onload = function (oFREvent)"""),format.raw("""{"""),format.raw/*115.42*/("""
		 	var span = document.createElement('span');
          span.innerHTML = ['<img class="thumbnail" src="',  oFREvent.target.result,
                             '"/>'].join('');
          document.getElementById('filesInfo').insertBefore(span, null);
          $('#upload-button').removeAttr('disabled');
         
          document.getElementById('progressouter').style.visibility="visible";
          $("#progress").css('width','0%');
		 """),format.raw("""}"""),format.raw/*124.5*/(""";
    
      """),format.raw("""}"""),format.raw/*126.8*/(""";
</script>


<!-- UPload FORM with data -->
<script>

	function upload() """),format.raw("""{"""),format.raw/*133.21*/("""
	
		var form = document.getElementById('file-form');
    	var fileSelect = document.getElementById('file-select');
    	var uploadButton = document.getElementById('upload-button');
    	
    	form.onsubmit = function(event) """),format.raw("""{"""),format.raw/*139.39*/("""
    		
    		event.preventDefault();
    		var files = fileSelect.files;
        	var formData = new FormData();
        	var file = files[0];

        	 // Add the file to the request.
        	formData.append('photo', file, file.name);
        	
        	// Set up the request.
       		var xhr = new XMLHttpRequest();

	        // Try to send the data.
	        try
	    	"""),format.raw("""{"""),format.raw/*154.8*/("""
	    		// Open the connection.
	        	xhr.open('POST', 'handler.php', true);
	        	xhr.send(formData);
	        	
	        """),format.raw("""}"""),format.raw/*159.11*/("""
	        catch(e)
	        """),format.raw("""{"""),format.raw/*161.11*/("""
	        	 // display status message
      			alert("There was a problem retrieving the data:\n" + xhr.statusText);
	        """),format.raw("""}"""),format.raw/*164.11*/("""
	        	
	       $("#progress").css('width','0%');
	      /* and display the numeric value */
	      $("#progress").html('0%');
	       $('#panic-button').removeAttr('disabled');
	        
	    """),format.raw("""}"""),format.raw/*171.7*/(""";
	"""),format.raw("""}"""),format.raw/*172.3*/(""";

</script>

<!-- Send a PANIC request -->
<script>

	function panic() """),format.raw("""{"""),format.raw/*179.20*/("""
		// Set up the request.
		var xhr = new XMLHttpRequest();
		
		// Open the connection.
        xhr.open('GET', 'panic.php', true);
        
        // Send the Data.
        xhr.send(null);
		
	"""),format.raw("""}"""),format.raw/*189.3*/(""";

</script>

<br>
<div class="progress progress-striped active" id ="progressouter" style="visibility:hidden">
  <div class="progress-bar" id = "progress" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 40%" >
    <span class="sr-only">0% Complete</span>
  </div>
</div></p>


<!-- Handle AJAX requests for progress bar -->
<script type="text/javascript">
function updateProgressBar()"""),format.raw("""{"""),format.raw/*203.30*/("""

	$(document).ready(function()"""),format.raw("""{"""),format.raw/*205.31*/("""
  	var progresspump = setInterval(function()"""),format.raw("""{"""),format.raw/*206.46*/("""
    	/* query the completion percentage from the server */
    	$.get("/update", function(data)"""),format.raw("""{"""),format.raw/*208.38*/("""
      	/* update the progress bar width */
	      $("#progress").css('width',data+'%');
	      /* and display the numeric value */
	      $("#progress").html(data+'%');

	      /* test to see if the job has completed */
	      if(data > 99.999) """),format.raw("""{"""),format.raw/*215.27*/("""
	        clearInterval(progresspump);
	        $("#progressouter").removeClass("active");
	        $("#progress").html("Done");
	      """),format.raw("""}"""),format.raw/*219.9*/("""
	    """),format.raw("""}"""),format.raw/*220.7*/(""")
	  """),format.raw("""}"""),format.raw/*221.5*/(""", 1000);"""),format.raw("""}"""),format.raw/*221.14*/(""");
  """),format.raw("""}"""),format.raw/*222.4*/("""
  </script>

<div>
</div>
<!-- FOOTER -->
<div class = "navbar navbar-default navbar-fixed-bottom" style="height: 2px;">
	<div class = "container-fluid" >
		<p class= "navbar-text pull-left" ><font size="2">©<a href="http://thalesgroup.com"> Thales Communications &amp; Security</a> and <a href="http://upmc.fr">UPMC Sorbonne Universités</a> 2013-2014 
		· <a href="#">Privacy</a> · <a href="#">Terms</a> </p> <p class = "navbar-text pull-right"> <a href="#"><font size="2">Back to top</a></p> 
	</div>
</div>



    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src=""""),_display_(Seq(/*241.19*/routes/*241.25*/.Assets.at("javascripts/bootstrap.min.js"))),format.raw/*241.67*/(""""></script>
    
    </body>
</html>
"""))}
    }
    
    def render(title:String,content:Html) = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.Html) = (title) => (content) => apply(title)(content)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Tue Apr 08 10:21:47 CEST 2014
                    SOURCE: /home/filippo/git/MOTO-Demo/MotoOffloading/app/views/main.scala.html
                    HASH: b877cb283563b84a4fb2169e591f5feaf44a98e6
                    MATRIX: 759->1|861->31|1267->406|1282->412|1337->445|1396->473|1423->478|1527->551|1542->557|1607->600|1714->676|1729->682|1796->727|1932->832|1947->838|2015->884|3264->2102|3279->2108|3335->2141|5162->3920|5375->4085|5865->4528|5926->4542|6049->4617|6323->4843|6747->5220|6927->5352|7004->5381|7179->5508|7424->5706|7475->5710|7596->5783|7840->5980|8315->6407|8395->6439|8489->6485|8634->6582|8929->6829|9113->6966|9167->6973|9220->6979|9277->6988|9330->6994|10154->7786|10170->7792|10235->7834
                    LINES: 27->1|30->1|42->13|42->13|42->13|44->15|44->15|47->18|47->18|47->18|49->20|49->20|49->20|52->23|52->23|52->23|78->49|78->49|78->49|140->111|144->115|153->124|155->126|162->133|168->139|183->154|188->159|190->161|193->164|200->171|201->172|208->179|218->189|232->203|234->205|235->206|237->208|244->215|248->219|249->220|250->221|250->221|251->222|270->241|270->241|270->241
                    -- GENERATED --
                */
            