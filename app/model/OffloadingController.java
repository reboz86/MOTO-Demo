package model;

import strategy.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.Response.ResponseType;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.commons.codec.binary.Base64;



import controllers.Application;


public class OffloadingController {

	protected static String feedSyncServerAddress = " http://localhost";
	protected static int feedSyncServerPort = 8083;
	protected static int timeLife = 60;
	protected static int panicZone = 5;
	private static boolean forcePanic; 

	protected static Set<IRI> userIDs = new HashSet<IRI>();
	private static Map<IRI, Set<IRI>> msg_infected = new HashMap<IRI, Set<IRI>>();
	private static Map<IRI, Set<IRI>> msg_sane = new HashMap<IRI, Set<IRI>>();

	static Timer timer;

	public OffloadingController(){ }
	
	public OffloadingController(String address, int port, int timeLife, int panic){
		// Initialize mapping 
		msg_infected.clear();
		msg_sane.clear();
		
		forcePanic = false;

		// initialize parameters
		feedSyncServerAddress = address;
		feedSyncServerPort = port;
		OffloadingController.timeLife = timeLife;
		panicZone = panic;

	}


	public static void armOffloading( IRI updateLink){
		
		// Fill the map with content and IDs to diffuse 
		msg_sane.put(updateLink,userIDs); 
		msg_infected.put(updateLink, new HashSet<IRI>());
		
		// Execute offloading control  
		timer = new Timer();
		MessageUpdateTrigger reInjectionHandler = new MessageUpdateTrigger(updateLink);
		// scheduling the task at fixed rate
		timer.scheduleAtFixedRate(reInjectionHandler,new Date(),500L);  

	}


	public static void getUserList()throws InterruptedException{

		// REST Client invocation
		Abdera abdera = new Abdera();
		AbderaClient abderaClient = new AbderaClient(abdera);

		// retrieve the list of IDs in the system
		System.out.println("Offloading Controller: connecting to "+ feedSyncServerAddress+":"+feedSyncServerPort+"...");

		ClientResponse resp = abderaClient.get(feedSyncServerAddress+":"+feedSyncServerPort+"/feedsync/rest/myfeeds/5/entries");

		// parse response
		if (resp.getType() == ResponseType.SUCCESS) {

			System.out.println("Offloading Controller: retrieved the user list");

			Document<Feed> doc = resp.getDocument();
			//IRI contentID=doc.getRoot().getId();

			System.out.println("Offloading Controller: "+ doc.getRoot().getEntries().size() + " UEs waiting the content.");
			if(!doc.getRoot().getEntries().isEmpty()){
				for(int i=0;i<doc.getRoot().getEntries().size();i++){
					userIDs.add(doc.getRoot().getEntries().get(i).getId()); // IDs of UEs are added in UserIDs
					//System.out.println(doc.getRoot().getEntries().get(i).getId());
				}
			} else {
				System.err.println("Error getting the list of users");
				throw new InterruptedException();
			}

		}else {
			System.err.println("Error getting the list of users");
			throw new InterruptedException();
		}

	}

	public static IRI postContent( File fileName ) throws Exception{ 

		Abdera abdera = new Abdera();
		AbderaClient abderaClient = new AbderaClient(abdera);

		byte[] bytes = loadFile(fileName);
		byte[] encoded = Base64.encodeBase64(bytes);

		// create the new entry
		Entry newEntry = abdera.newEntry();
		//newEntry.setId("thales:example-content");
		//newEntry.addAuthor("Admin");
		newEntry.setTitle("Photo");
		newEntry.setUpdated(new Date());
		newEntry.setContent(new String(encoded), Content.Type.MEDIA);

		long postTime=System.currentTimeMillis();
		// post the content
		ClientResponse  resp= abderaClient.post(feedSyncServerAddress+":"+feedSyncServerPort+"/feedsync/rest/myfeeds/9/entries", newEntry);
		System.out.println("post time: "+(System.currentTimeMillis()-postTime)/(double)1000);

		if (resp.getType() == ResponseType.SUCCESS) {
			IRI editUri = resp.getLocation();
			return editUri;
		}else {
			System.err.println(resp.toString());
			throw new InterruptedException();
		}


	}

	private static byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}
		byte[] bytes = new byte[(int)length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+file.getName());
		}

		is.close();
		return bytes;
	}
	
	public static void forcePanic(boolean panic) {forcePanic = panic;}


	// This is a separate thread
	static class MessageUpdateTrigger extends TimerTask{

		//protected Set<Integer> present_ids = new HashSet<Integer>();
		long startTime;
		WhoToPush who_to_push;
		NumToPush num_to_push;
		IRI msgID;
		HashSet<IRI> sane, infected, ueToPush;


		public MessageUpdateTrigger(IRI msgID){

			//present_ids.clear();
			startTime = System.currentTimeMillis();
			num_to_push = (NumToPush) new InitialPush();
			who_to_push = (WhoToPush) new RandomWho();
			this.msgID = msgID;
		}

		@Override
		public void run() {

			//System.out.println("Offloading Controller: Running");

			sane = new HashSet<IRI>( msg_sane.get(msgID));
			infected =  new HashSet<IRI>(msg_infected.get(msgID));
			ueToPush =  new HashSet<IRI>();


			// Retrieve the list of UEs that acked reception of the content	
			try {
				getAckList( msgID);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// PANIC ZONE: send to all the uninfected UEs
			if ( panic(forcePanic) ){

				Iterator<IRI> i = sane.iterator();
				while ( i.hasNext() ){
					IRI to = i.next();

					infected.add(to);
					ueToPush.add(to);

					msg_sane.get(msgID).remove(to);
					msg_infected.get(msgID).add(to);
					
					// update the back-end server
					Application.updateCompletion(msg_infected.get(msgID).size()/userIDs.size()*100);

					i.remove();
				}

			} else {
				// Compute the number of copies to send at time t
				int n = num_to_push.numToPush( ( new Date()).getTime() - startTime, infected.size(),sane.size() );

				//System.out.println("Offloading Controller:numToPush: " +n );
				n = Math.min(n, sane.size());
				for(int i=0; i<n; ++i){

					//Compute to whom to send the copies
					IRI to = who_to_push.whoToPush(infected, sane);

					//System.out.println("Offloading Controller:whoToPush: " +to );
					if ( to == null )
						break;

					//update of the local Set (sane and infected) and overall Map (msg_sane and msg_infected)
					sane.remove(to);
					infected.add(to);

					msg_sane.get(msgID).remove(to);
					msg_infected.get(msgID).add(to);
					
					// update the back-end server
					Application.updateCompletion(msg_infected.get(msgID).size()/userIDs.size()*100);

					ueToPush.add(to);
				}
			}

			if(!ueToPush.isEmpty()){

				try {
					putDistributionList(msgID, ueToPush);
				} catch (InterruptedException e) {
					System.out.println("Interrupted !!!");
					e.printStackTrace();
				}
				System.out.println("Offloading Controller: Injection of "+ueToPush.size()+" copies after t = "+(( new Date()).getTime() - startTime)/1000);
			}else
				System.out.println("Offloading Controller: No injection after t="+(( new Date()).getTime() - startTime)/1000);

			// exiting when panic has been computed 
			if ( panic(forcePanic) ){
				System.out.println("Offloading Controller... Exiting");
				timer.cancel();
				timer.purge();
			}
		}

		private boolean panic(boolean forcePanic){ return forcePanic ? true :(( new Date()).getTime() - startTime >= (OffloadingController.timeLife - OffloadingController.panicZone) * 1000 );}

		
		private static void putDistributionList( IRI editUri, HashSet<IRI> idUes) throws InterruptedException{

			Abdera abdera = new Abdera();
			AbderaClient abderaClient = new AbderaClient(abdera);

			ClientResponse resp = abderaClient.get(editUri.toString());

			if (resp.getType() == ResponseType.SUCCESS) {

				Document<Entry> doc = resp.getDocument();
				//System.out.println("Offloading Controller: retrieved the required entry: "+ doc.getRoot().toString());

				doc.getRoot().addAuthor(idUes.toString().substring(1, idUes.toString().length() - 1));
				doc.getRoot().setUpdated(new Date());

				//System.out.println("putDistributionList put document: " +doc.getRoot().toString());
				abderaClient.put(editUri.toString(), doc.getRoot());


			}else {
				System.err.println("Error pushing the  distribution list of users");
				throw new InterruptedException();
			}

		}

		private static void getAckList( IRI editUri ) throws InterruptedException{

			Abdera abdera = new Abdera();
			AbderaClient abderaClient = new AbderaClient(abdera);

			//retrieve the content ID to look for
			String contentID =editUri.toASCIIString().substring(editUri.toASCIIString().lastIndexOf("/")+1);

			long getTime=System.currentTimeMillis();

			// Retrieve the entry with acked users for content with ID contentID
			ClientResponse resp = abderaClient.get(feedSyncServerAddress+":"+feedSyncServerPort+"/feedsync/rest/myfeeds/10/entries/"+contentID+"/ack");
			System.out.println("getTime: "+ (System.currentTimeMillis()-getTime)/(double)1000);
			// parse response
			if (resp.getType() == ResponseType.SUCCESS ) {

				Document<Entry> doc = resp.getDocument();
				//IRI contentID=doc.getRoot().getId();


				if(doc.getRoot().getAuthor()!= null) {
					// we have some users that acked the content reception
					HashSet<IRI> infected, sane; 
					sane = new HashSet<IRI>( msg_sane.get(editUri));
					infected =  new HashSet<IRI>(msg_infected.get(editUri));

					// Extract Acked recipients
					StringTokenizer st ;
					if (doc.getRoot().getAuthor().getName() !=null){
						st = new StringTokenizer(doc.getRoot().getAuthor().getName().toString(), ",");

						while(st.hasMoreTokens()){
							String token = st.nextToken();
							infected.add(new IRI(token));
							sane.remove(new IRI(token));
						}
						// Insert updated SETs
						msg_sane.put(editUri, sane);
						msg_infected.put(editUri,infected);
						System.out.println("List of infected (Acked) users: "+infected.toString());
						
						// update the back-end server
							Application.updateCompletion(msg_infected.get(editUri).size()/userIDs.size()*100);
					}
				}
			}else {
				System.err.println("No users acked yet for content "+ contentID);
				//throw new InterruptedException();
			}
		}
	}

}
