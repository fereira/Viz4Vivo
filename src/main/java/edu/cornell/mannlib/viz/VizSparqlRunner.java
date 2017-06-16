package edu.cornell.mannlib.viz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.sparql.resultset.ResultsFormat;

import edu.cornell.mannlib.viz.config.Configuration; ;

public class VizSparqlRunner {
	private static final Logger LOGGER = Logger.getLogger( VizSparqlRunner.class.getName() );
	 
	private static String currentDate;
	
	protected static HashMap<String, ResultsFormat> formatSymbols = new HashMap<String, ResultsFormat>();
    static { 
    	    
            formatSymbols.put(ResultsFormat.FMT_RS_XML.getSymbol(), ResultsFormat.FMT_RS_XML);
            formatSymbols.put(ResultsFormat.FMT_RDF_XML.getSymbol(), ResultsFormat.FMT_RDF_XML);
            formatSymbols.put(ResultsFormat.FMT_RDF_N3.getSymbol(), ResultsFormat.FMT_RDF_N3);
            formatSymbols.put(ResultsFormat.FMT_RS_CSV.getSymbol(), ResultsFormat.FMT_RS_CSV);
            formatSymbols.put(ResultsFormat.FMT_TEXT.getSymbol(), ResultsFormat.FMT_TEXT);
            formatSymbols.put(ResultsFormat.FMT_RS_JSON.getSymbol(), ResultsFormat.FMT_RS_JSON);
    }
    
    public static void setCurrentDate() {
		String date = null;
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		date = dateFormatter.format(now);
		LOGGER.info(date);
		VizSparqlRunner.currentDate = date;
	}
    
    public static String getCurrentDate() {
    	return VizSparqlRunner.currentDate;
    }
    
   

	public static HashMap<String, ResultsFormat> getFormatSymbols() {
		return VizSparqlRunner.formatSymbols;
	}

	public static void setFormatSymbols(HashMap<String, ResultsFormat> formatSymbols) {
		VizSparqlRunner.formatSymbols = formatSymbols;
	}

	public static Logger getLogger() {
		return LOGGER;
	} 

	public VizSparqlRunner() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		try {
			if (args.length > 0) {
				init(args[0]);
			} else{ 
				String propFilePath = "setup.properties";
				init(propFilePath);
			}
			VizSparqlRunner app = new VizSparqlRunner();
			app.run();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void init(String propFilePath) throws IOException{
		VizSparqlRunner.setCurrentDate(); 
		Configuration.setDate(getCurrentDate());
		generateDirectories(getCurrentDate(), propFilePath);
	}
	
	

	private void run() {
	   System.out.println("QUERY_FOLDER: "+ Configuration.QUERY_FOLDER);
	   System.out.println("QUERY_RESULTSET_FOLDER: "+ Configuration.QUERY_RESULTSET_FOLDER);
	   String[] extensions = new String[] { "txt", "rq" };
	   List<File> files = (List<File>) FileUtils.listFiles(new File(Configuration.QUERY_FOLDER), extensions, true);
	   try {
		  ResultSet rs;
	      for (File file : files) {
	         System.out.println("file: " + file.getCanonicalPath());
	         ResultsFormat outFormat = getResultFormat(file);
	         // System.out.println("outFormat: "+ outFormat.getSymbol());
	         String queryString = FileUtils.readFileToString(file, "UTF-8");
	         String results = getResultSet(queryString, outFormat);
	         saveFile(results, outFormat, file);
	         System.out.println(); 
	          
	         
	      }
	   } catch (IOException ex) {
		   ex.printStackTrace();
		   System.exit(0);
	   } catch (Exception ex) {
		   ex.printStackTrace();
		   System.exit(0);
	   }
	  
	}
	
	private String getResultSet(String qs, ResultsFormat outFormat) throws Exception {
		QueryExecution qexec = null;
		String results = new String();
		ResultSet resultSet = null;
		OutputStream os = new ByteArrayOutputStream();
		try {
			Query query = QueryFactory.create(qs);
			qexec = QueryExecutionFactory.sparqlService(Configuration.getSPARQL_ENDPOINT(), query);
			resultSet = qexec.execSelect();
			if (outFormat == ResultsFormat.FMT_RS_CSV) {
				ResultSetFormatter.outputAsCSV(os, resultSet);
				results = os.toString();
			} else if (outFormat == ResultsFormat.FMT_RS_XML) {
				ResultSetFormatter.outputAsXML(os, resultSet);
				results = os.toString();
			} else if (outFormat == ResultsFormat.FMT_RS_JSON) {
				ResultSetFormatter.outputAsJSON(os, resultSet);
				results = os.toString();
			} else if (outFormat == ResultsFormat.FMT_TEXT) {
				//ResultSetFormatter.out(os, resultSet);
				ResultSetFormatter.outputAsTSV(os, resultSet);
				results = StringUtils.replace(os.toString(),"\t", "|");
				//results = ResultSetFormatter.asText(resultSet);
				
			} else if (outFormat == ResultsFormat.FMT_RS_TSV) {
				ResultSetFormatter.outputAsTSV(os, resultSet);
				results = os.toString();
			} else {
				ResultSetFormatter.out(os, resultSet);
				results = os.toString();
				//results = ResultSetFormatter.asText(resultSet);
			}
			
		} catch (Exception ex) { 
			throw ex;
		} finally {
			os.close();
			if (qexec != null) {
				qexec.close();
			}
		}
		return results;
	}
	
	private void saveFile(String results, ResultsFormat outFormat, File inFile) {
		String oFileName = new String();
		if (outFormat == ResultsFormat.FMT_RS_CSV) {
			oFileName = StringUtils.replace(inFile.getName(), ".txt", ".csv");
		} else if (outFormat == ResultsFormat.FMT_RS_XML) {
			oFileName = StringUtils.replace(inFile.getName(), ".txt", ".xml");
		} else if (outFormat == ResultsFormat.FMT_RS_JSON) {
			oFileName = inFile.getName();
		} else if (outFormat == ResultsFormat.FMT_RS_TSV) {
			oFileName = inFile.getName();
		} else if (outFormat == ResultsFormat.FMT_TEXT) {
			oFileName = inFile.getName();
		} else {
			oFileName = inFile.getName();
		}
		File oFile = new File(Configuration.QUERY_RESULTSET_FOLDER +"/"+ this.getCurrentDate() +"/"+ oFileName);
		try {
			System.out.println("Writing to file: "+ oFile.getCanonicalPath());
			FileUtils.writeStringToFile(oFile, results);
			//System.out.println(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	private static ResultsFormat getResultFormat(File f) {
		String line = new String();
		try {
			line = FileUtils.readLines(f).get(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		if (StringUtils.startsWith(line, "#csv")) {
			return ResultsFormat.FMT_RS_CSV;
		} else if (StringUtils.startsWith(line, "#xml")) {
			return ResultsFormat.FMT_RS_XML;
		} else if (StringUtils.startsWith(line, "#json")) {
			return ResultsFormat.FMT_RS_JSON;
		} else if (StringUtils.startsWith(line, "#tsv")) {
			return ResultsFormat.FMT_RS_TSV;
		} else if (StringUtils.startsWith(line, "#psv")) {
			return ResultsFormat.FMT_TEXT;
		} else {
			return ResultsFormat.FMT_TEXT;
		}
		
	}
	
	
	
	private static void generateDirectories(String date, String propFilePath) throws IOException {
        
		// SET FILEPATHS
		SetupPropertyValues properties = new SetupPropertyValues();
		Map<String, String> map = properties.getPropValues(propFilePath);
		Configuration.setQUERY_FOLDER(map.get("QUERY_FOLDER"));
		Configuration.setQUERY_RESULTSET_FOLDER(map.get("QUERY_RESULTSET_FOLDER"));
		Configuration.setPOSTPROCESS_RESULTSET_FOLDER(map.get("POSTPROCESS_RESULTSET_FOLDER"));
		Configuration.setSUPPL_FOLDER(map.get("SUPPL_FOLDER"));
		Configuration.setSPARQL_ENDPOINT(map.get("SPARQL_ENDPOINT"));

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
				LOGGER.throwing("KWCloudSparqlRunner", "createFolder", new Throwable("EXCEPTION: Could not create folder..."));
			}
		}
	}

}
