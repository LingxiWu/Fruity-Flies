package SimpleIdeaPlayground;

import java.util.HashSet;
import java.util.Set;

import Utilities.ParserUtility;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation;

public class Playground {

	static ParserUtility parserHelper = new ParserUtility();
	
	public static void main(String[] args) {

		
		
//	    String text = "The number of vehicles in a lane should never exceed its maximum vehicle capacity.";
//		String text = "No vehicles around UVA campus should be delayed for more than 50 seconds.";
//		String text = "Emergency vehicles should not wait for more than 10 seconds at an intersection.";
		String text = "The camera should be keep on after 5 am.";
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    parserHelper.getPipeline().annotate(document);

//	    System.out.println(document.get(SentencesAnnotation.class).get(0).get(BasicDependenciesAnnotation.class).toString());
	    
//	    ParserUtility.extractNamedEntity(text);
//	    String[] s = ParserUtility.extractNounSubject(text);
//	    System.out.println(s[1]);
	    ParserUtility.extractNamedEntity(text);

	}
	
	public static void testDuration(String sentense) {
		
		
	}

}
