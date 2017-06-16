package edu.cornell.mannlib.viz;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.cornell.mannlib.viz.util.ClassPathPropertyLoader;

public class SetupPropertyValues { 
	Map<String, String> map = new HashMap<String, String>();
	
	public Map<String, String> getPropValues(String propFilePath) throws IOException {
		ClassPathPropertyLoader loader = new ClassPathPropertyLoader();
		Properties prop = loader.load(propFilePath);
		if (prop == null) {
			throw new FileNotFoundException("property file '" + propFilePath + "' not found in the classpath");	
		}
 
		String queryFolder = prop.getProperty("QUERY_FOLDER");
		map.put("QUERY_FOLDER", queryFolder);
		String queryResultSet = prop.getProperty("QUERY_RESULTSET_FOLDER");
		map.put("QUERY_RESULTSET_FOLDER", queryResultSet);
		String PostprocessResultSet = prop.getProperty("POSTPROCESS_RESULTSET_FOLDER");
		map.put("POSTPROCESS_RESULTSET_FOLDER", PostprocessResultSet);
		String SupplFolder = prop.getProperty("SUPPL_FOLDER");
		map.put("SUPPL_FOLDER", SupplFolder); 
		String SparqlEndpoint = prop.getProperty("SPARQL_ENDPOINT");
		map.put("SPARQL_ENDPOINT", SparqlEndpoint);
		String VivoNamespace = prop.getProperty("VIVO_NAMESPACE");
		map.put("VIVO_NAMESPACE", VivoNamespace);
		return map;
	}
}
