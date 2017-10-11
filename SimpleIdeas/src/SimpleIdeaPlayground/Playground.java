package SimpleIdeaPlayground;

import Utilities.ParserUtility;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.EnhancedDependenciesAnnotation;

public class Playground {

	public static void main(String[] args) {

		ParserUtility parserHelper = new ParserUtility();
		
	    // read some text in the text variable
	    String text = "The number of vehicles in a lane should never exceed its maximum vehicle capacity.";

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    parserHelper.getPipeline().annotate(document);
//	    System.out.println(document);
//	    document.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class).get(0).get(CollapsedCCProcessedDependenciesAnnotation.class).toString();
	    
//	    for(String token : document.get(SentencesAnnotation.class).get(0).get(CollapsedCCProcessedDependenciesAnnotation.class).toString()) {
//	    	System.out.println(token.get(TokensAnnotation.class).toString());
//	    }
	    
	    System.out.println(document.get(SentencesAnnotation.class).get(0).get(BasicDependenciesAnnotation.class).toString());
	    
//	    System.out.println(document.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class).get(0).toString());
	    
	    
	}

}
