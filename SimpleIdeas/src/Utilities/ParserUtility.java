package Utilities;

import java.util.ArrayList;
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
	
	private Properties props;
	private static StanfordCoreNLP pipeline;
	private static NamedEntityInfo neInfo;
	
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

	
	public static void extractNamedEntity(String sentence) {

		Annotation doc = new Annotation(sentence);
		pipeline.annotate(doc);
		
		for(CoreLabel token : doc.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class)) {
			String ne = token.get(NamedEntityTagAnnotation.class);
			System.out.println(ne);
//			String neParts = "";
			if(!ne.equals("O")) {
				neInfo.getNEClass().add(ne);
//				neParts += token.toString();
//				System.out.println(token.toString());
			}
//			neInfo.getNE().add(neParts);
		}
//		System.out.println(neInfo.getNEClass().toString());
		
	}
	
	public static void entity(String sentence) {
		Annotation doc = new Annotation(sentence);
		pipeline.annotate(doc);
		for(CoreLabel token : doc.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class)) {
			String ne = token.get(NamedEntityTagAnnotation.class);
			String neParts = "";
			if(!ne.equals("O")) {
				neInfo.getNEClass().add(ne);
//				neParts += token.toString();
				System.out.println(token.toString());
			}
			neInfo.getNE().add(neParts);
		}
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
