package Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
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
		props.put("annotators", "tokenize,ssplit,parse,lemma,ner,dcoref");
		pipeline = new StanfordCoreNLP(props);
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
	public static String[] extractNounSubject(String clause) {
		
		String[] nounSubject = new String[2];
		Annotation doc = new Annotation(clause);
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
	 * Loop over and see 
	 * @param rawDependencyTree
	 * @return
	 */
	public static int numOfNSubject(String rawDependencyTree) {
		int numOfNSubjects = 0;
		for(String dependencySentence : rawDependencyTree.split("->")) {		
			for(SubjectTags st : SubjectTags.values()) {
				if(dependencySentence.contains(st.toString())){
					numOfNSubjects++;
				}
			}
		}	
		return numOfNSubjects;
	}
	
	/**
	 * Remove the ending punctuation like period. It kind messes up the parse tree sometimes.
	 * @param origStr
	 * @return
	 */
	public static String removeEndPunctuation(String origStr) {
		if(!Character.isDigit(origStr.charAt(origStr.length()-1)) && !Character.isLetter(origStr.charAt(origStr.length()-1))){
			origStr = origStr.substring(0, origStr.length()-1);
		}
		return origStr;
	}
	
	/**
	 * Find the first word after SBAR tag as the begining word of dependent clause.
	 * @param parseTree
	 * @return
	 */
	public static String dependentClauseStartWord(String parseTree) {
		int startIndex = parseTree.indexOf("(SBAR");
		String partialParseTree;
		String startWord = "";
		if(startIndex != -1) {
			partialParseTree = parseTree.substring(startIndex);
			String[] parts = partialParseTree.split(" ");
			for(int i=0;i<parts.length;i++) {
				if(!parts[i].contains("(")){
					startWord = parts[i].substring(0,parts[i].length()-1);
					System.out.println("Dependent Clause starts here: " + startWord);
					break;
				}
			}
		}
		return startWord;
	}
	
	/**
	 * It finds the last word of a dependent clause.
	 * e.g. 
	 * @param parseTree
	 * @return
	 */
	public static String dependentClauseEndWord(String parseTree) {
		
		Stack<String> stack = new Stack<String>();
		String partialParseTree;
		String endWord = "";
		
		int startIndex = parseTree.indexOf("(SBAR");
		if(startIndex != -1) {
			partialParseTree = parseTree.substring(startIndex);
			System.out.println(partialParseTree);
			stack.push("(");
			for(int i=1; i<partialParseTree.length()-1; i++) {
				if(stack.size() == 0) {
					// traverse backwards for the end word of that dependent clause.
					for(int j=i-1; j>0; j--) {							 
						if(partialParseTree.charAt(j)!=')') {								 
							endWord += partialParseTree.charAt(j);		
							if(partialParseTree.charAt(j)==' ') {
								break;
							}
						} 
					}
					System.out.println("Dependent Clause ends here: " + new StringBuilder(endWord).reverse().toString());
					break;
				}
				if(partialParseTree.charAt(i) == '(') {
					stack.push("(");
				} else if(partialParseTree.charAt(i) == ')') {
					stack.pop();
				}					 
			}	 
		}		
		
		endWord = new StringBuilder(endWord).reverse().toString();
		return endWord;
	}

	/**
	 * Build a hash map that stores Named Entities: LOCATION -> "Baker Street" 
	 * Be careful with 0 and O... For some reason Stanford Core NLP uses O not 0.
	 * @param sentence
	 */
	public static HashMap<String, String> extractNamedEntities(String clause) {
		
		System.out.println("Performing Named Entity Extraction Process ... ");
		
		Annotation doc = new Annotation(clause);
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

		System.out.println(tempNEClassHolder.toString());
		System.out.println(tempNE.toString());
		return namedEntities;
		
	}
	
	/**
	 * This method is generalized based on imperical observation and English Grammer.
	 * Given a specification sentence, determine if it's a simple sentence, complex sentence, or compound sentence.
	 * e.g. 1 Complex: When the illumination is set to level 3, the carbon monoxide (CO) emission in a lane should be no more than 50 mg.
	 * 		2 Compound: The camera should be turned on, and the illumination of street should be set at least level 3 between 10 PM to 7 AM.
	 * 		0 Simple: The noise level in a lane should always be less than 70 dB.
	 * 		For the time being we don't consider compound-comlex sentence.
	 * Rules: Complex sentence has SBAR tag. Compound sentence has two or more s tags. Simple sentence has only one NSUBJ and one 
	 * @param sentence
	 * @return
	 */
	public static int determineSentenceType(String specification) {
		
		System.out.println("Analyzing Sentence Type ... ");
		
		int sentenceType = -1;
		String type = "";
		
		// Run parse tree on that sentence.
		Annotation doc = new Annotation(specification);
		pipeline.annotate(doc);
		String rawDependencyTree = doc.get(SentencesAnnotation.class).get(0).get(BasicDependenciesAnnotation.class).toString(); // A dependency parse tree.
		Tree parseTree = doc.get(SentencesAnnotation.class).get(0).get(TreeAnnotation.class);
		System.out.println(parseTree.toString());
		if(parseTree.toString().contains("(SBAR")) {
			sentenceType = 1; // a complex sentence.
			type = "Complex";
		} else if(parseTree.toString().contains("(CC ")) { // (cc along is not enough.
			sentenceType = 2;
			type = "Compound";
		} else {
			sentenceType = 0;
			type = "Simple";
		}
		
		
		System.out.println("Sentence Type: " + type);
		
		return sentenceType;
	}
	
	/**
	 * Takes the sample specification and sentence type as a clue to extract clause(s).
	 * It seems to be more natural and concise to describe around one sensor/actuator 
	 * @param specification
	 * @param sentenceType
	 * @return
	 */
	public static HashMap<String, String> extractClauses(String specification, int sentenceType){
		
		System.out.println("Extracting clauses ... ");
		
		HashMap<String, String> clauses = new HashMap<String, String>();
		
		if(sentenceType == 0) { // Simple
			clauses.put("independent", specification);
		} else if(sentenceType == 1) { // Complex
			// Find the dependent clause first. It's the part followed by SBAR.
			
		}
		
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
	
}
