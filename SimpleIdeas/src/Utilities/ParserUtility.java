package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
/**
 * Utility class for parsing a single sentence.
 * @author lingxiwu
 *
 */
public class ParserUtility {

	// http://wordnetweb.princeton.edu/perl/webwn
	
	private enum SubjectTags {
		nsubj, // nominal subject.
		nsubjpass // passive nominal subject.
	}
	private enum BaseVerbTags {
		VB, // Verb, base form
		VBD, // Verb, past tense
		VBG, // Verb, gerund/present participle
		VBN, // Verb, past participle
		VBP, // Verb, non-3rd person singular present  
		VBZ // Verb, 3rd person singular present
	}
	
	private static ParserUtility firstInstance = null;
	private Properties props;
	private static StanfordCoreNLP pipeline;
	private static NamedEntityInfo neInfo;
	
//	private ParserUtility() {
//		
//	}
//	
//	public static ParserUtility getInstance() {
//		
//		
//		if(firstInstance == null) {
//			
//		}
//	}
	
	/**
	 * Empty args Constructor.
	 * @author lw2ef
	 *
	 */
	public ParserUtility() {
		// Set default props which is almost annotators.
		props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		pipeline = new StanfordCoreNLP(props);
		neInfo = new NamedEntityInfo();
	}
	
	/**
	 * One constructor that can instantiate a pipeline object.
	 * @author lw2ef
	 *
	 */
	public ParserUtility(String propsString) {
		// Make a pipeline object.
		props = new Properties();
		props.put("annotators", propsString);
		pipeline = new StanfordCoreNLP(props);		
		neInfo = new NamedEntityInfo();
	}
	
	
	
	/**
	 * TO-DO:
	 * Take an annotated String such as occur/VB (root) to extract the base verb "occur."
	 * @param args
	 */
	public static String[] baseVerb(String dependencyAnnotatedLine) {
		
		String[] baseVerb = new String[2];
		
		for(BaseVerbTags bvt : BaseVerbTags.values()) {
			if(dependencyAnnotatedLine.contains(bvt.toString())){
				baseVerb[0] = dependencyAnnotatedLine.trim();
				break;
			}
		}
		if(baseVerb[0].length() != 0) {
			baseVerb[1] = lemmatize(baseVerb[0]);
		}
		
		return baseVerb;
	}
	
	/**
	 * 
	 * @param dependencyLine
	 * @return A string array with two entries. First = Original , Second = Lemmatized version
	 */
	public static String[] extractNounSubject(String sentence) {
		
		String[] nounSubject = new String[2];
		Annotation doc = new Annotation(sentence);
		pipeline.annotate(doc);
		String raw = doc.get(SentencesAnnotation.class).get(0).get(BasicDependenciesAnnotation.class).toString(); // A dependency parse tree.
		
		outerloop:
		for(String dependencySentence : raw.split("->")) {		
			for(SubjectTags st : SubjectTags.values()) {
				if(dependencySentence.contains(st.toString())){
					nounSubject[0] = dependencySentence.trim().split("/")[0];
					nounSubject[1] = lemmatize(nounSubject[0]);
					break outerloop;
				}
			}
		}		
		
		return nounSubject;
	}

	/**
	 * Build a hash map that stores Named Entities: LOCATION -> "Baker Street" 
	 * Be careful with 0 and O... For some reason Stanford Core NLP uses O not 0.
	 * @param sentence
	 */
	public static HashMap<String, String> extractNamedEntities(String sentence) {

		Annotation doc = new Annotation(sentence);
		pipeline.annotate(doc);
		ArrayList<String> tempNEClassHolder = new ArrayList<String>();
		ArrayList<String> tempNE = new ArrayList<String>();
		HashMap<String, String> namedEntities = new HashMap<String, String>();
		
		String recognizedNE = "O", partialNEValue = "";		
		for(CoreLabel token : doc.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class)) {
			String ne = token.get(NamedEntityTagAnnotation.class);
			if(!ne.equals("O")) {								
				if(!recognizedNE.equals(ne)) {
					recognizedNE = ne;		
					tempNEClassHolder.add(recognizedNE);
					if(!partialNEValue.isEmpty()) {						
						tempNE.add(partialNEValue.trim());
					}
					partialNEValue = ""; 					
				}
				partialNEValue += token.toString().split("-")[0];
				partialNEValue += " ";
			}
		}
		if(!partialNEValue.isEmpty()) {
			tempNE.add(partialNEValue.trim());
		}
		for(int i=0;i<tempNEClassHolder.size();i++) {
			namedEntities.put(tempNEClassHolder.get(i), tempNE.get(i));
		}
		System.out.println("Performing Named Entity Extraction Process ... ");

		System.out.println(tempNEClassHolder.toString());
		System.out.println(tempNE.toString());
		return namedEntities;
		
	}
	
	public static SensorActuator matchSensorActuator(String sentence) {
		
		
		
		return null;
	}
	
	/**
	 * Take a word and return its lemma. occurs -> occur, occurred -> occur.
	 * @param propsString
	 */
	public static String lemmatize(String word) {
		String lemma = word;
		Annotation doc = new Annotation(word);
		pipeline.annotate(doc);
		lemma = doc.get(TokensAnnotation.class).get(0).get(LemmaAnnotation.class);
		
		return lemma;
		
	}
	
	/*
	 * Getters and Setters.
	 */
	public void setProps(String propsString) {
		props.put("annotators", propsString);
	}
	
	public Properties getProps() {
		return props;
	}
	
	public StanfordCoreNLP getPipeline() {
		return pipeline;
	}
	
	/*
	 * other utility stuff.
	 */
	
	/**
	 * Holds Named Entity information. NEClass -> "LOCATION","PEOPLE". NE -> "Emmet Street", "Mike Johnson"
	 * @author lingxiwu
	 *
	 */
	public class NamedEntityInfo{
		
		// Use set to eliminate duplicate.
		Set<String> NEClass = new HashSet<>();
		Set<String> NE = new HashSet<>();
		
		HashMap<String, String> NamedEntities = new HashMap<String, String>();
		
		/**
		 * 
		 * @param NEName: like LOCATION, DURATION, PERSON, and etc.
		 */
		public void addNEClass(String NEClass) {
			this.NEClass.add(NEClass);
		}
		
		public void addNE(String NE) {
			this.NE.add(NE);
		}
		
		public Set<String> getNEClass(){
			return NEClass;
		}
		
		public Set<String> getNE(){
			return NE;
		}
		
		public void cleanupNEInfo() {
			NEClass = new HashSet<>();
			NE = new HashSet<>();
		}
		
	}
	
}
