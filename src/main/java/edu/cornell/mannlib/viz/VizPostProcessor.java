package edu.cornell.mannlib.viz;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.cornell.mannlib.viz.collaboration.globalcollabharvester.GlobalCollaborationHarvesterEntryPoint;
import edu.cornell.mannlib.viz.collaborationwheel.CollabVizDataGenerator;
import edu.cornell.mannlib.viz.config.Configuration;
import edu.cornell.mannlib.viz.journalsubjectareamapper.JournalToSubjectAreaMapEntryPoint;
import edu.cornell.mannlib.viz.keywordcloudgenerator.SiteLevelKeywordCloudGenerator;
import edu.cornell.mannlib.viz.keywordminer.article.ArticleKeywordMinerEntryPoint;


public class VizPostProcessor {

	private static final Logger LOGGER = Logger.getLogger( VizPostProcessor.class.getName() );

	public static void main(String[] args) {
		try {
			if(args.length > 0){
				init(args[0]);
			}else{
				String propFilePath = "setup.properties";
				init(propFilePath);
			}
			VizPostProcessor mep = new VizPostProcessor();
			mep.runProcess();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init(String propFilePath) throws IOException{
		String date = getCurrentDate();
		Configuration.setDate(date);
		initConfiguration(date, propFilePath);
	}

	private void runProcess() {

		//Run the InferredKeywords Process
		LOGGER.info("\n\n---------- STARTING INFERRED KEYWORD HARVEST PROCESS----------");
		try{
			runInferredKeywordProcess();
		}catch(Exception exp){
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}

		//Run the Home page WordCloud Generator Process
		LOGGER.info("\n\n---------- STARTING HOMEPAGE LEVEL KEYWORD CLOUD GENERATOR PROCESS----------");
		try{
			runHomepageKeywordCloudProcess();
		}catch(Exception exp){
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}

		//Run the Grants data process
		/*LOGGER.info("\n\n---------- STARTING OSP GRANTS DATA PROCESS----------");
		try{
			runGrantsDataProcess();
		}catch(Exception exp){
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}*/

		//Run the journal to subject area map process
		LOGGER.info("\n\n---------- STARTING JOURNAL TO SUBJECT AREA MAP PROCESS----------");
		try{
			runJournalSubjectAreaMapProcess();
		}catch(Exception exp){
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}

		//Run the collaboration data process
		LOGGER.info("\n\n---------- STARTING COLLABORATION DATA PROCESS----------");
		try{
			runCollaborationDataProcess();
		}catch(Exception exp){
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}
		
		// Run the collaboration data process
		LOGGER.info("\n\n---------- STARTING COLLABORATION DATA PROCESS----------");
		try {
			runCollaborationProcess();
		} catch (Exception exp) {
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}

		//Run the grant data process
		/*LOGGER.info("\n\n---------- STARTING GRANTS INFERRED KEYWORDS DATA PROCESS----------");
		try{
			runGrantInferredKeywordsProcess();
		}catch(Exception exp){
			LOGGER.log(Level.WARNING, "\n\n---------ERROR OCCURED:----------", exp);
		}*/


		LOGGER.info("\n\n---------- ALL PROCESSES COMPLETED----------");
	}

	/**
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	/*private void runGrantInferredKeywordsProcess() throws IOException, ParserConfigurationException, SAXException {
		GrantKeywordMinerEntryPoint obj = new GrantKeywordMinerEntryPoint();
		obj.runProcess();
	}*/

	/**
	 * 
	 * @throws IOException
	 */
 	private void runCollaborationDataProcess() throws IOException {
 		CollabVizDataGenerator obj = new CollabVizDataGenerator();
 		obj.runProcess();
 	}
 	
 	private void runCollaborationProcess() {
		try {
			GlobalCollaborationHarvesterEntryPoint obj = new GlobalCollaborationHarvesterEntryPoint();
			obj.runProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads all the articles and infer the keywords. Reason to read all the articles 
	 * is as we do not know which article is updated in near past. This means we need to 
	 * process all the articles for now and inferred keyword graph has to be replaced 
	 * with every call.
	 * 
	 * Inferred Keyword Cloud Graph has to be replaced every time.
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private void runInferredKeywordProcess() throws IOException, ParserConfigurationException, SAXException {
		ArticleKeywordMinerEntryPoint kmep = new ArticleKeywordMinerEntryPoint();
		kmep.runProcess();
	}

	/**
	 * Reads all keywords, concepts and inferred terms. 
	 * Currently the word type is only one, either KEYWORD or Concept. 
	 * It could be either or both of them. Once, we start inferring from 
	 * additional vocabularies, the type could be
	 * KEYWORD, Concept or INFERRED.
	 * 
	 * JSON file has to be replaced every time.
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private void runHomepageKeywordCloudProcess() throws IOException, ParserConfigurationException, SAXException {
		SiteLevelKeywordCloudGenerator obj = new SiteLevelKeywordCloudGenerator();
		obj.runProcess();	
	}

	/**
	 * Reads the All-Grant file and identifies new grants.
	 * CSV and RDF is generated only for the new grants.
	 * 
	 * New RDF triples should be added in the existing grants graph.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	/*private void runGrantsDataProcess() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException {
		OSPGrantsEntryPoint ospep = new OSPGrantsEntryPoint();
		ospep.runProcess();
	}*/

	/**
	 * The query will return only those Journals that do not have any subject area associated.
	 * This means, we will have RDF triples for only new Journals.
	 * 
	 * New RDF triples should be added in the existing journal 2 subject area graph.
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void runJournalSubjectAreaMapProcess() throws NoSuchAlgorithmException, IOException, InterruptedException {
		JournalToSubjectAreaMapEntryPoint jrnlep = new JournalToSubjectAreaMapEntryPoint();
		jrnlep.runProcess();
	}


	private static String getCurrentDate() {
		String date = null;
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		date = dateFormatter.format(now);
		LOGGER.info(date);
		return date;
	}

	private static void initConfiguration(String date, String propFilePath) throws IOException {

		// SET FILEPATHS and other Properties
		SetupPropertyValues properties = new SetupPropertyValues();
		Map<String, String> map = properties.getPropValues(propFilePath);
		Configuration.setQUERY_RESULTSET_FOLDER(map.get("QUERY_RESULTSET_FOLDER"));
		Configuration.setPOSTPROCESS_RESULTSET_FOLDER(map.get("POSTPROCESS_RESULTSET_FOLDER"));
		Configuration.setSUPPL_FOLDER(map.get("SUPPL_FOLDER"));
		Configuration.setVIVO_NAMESPACE(map.get("VIVO_NAMESPACE"));

		// CREATE NEW DIRECTORIES
		createFolder(new File(Configuration.POSTPROCESS_RESULTSET_FOLDER+"/"+date+"/"+Configuration.GRANTS_FOLDER));
		createFolder(new File(Configuration.POSTPROCESS_RESULTSET_FOLDER+"/"+date+"/"+Configuration.COLLABORATION_FOLDER+"/"+Configuration.COLLAB_EXTERNAL_FOLDER));
		createFolder(new File(Configuration.POSTPROCESS_RESULTSET_FOLDER+"/"+date+"/"+Configuration.COLLABORATION_FOLDER+"/"+Configuration.COLLAB_INTERNAL_FOLDER));
		createFolder(new File(Configuration.POSTPROCESS_RESULTSET_FOLDER+"/"+date+"/"+Configuration.INFERRED_KEYWORDS_FOLDER));
		createFolder(new File(Configuration.POSTPROCESS_RESULTSET_FOLDER+"/"+date+"/"+Configuration.HOMEPAGE_KEYWORD_CLOUD_FOLDER));
		createFolder(new File(Configuration.POSTPROCESS_RESULTSET_FOLDER+"/"+date+"/"+Configuration.SUBJECTAREA_FOLDER));
	}

	private static void createFolder(File file) {
		if (!file.exists()) {
			if (file.mkdirs()) {
				LOGGER.info(file.getAbsolutePath()+" folder created!");
			} else {
				LOGGER.throwing("KWCloudPostProcessor", "createFolder", new Throwable("EXCEPTION: Could not create folder..."));
			}
		}
	}
}
