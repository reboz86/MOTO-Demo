package controllers;

import java.io.File;

import org.apache.abdera.i18n.iri.IRI;

import play.*;
import play.mvc.*;
import views.html.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import model.OffloadingController;



public class Application extends Controller {

	static OffloadingController offloading; 
	volatile static int completion=0;

	public static Result index() throws InterruptedException {

		offloading = new OffloadingController();
		
		OffloadingController.getUserList();

		return ok(index.render("FP7 MOTO"));
	}

	// The offload should start here 
	public static Result offload(){
		MultipartFormData body  = request().body().asMultipartFormData();
		FilePart picture = body.getFile("photo");
		if (picture != null) {
			File file = picture.getFile();
			
			// post the content to be offloaded into feedSync
			IRI updateLink;
			try {
				updateLink = OffloadingController.postContent(file);
			} catch (Exception e) {
				return ok("Error!! ");//internalServerError(e.toString());
			}
			completion=0;
			OffloadingController.armOffloading(updateLink);
			return ok(Integer.toString(completion));
		} else {
			System.out.println("ERROR");
			flash("error", "Missing file");
			return ok("Error!! ");
		}
	}

	public static Result update() {
		return ok(Integer.toString(completion));
	}

	public static void panic() {
		OffloadingController.forcePanic(true);
	}
	
	public static void updateCompletion(int i){	 
		
		System.out.println("completion = "+i);
		completion = i; 
		}

}