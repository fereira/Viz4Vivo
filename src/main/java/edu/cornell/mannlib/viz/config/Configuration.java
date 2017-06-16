package edu.cornell.mannlib.viz.config;

public class Configuration {
	
	// VIVO NAMESPACE
    public static String VIVO_NAMESPACE = null;
    
    public static String date = null;
	
    public static String QUERY_FOLDER = null;
	public static String QUERY_RESULTSET_FOLDER = null;		  
	public static String POSTPROCESS_RESULTSET_FOLDER = null; 
	public static String SUPPL_FOLDER = 	null;
	public static String SPARQL_ENDPOINT = null;
	
	// FOLDERS
	public static String GRANTS_FOLDER = "grant";
	public static String COLLABORATION_FOLDER = "collab";
	public static String COLLAB_INTERNAL_FOLDER = "internal";
	public static String COLLAB_EXTERNAL_FOLDER = "external";
	public static String INFERRED_KEYWORDS_FOLDER = "inferredkeywords";
	public static String HOMEPAGE_KEYWORD_CLOUD_FOLDER = "kwclouds";
	public static String SUBJECTAREA_FOLDER = "subjectarea";
	
	// GENERAL
	public static String ALL_KEYWORDS_FILENAME = "AllFreeTextKeywords.txt";
	public static String ALL_CONCEPTS_FILENAME = "AllConcepts.xml";
	public static String ARTICLE_2_KEYWORDSET_MAP_FILENAME = "Article2KeywordSetMap.txt";
	public static String ARTICLE_2_CONCEPT_MAP_FILENAME = "Article2ConceptMap.txt";
	
	
	// ARTICLE KEYWORD MINER
	public static String ARTICLE_2_TITLE_ABSTRACT_MAP_FILENAME = "Article2TitleAbstractMap.xml";
	public static String ARTICLEID_MASTER_KEYWORDMINER_FILENAME = 	"MASTER_KeywordMiner_ARTICLEID.csv";
	public static String INF_KEYWORDS_CSV = "ArticleMapWithMinedKeywordsAndConcept.csv";
	public static String INF_KEYWORDS_NT =  "ArticleMapWithMinedKeywordsAndConcept.nt";
	
	
	// HOMEPAGE KEYWORD CLOUD GENERATOR
	public static String ARTICLE_2_PERSON_MAP_FILENAME = "Article2PersonMap.csv";
	public static String ARTICLE_2_INFERREDKEYWORD_FILENAME = "Article2InferredKeywordSetMap.txt";
	public static String HOMEPAGE_KEYWORD_CLOUD_FILENAME = "HomepageKWCloud.json";
	
	
	// GRANTS DATA
	public static String OSP_AWARDS_FILENAME = "Awards.xml";
	public static String OSP_INV_FILENAME = "Investigators.xml";
	public static String PERSON_2_DEPT_UNIT_MAP_FILENAME = "Person2DepartmentUnitMap.txt";
	public static String OSP_ADMNDEPT_FILENAME ="GrantAdministringDepartmentMapper.csv";
	public static String ALL_GRANTS_FILENAME = "AllGrants.csv";
	public static String OSP_GRANT_TXT = "AwdInv-all.txt";
	public static String ALL_ORGANIZATION_MAP_FILENAME = "AllOrganizationsMap.psv";
	public static String OSP_GRANT_NT = "AwdInv-all.nt";
	
	
	// JOURNAL SUBJECT AREA
	public static String JOURNAL_2_ISSN_EISSN_SUBJECTAREA_MAP_FILENAME = "Journal2IssnEissnSubjectAreaMap.txt";
	public static String JOURNAL_MASTER_FILENAME = 		"MASTER_JOURNALID.csv";
	public static String ALL_SUBJECTAREAS_FILENAME = "AllSubjectAreas.txt";
	public static String WOS_JOURNAL_CLSFCN_FILENAME ="JournalClassification-WOS.csv";
	public static String FOR_JOURNAL_CLSFCN_FILENAME ="JournalClassification-FOR.csv";
	public static String JOURNAL_2_SUBJECTAREA_CSV = "Journal2SubjectArea.csv";
	public static String JOURNAL_2_SUBJECTAREA_NT = "Journal2SubjectArea.nt";
	
	
	// INTERNAL COLLABORATIONS
	public static String ORG_ORGCODE_MAP_FILE = "org-orgcode-map.csv";
	
	public static String EXT_COLLABORATIONS_FILE_STATE_JSON = "ExternalCollaborations-State.json";
	public static String EXT_COLLABORATIONS_FILE_COUNTRY_JSON = "ExternalCollaborations-Country.json";
	public static String NETID_2_UNITS_MAP_FILE = "NetId2UnitMap.csv";
	public static String ARTICLE_2_WOS_PUBMED_ID_MAP_FILE_CSV = "Article2WosPubmedIdMap.csv";
	public static String COUNTRIES_FILE = "countries.tsv";
	public static String USA_STATE_FILE ="usa_state.txt";
	public static String GRID_FILENAME = "grid.csv";
	public static String WOS_DATA_FOLDER = "WOS_FILES";
	public static String WOS_DATA_FILENAME = "WOSDataFile.csv";
	public static String AFF_GRID_MAP = "AffiliationGridMapper.csv";
	public static String ARTICLE_2_SUBJECTAREA_FILENAME = "ArticleURI2SubjectAreas.csv";
	public static String PERSON_2_ARTICLE_MAP_FILENAME = "Person2ArticleArticleIdMap.csv";
	
	// GRANT KEYWORD MINER
	public static String GRANT_2_TITLE_ABSTRACT_MAP_FILENAME = "Grant2TitleAbstractMap.xml";
	public static String GRANTID_MASTER_KEYWORDMINER_FILENAME = "MASTER_KeywordMiner_GRANTID.csv";
	public static String GRANTS_INF_KEYWORDS_CSV = "GrantMapWithMinedKeywordsAndConcept.csv";
	public static String GRANTS_INF_KEYWORDS_NT =  "GrantMapWithMinedKeywordsAndConcept.nt";
	
	

	public static void setDate(String date) {
		Configuration.date = date;
	}
	
	public static void setPOSTPROCESS_RESULTSET_FOLDER(String pOSTPROCESS_RESULTSET_FOLDER) {
		POSTPROCESS_RESULTSET_FOLDER = pOSTPROCESS_RESULTSET_FOLDER;
	}
	
	public static void setQUERY_FOLDER(String qUERY_FOLDER) {
		QUERY_FOLDER = qUERY_FOLDER;
	}
	
	public static void setQUERY_RESULTSET_FOLDER(String qUERY_RESULTSET_FOLDER) {
		QUERY_RESULTSET_FOLDER = qUERY_RESULTSET_FOLDER;
	}
	
	public static void setSUPPL_FOLDER(String sUPPL_FOLDER) {
		SUPPL_FOLDER = sUPPL_FOLDER;
	}

	public static String getSPARQL_ENDPOINT() {
		return SPARQL_ENDPOINT;
	}

	public static String getVIVO_NAMESPACE() {
		return VIVO_NAMESPACE;
	}

	public static void setVIVO_NAMESPACE(String vIVO_NAMESPACE) {
		VIVO_NAMESPACE = vIVO_NAMESPACE;
	}

	public static void setSPARQL_ENDPOINT(String sPARQL_ENDPOINT) {
		SPARQL_ENDPOINT = sPARQL_ENDPOINT;
	}

	public static String getQUERY_FOLDER() {
		return QUERY_FOLDER;
	}

	public static String getQUERY_RESULTSET_FOLDER() {
		return QUERY_RESULTSET_FOLDER;
	}

	public static String getSUPPL_FOLDER() {
		return SUPPL_FOLDER;
	}

	public static String getGRANTS_FOLDER() {
		return GRANTS_FOLDER;
	}

	public static void setGRANTS_FOLDER(String gRANTS_FOLDER) {
		GRANTS_FOLDER = gRANTS_FOLDER;
	}

	public static String getCOLLABORATION_FOLDER() {
		return COLLABORATION_FOLDER;
	}

	public static void setCOLLABORATION_FOLDER(String cOLLABORATION_FOLDER) {
		COLLABORATION_FOLDER = cOLLABORATION_FOLDER;
	}

	public static String getCOLLAB_INTERNAL_FOLDER() {
		return COLLAB_INTERNAL_FOLDER;
	}

	public static void setCOLLAB_INTERNAL_FOLDER(String cOLLAB_INTERNAL_FOLDER) {
		COLLAB_INTERNAL_FOLDER = cOLLAB_INTERNAL_FOLDER;
	}

	public static String getCOLLAB_EXTERNAL_FOLDER() {
		return COLLAB_EXTERNAL_FOLDER;
	}

	public static void setCOLLAB_EXTERNAL_FOLDER(String cOLLAB_EXTERNAL_FOLDER) {
		COLLAB_EXTERNAL_FOLDER = cOLLAB_EXTERNAL_FOLDER;
	}

	public static String getINFERRED_KEYWORDS_FOLDER() {
		return INFERRED_KEYWORDS_FOLDER;
	}

	public static void setINFERRED_KEYWORDS_FOLDER(String iNFERRED_KEYWORDS_FOLDER) {
		INFERRED_KEYWORDS_FOLDER = iNFERRED_KEYWORDS_FOLDER;
	}

	public static String getHOMEPAGE_KEYWORD_CLOUD_FOLDER() {
		return HOMEPAGE_KEYWORD_CLOUD_FOLDER;
	}

	public static void setHOMEPAGE_KEYWORD_CLOUD_FOLDER(String hOMEPAGE_KEYWORD_CLOUD_FOLDER) {
		HOMEPAGE_KEYWORD_CLOUD_FOLDER = hOMEPAGE_KEYWORD_CLOUD_FOLDER;
	}

	public static String getSUBJECTAREA_FOLDER() {
		return SUBJECTAREA_FOLDER;
	}

	public static void setSUBJECTAREA_FOLDER(String sUBJECTAREA_FOLDER) {
		SUBJECTAREA_FOLDER = sUBJECTAREA_FOLDER;
	}

	public static String getWOS_DATA_FOLDER() {
		return WOS_DATA_FOLDER;
	}

	public static void setWOS_DATA_FOLDER(String wOS_DATA_FOLDER) {
		WOS_DATA_FOLDER = wOS_DATA_FOLDER;
	}
	
	
}
