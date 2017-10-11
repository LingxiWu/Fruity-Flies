package SimpleIdeaPlayground;

import Utilities.ParserUtility;
import edu.stanford.nlp.pipeline.Annotation;

public class Playground {

	public static void main(String[] args) {

		ParserUtility parserHelper = new ParserUtility();
		
	    // read some text in the text variable
	    String text = "No incidence of vehicle collision should occur.";

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    parserHelper.getPipeline().annotate(document);

	    System.out.println('a');
	}

}
