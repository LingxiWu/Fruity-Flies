package SimpleIdeaPlayground;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utilities.ParserUtility;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import Utilities.KnowledgeBase;
public class Playground {

	static ParserUtility parserHelper = new ParserUtility();
	
	public static void main(String[] args) {

		
		
//	    String text = "The quantity of vehicles in a lane should never exceed its maximum vehicle capacity.";
//		String text = "No vehicles around UVA campus should be delayed for more than 50 seconds.";
//		String text = "There are 50% chance that a Emergency vehicle could wait for more than 10 seconds at an intersection arter 5 am on Emmet Street.";
//		String text = "The camera should be keep on after 5 o'clock.";
//		String text = "There cannot be more than five cars in a lane.";
	    // create an empty Annotation just with the given text
//		String text = "When there are less than 5 vehicles."; // dependent clause
//	    String text = "When the illumination is set to level 3, the carbon monoxide (CO) emission in a lane should be no more than 50 mg."; // Complex sentence
//	    String text = "The camera should be turned on, and the illumination of street should be set at least level 3 between 10 PM to 7 AM."; // Compound sentence.
//		String text = "the illumination of street should be set at least level 3 between 5 AM and 12 AM If the number of cars is less than 10."; // Complex sentence.
		String text = "after we eat cake, we played football.";
	    Annotation document = new Annotation(parserHelper.removeEndPunctuation(text));

	    // run all Annotators on this text
	    parserHelper.getPipeline().annotate(document);

//	    System.out.println(document.get(SentencesAnnotation.class).get(0).get(BasicDependenciesAnnotation.class).toString());
	    
//	    String[] s = ParserUtility.extractNounSubject(text);
//	    System.out.println(s[1]);
//	    HashMap<String, String> hp = ParserUtility.extractNamedEntities(text);
//	    String regex = "(?i)(number|quantity)+\\s(of)\\s+(vehicles|cars|automobiles)"; 
//	    String regex = "(?i)([0-9]|[one|two|three|four|five|six|seven|eight|nine|ten])+\\s+(vehicles|cars|automobiles)"; 
//	    regexChecker(regex,text);
//	    System.out.println(text.matches(regex));
	    
//	    KnowledgeBase knowledgeBase = new KnowledgeBase();
//	    knowledgeBase.addSampleSensorActuators();
//	    System.out.println(knowledgeBase.identifySensorActuator(text).toString());
	    
//	    System.out.println("");
//	    parserHelper.determineSentenceType(text);
	    String parsedTree = document.get(SentencesAnnotation.class).get(0).get(TreeAnnotation.class).toString();
	    System.out.println(parsedTree);
	    parserHelper.dependentClauseStartWord(parsedTree);
	    parserHelper.dependentClauseEndWord(parsedTree);
	    
//	    System.out.println(parsedTree.substring(151));
	    
//	    System.out.println(knowledgeBase.getSensorsActuators().get(0).match(text));
	    
	    
	    
	}
	    

	public static void regexChecker(String theRegex, String str2Check) {
		Pattern checkRegex = Pattern.compile(theRegex);
		Matcher regexMatcher = checkRegex.matcher(str2Check);
		
		while(regexMatcher.find()) {
			if(regexMatcher.group().length() != 0) {
				System.out.println(regexMatcher.group().trim());
			}
		}
		
		
	}
}
