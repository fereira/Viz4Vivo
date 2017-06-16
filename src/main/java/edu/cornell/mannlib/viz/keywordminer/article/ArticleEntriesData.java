package edu.cornell.mannlib.viz.keywordminer.article;

import java.util.HashSet;
import java.util.Set;

public class ArticleEntriesData extends ArticleEntries{

	private Set<String> titlswords;
	private Set<String> conceptTerms;
	private Set<String> keywords;
	private int minedKeywordCount;
	
	public Set<String> getTitlewords() {
		return titlswords;
	}

	public void setTitlewords(Set<String> titlswords) {
		this.titlswords = titlswords;
	}
	public void addTitlewords(String titlswords) {
		if(this.titlswords == null){
			this.titlswords = new HashSet<String>();
		}
		this.titlswords.add(titlswords);
	}

	public Set<String> getConceptTerms() {
		return conceptTerms;
	}

	public void setConceptTerms(Set<String> conceptTerms) {
		this.conceptTerms = conceptTerms;
	}
	public void addConceptTerms(String conceptTerm) {
		if(this.conceptTerms == null){
			this.conceptTerms = new HashSet<String>();
		}
		this.conceptTerms.add(conceptTerm);
	}
	
	public Set<String> getKeywords() {
		return keywords;
	}
	
	public void addKeywords(String keyword) {
		if(this.keywords == null){
			this.keywords = new HashSet<String>();
		}
		this.keywords.add(keyword);
	}

	public int getMinedKeywordCount() {
		return minedKeywordCount;
	}

	public void setMinedKeywordCount(int minedKeywordCount) {
		this.minedKeywordCount = minedKeywordCount;
	}

	@Override
	public String toString() {
		return "\"" + getArticleURI() + "\",\"" + getArticleTitle().replaceAll("\"", "") + "\",\"" + minedKeywordCount + "\",\"" + conceptTerms
				+ "\",\"" + keywords + "\"";
	}
	
	
	
}
