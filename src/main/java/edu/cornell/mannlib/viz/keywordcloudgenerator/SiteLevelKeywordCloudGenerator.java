package edu.cornell.mannlib.viz.keywordcloudgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;

import edu.cornell.mannlib.viz.VizPostProcessor;
import edu.cornell.mannlib.viz.config.Configuration;
import edu.cornell.mannlib.viz.keywordminer.article.Concept;



public class SiteLevelKeywordCloudGenerator {

	private static final Logger LOGGER = Logger.getLogger(SiteLevelKeywordCloudGenerator.class.getName());

	private static String ARTICLE_2_PERSON_MAP = null;
	private static String ALL_KW_FILENAME = null;
	private static String ALL_CONCEPTS_FILENAME = null;
	private static String ARTICLE_2_KW_FILENAME = null;
	private static String ARTICLE_2_CONCEPTS_FILENAME = null;
	private static String ARTICLE_2_INFERRED_KEYWORDS_MAP = null;
	private static String SITE_KEYWORD_CLOUD = null;

	private Set<String> allkeywords = null;
	private Set<Concept> allConcept = null;
	private Set<String> allConceptWords = null;
	private Map<String, Set<String>> articleConceptMap = null;
	private Map<String, Set<String>> articleKWMap = null;
	private Map<String, Set<String>> articleINFKWMap = null;
	private Map<String, Set<Person>> article2person = null;
	private Set<String> NFPersonArticles = null;
	private String[] sw =  {
			"Animals",
			"Humans",
			"Male",
			"Female",
			"Mice",
			"Rats",
			"Cattle",
			"Swine",
			"Sheep"
			};
	private Set<String> stopwords = new HashSet<String>();
	
	public static void main(String[] args) {
		try {
			VizPostProcessor.init("setup.properties");
			SiteLevelKeywordCloudGenerator obj = new SiteLevelKeywordCloudGenerator();
			obj.setLocalDirectories();
			obj.runProcess();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}	
	}

	private void setLocalDirectories() {
		ARTICLE_2_PERSON_MAP = Configuration.QUERY_RESULTSET_FOLDER +"/"+ Configuration.date 
				+ "/" + Configuration.ARTICLE_2_PERSON_MAP_FILENAME;

		ALL_KW_FILENAME = Configuration.QUERY_RESULTSET_FOLDER +"/"+ Configuration.date +"/"+
				Configuration.ALL_KEYWORDS_FILENAME;
		ALL_CONCEPTS_FILENAME = Configuration.QUERY_RESULTSET_FOLDER +"/"+ Configuration.date +"/"+
				Configuration.ALL_CONCEPTS_FILENAME;

		ARTICLE_2_KW_FILENAME = Configuration.QUERY_RESULTSET_FOLDER +"/"+ Configuration.date +"/"+
				Configuration.ARTICLE_2_KEYWORDSET_MAP_FILENAME;
		ARTICLE_2_CONCEPTS_FILENAME = Configuration.QUERY_RESULTSET_FOLDER +"/"+ Configuration.date +"/"+
				Configuration.ARTICLE_2_CONCEPT_MAP_FILENAME;
		ARTICLE_2_INFERRED_KEYWORDS_MAP = Configuration.QUERY_RESULTSET_FOLDER +"/"+ Configuration.date 
				+ "/" + Configuration.ARTICLE_2_INFERREDKEYWORD_FILENAME;

		SITE_KEYWORD_CLOUD = Configuration.POSTPROCESS_RESULTSET_FOLDER +"/"+ Configuration.date +"/"+
				Configuration.HOMEPAGE_KEYWORD_CLOUD_FOLDER + "/" + Configuration.HOMEPAGE_KEYWORD_CLOUD_FILENAME;
	}

	public void runProcess() throws IOException, ParserConfigurationException, SAXException {
		setLocalDirectories();
		
		for(String s: sw){
			stopwords.add(s.toLowerCase());
		}
	
		NFPersonArticles = new HashSet<String>();	
		article2person = readArticle2PersonMapFile(ARTICLE_2_PERSON_MAP);
		LOGGER.info("article2person map size: "+ article2person.size());
		/*Iterator iter = article2person.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			LOGGER.info("article2person key: ["+ key +"] ");
		}*/
		allkeywords = getLines(new File(ALL_KW_FILENAME));
		LOGGER.info("allkeywords map size: "+ allkeywords.size());
		 
		allConcept = getConceptLines(new File(ALL_CONCEPTS_FILENAME));
		articleKWMap   = getArticleKeywordConceptMap(new File(ARTICLE_2_KW_FILENAME));
		LOGGER.info("articleKWMap size: "+ articleKWMap.size());
		/*Iterator iter2 = articleKWMap.keySet().iterator();
		if (iter2.hasNext()) {
			String firstKey = (String) iter2.next();
			Set<String> firstSet = articleKWMap.get(firstKey);
			LOGGER.info("key: ["+ firstKey +"] ");
			LOGGER.info("set: "+ firstSet.toString());
		}  */
		articleConceptMap = getArticleKeywordConceptMap(new File(ARTICLE_2_CONCEPTS_FILENAME));
		//articleINFKWMap = getArticleKeywordConceptMap(new File(ARTICLE_2_INFERRED_KEYWORDS_MAP));
        
		
		//iterate over keywords and find articles and then persons for those articles.
		 
		Map<String, Keyword> keywordMap = getKeywordToPersonMap(allkeywords, articleKWMap, article2person);
		LOGGER.info("keywordMap size: "+ keywordMap.size());
		//iterate over concept terms and find articles and then persons for those articles.
		Map<String, Keyword> conceptMap = getConceptTermToPersonMap(allConcept, articleConceptMap, article2person);

		Map<String, Keyword> groupedMap = new HashMap<String, Keyword>();
		groupedMap.putAll(keywordMap);
		groupedMap.putAll(conceptMap);
		LOGGER.info("groupedMap size: "+  groupedMap.size());
		LOGGER.info("NFPersonArticles: "+NFPersonArticles.size()); // not used right now.
		//printKeywordMap(groupedMap);
		computeWordCloudData(groupedMap); 


	}

	private void printKeywordMap(Map<String, Keyword> keywordMap) {
		Set<String> keys = keywordMap.keySet();
		for(String key: keys){
			Keyword kw = keywordMap.get(key);
			LOGGER.info("\""+key+"\",\""+kw.getCountByPerson()+"\"");
		}
	}

	private void computeWordCloudData(Map<String, Keyword> kwMap) throws JsonGenerationException, JsonMappingException, IOException {
		List<Keyword> kwListByPersonCount = new ArrayList<Keyword>(kwMap.values());
		//LOGGER.info("keyword list size: "+ kwListByPersonCount.size());
		Collections.sort(kwListByPersonCount, new Keyword.SortByPersonCount());
		kwListByPersonCount = removeStopWords(stopwords, kwListByPersonCount);
		int numKW = 100;
		if (kwListByPersonCount.size() < 100) {
			numKW = kwListByPersonCount.size();
		}
		List<Keyword> subKWListByPersonCount = kwListByPersonCount.subList(0, numKW);
		mapDataToJSON(subKWListByPersonCount, SITE_KEYWORD_CLOUD);
	}


	private List<Keyword> removeStopWords(Set<String> stopwords2, List<Keyword> kwListByPersonCount) {
		List<Keyword> newKWList = new ArrayList<Keyword>();
		for(Keyword kw:kwListByPersonCount){
			String k = kw.getKeyword();
			if(!stopwords2.contains(k.toLowerCase())){
				newKWList.add(kw);
			}
		}
		return newKWList;
	}

	private Map<String, Keyword> getConceptTermToPersonMap(Set<Concept> allConcept2,
			Map<String, Set<String>> articleConceptMap2, Map<String, Set<Person>> article2person2) {

		Map<String, Keyword> conceptMap = new HashMap<String, Keyword>();
		LOGGER.info("article2person2 size: " + article2person2.size());
		for (Concept concept : allConcept2) {
			String conceptLabel = concept.getConceptLabel();
			// if(conceptLabel.equalsIgnoreCase("Animals")){
			// System.out.println("got the concept");
			// }
			Set<String> listOfArticles = getArticleListForAConceptTerm(conceptLabel, articleConceptMap2);
			//LOGGER.info("listOfArticles size: " + listOfArticles.size());
			if (listOfArticles.size() > 0) {
				Set<Person> listOfPersons = getPersonsListForAKeywordConcept(listOfArticles, article2person2);
				//LOGGER.info("listOfPersons size: "+ listOfPersons.size());
				if (listOfPersons.size() > 0) {
					Keyword kw = new Keyword();
					for (Person per : listOfPersons) {
						kw.addPerson(per);
					}
					kw.addTypes("CONCEPT");
					kw.setCountOfArticle(listOfArticles.size());
					kw.setCountOfPerson(kw.getPersons().size());
					kw.setKeyword(conceptLabel);
					conceptMap.put(conceptLabel, kw);
				}
			}
		}
		return conceptMap;
	}

	private Map<String, Keyword> getKeywordToPersonMap(Set<String> allkeywords2, Map<String, Set<String>> articleKWMap2,
			Map<String, Set<Person>> article2person2) {

		Map<String, Keyword> keywordMap = new HashMap<String, Keyword>();
		

		for (String keyword : allkeywords2) {
			//if (keyword.equalsIgnoreCase("Animals")) {
				// System.out.println("got the keyword");
			//}
			Set<String> listOfArticles = getArticleListForAKeyword(keyword, articleKWMap2);
			// LOGGER.info("listOfArticles size: "+ listOfArticles.size());
			if (listOfArticles.size() > 0) {
				Set<Person> listOfPersons = getPersonsListForAKeywordConcept(listOfArticles, article2person2);
				//LOGGER.info("\""+keyword+"\",\""+ listOfPersons.size()+"\"");
				if (listOfPersons.size() > 0) {
					Keyword kw = new Keyword();
					for (Person per : listOfPersons) {
						kw.addPerson(per);
					}
					kw.addTypes("KEYWORD");
					kw.setCountOfArticle(listOfArticles.size());
					kw.setCountOfPerson(kw.getPersons().size());
					kw.setKeyword(keyword);
					keywordMap.put(keyword, kw);
				}
			}
		}
		return keywordMap;
	}

	private Set<Person> getPersonsListForAKeywordConcept(Set<String> listOfArticles,
			Map<String, Set<Person>> article2person2) {
		Set<Person> persons = new HashSet<Person>();
		for (String article : listOfArticles) {
			//LOGGER.info("article: "+ article);
			String fixedArticle = StringUtils.replace(article,"<", "");
			fixedArticle = StringUtils.replace(fixedArticle,">", "");
			Set<Person> personSet = article2person2.get(fixedArticle);
			try {
				addNewPersonsWithArticleCount(persons, personSet);
			} catch (NullPointerException exp) {
				NFPersonArticles.add(article);
			}
		}
		//LOGGER.info("persons size: "+ persons.size());
		return persons;
	}

	private void addNewPersonsWithArticleCount(Set<Person> allPersonSet, Set<Person> newPersonSet) {
		// if person already exist in allPersonSet, increment count by 1.
		// if person do not have in allPersonSet, add new Person and made count 1.
		for(Person newPerson: newPersonSet){
			if(allPersonSet.contains(newPerson)){
				Person p = getPerson(allPersonSet, newPerson);
				p.setArticleCount(p.getArticleCount()+1);
			}else{
				newPerson.setArticleCount(1);	
				allPersonSet.add(newPerson);
			}
		}
	}

	private Person getPerson(Set<Person> allPersonSet, Person newPerson) {
		Person p = null;
		for(Iterator<Person> i = allPersonSet.iterator(); i.hasNext();){
			Person person = i.next();
			if(person.equals(newPerson)){
				p = person;
				break;
			}
		}
		return p;
	}

	private Set<String> getArticleListForAConceptTerm(String conceptLabel, Map<String, Set<String>> articleConceptMap2) {
		Set<String> articles = new HashSet<String>();
		Set<String> keys = articleConceptMap2.keySet();
		for(String key: keys){
			Set<String> keywords = articleConceptMap2.get(key);
			if(keywords.contains(conceptLabel.toUpperCase())){
				articles.add(key);
			}
		}
		return articles;
	}

	private Set<String> getArticleListForAKeyword(String keyword, Map<String, Set<String>> articleKWMap2) {
		Set<String> articles = new HashSet<String>();
		Set<String> keys = articleKWMap2.keySet();
		for(String key: keys){
			Set<String> keywords = articleKWMap2.get(key);
			if(keywords.contains(keyword.toUpperCase())){
				articles.add(key);
			}
		}
		return articles;
	}

	private Map<String, Set<Person>> readArticle2PersonMapFile(String filePath) throws IOException {
		Map<String, Set<Person>> map = new HashMap<String, Set<Person>>();
		BufferedReader br = null;
		String line = "";
		long lineCount = 0;
		br = new BufferedReader(new FileReader(new File(filePath)));
		while ((line = br.readLine()) != null) {
			lineCount++;
			if(line.trim().length() == 0) continue;
			@SuppressWarnings("resource")
			CSVReader reader = new CSVReader(new StringReader(line),',','\"');
			String[] tokens;
			while ((tokens = reader.readNext()) != null) {
				try {
					if (lineCount == 1) continue; // header line
					
					String articleURI = tokens[0];
					Person person = new Person();
					String personURI = tokens[1];
					String personName = getPersonName(tokens[2], tokens[3], tokens[4]);
					person.setPersonURI(personURI);
					person.setPersonName(personName);
					if(map.get(articleURI)!= null){
						Set<Person> p = map.get(articleURI);
						p.add(person);
						map.put(articleURI, p);
					}else{
						Set<Person> p = new HashSet<Person>();
						p.add(person);
						map.put(articleURI, p);
					}	
				}catch (ArrayIndexOutOfBoundsException exp) {
					for (String s : tokens) {
						LOGGER.warning("ArrayIndexOutOfBoundsException: "+ lineCount+" :"+ s);
					}
					LOGGER.warning("\n");
					continue;
				}
			}
		}
		br.close();
		LOGGER.info("UNIV-LEVEL KEYWORD CLOUD: Article to Person Set Map size:"+ map.size());
		return map;
	}


	private Map<String, Set<String>> getArticleKeywordConceptMap(File file) throws IOException {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		LOGGER.info("Reading : "+ file.getName());
		BufferedReader br = null;
		String line = "";
		long lineCount = 0;
		long kwCount = 0;
		br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) {
			lineCount++;
			if(line.trim().length() == 0) continue;
			@SuppressWarnings("resource")
			CSVReader reader = new CSVReader(new StringReader(line),'|', '\"');
			 
			String[] tokens;
			
			while ((tokens = reader.readNext()) != null) {
				//LOGGER.info("tokens size: "+ tokens.length);
				try {
					//if (tokens.length > 0) {
					   Set<String> kwords = new HashSet<String>();
					   
					   String kw[] = tokens[1].split(";;");
					 
					   for (String k : kw) {
						 kwords.add(k.trim().toUpperCase());
						 kwCount = kwCount + 1;
					   }
					   map.put(tokens[0], kwords);
					   //LOGGER.info("Num kwords: "+ kwords.size());
				   //}
				} catch (ArrayIndexOutOfBoundsException exp) {
					for (String s : tokens) {
						LOGGER.warning("ArrayIndexOutOfBoundsException: " + lineCount + " :" + s);
					}
					LOGGER.warning("\n");
					continue;
				}
			}
		}
		br.close();
		LOGGER.info("article to keywords/concept line count: "+lineCount);
		LOGGER.info("kwCount: "+kwCount);
		return map;
	}


	private Set<Concept> getConceptLines(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		allConceptWords = new HashSet<String>();
		Set<Concept> rows = new HashSet<Concept>();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
		NodeList entryList = doc.getElementsByTagName("result");
		for (int index = 0; index < entryList.getLength(); index++) {
			Concept obj = new Concept();
			Node node = entryList.item(index);
			// System.out.println(node.getNodeName());
			Element eElement = (Element) node;
			NodeList bindingNodes = eElement.getElementsByTagName("binding");
			for (int i = 0; i < bindingNodes.getLength(); i++) {
				Node n = bindingNodes.item(i);
				Element bindElement = (Element) n;
				String att = bindElement.getAttribute("name");
				switch (att) {
				case "subjectUri":
					obj.setConcept(bindElement.getElementsByTagName("uri").item(0).getTextContent());
					break;
				case "label":
					obj.setConceptLabel(bindElement.getElementsByTagName("literal").item(0).getTextContent());
					break;
				}
			}
			allConceptWords.add(obj.getConceptLabel());
			rows.add(obj);
		} // end of reading entries.
		LOGGER.info("Total number of concept terms:" + rows.size());
		return rows;
	}

	private Set<String> getLines (File file) throws IOException{
		Set<String> rows = new HashSet<String>();
		CSVReader reader;
		reader = new CSVReader(new FileReader(file),',','\"');
		String [] nextLine;	
		reader.readNext();  // header
		while ((nextLine = reader.readNext()) != null) {
			rows.add(nextLine[0]);
		}
		reader.close();
		LOGGER.info("Total number of freetext keywords:"+ rows.size());
		return rows;
	}
	private String getPersonName(String firstName, String middleName, String familyName) {
		return firstName+" "
				+ (middleName.trim().length()>0 ? middleName:"")+" "
				+familyName;
	}

	private void mapDataToJSON(Collection<Keyword> collection, String filePath) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
			mapper.writeValue(new File(filePath), collection);
			jsonInString = mapper.writeValueAsString(collection);
			//System.out.println(jsonInString);
	}
	
	private void printMapSet(Map<String, Set<Object>> mapOfSets) {
		Iterator iter = mapOfSets.keySet().iterator();
		if (iter.hasNext()) {
			String firstKey = (String) iter.next();
			Set<Object> firstSet = mapOfSets.get(firstKey);
			LOGGER.info("key: ["+ firstKey +"] ");
			LOGGER.info("set: "+ firstSet.toString());
		}
	}
	
	private void printSet(Set<String> mySet) {
	   for (String s: mySet) {
	      LOGGER.info("set: "+ s);
	   }
	}
}
