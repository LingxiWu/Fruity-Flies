package SimpleIdeaPlayground;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utilities.ParserUtility;
import Utilities.SensorActuator;
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
	
		KnowledgeBase knowledgeBase;
	
	public static void main(String[] args) {
		String text = "The CO emission on Interstate 69 (I-69) Street should always be no more than 50 mg."; // Simple sentence.
//	    String text = "When the number of vehicles is more than 50, the carbon monoxide (CO) emission on highway US-29 should be no more than 50 mg."; // Complex sentence.
//	    String text = "The camera should be turned on, and the illumination of street should be set at least level 3 between 10:00 and 21:00"; // Compound sentence.
		
//	    String text = "The quantity of vehicles in a lane should never exceed its maximum vehicle capacity.";
//		String text = "No vehicles around UVA campus should be delayed for more than 50 seconds.";
//		String text = "An Emergency vehicle could wait for more than 10 seconds at an intersection after 17:00 a.m. on Emmet Street.";
//		String text = "The camera should be keep on after 5 o'clock.";
//		String text = "There cannot be more than five cars in a lane.";
//		String text = "When there are less than 5 vehicles."; // dependent clause

//		String text = "the illumination of street should be set at least level three between 17:00 and 23:00 If the number of cars is less than 10."; // Complex sentence.
//		String text = "Any given lane can emit no more than 50 mg of carbon monoxide.";

//	    String text = "I like apple, and I drink water.";
//		String text = "The camera should be turned on and the noise level should be less than 50 dB when there are less than 50 vehicles.";
//		String text = "The camera should be turned on, and the noise level should be less than 50 dB when there are more than 50 vehicles or the pedestrian number is over 65.";
//	    String text = "The camera should be turned on and the illumination of street light should be set at least level 3 within a time interval.";
//	    String text = "Both the cockroach and the bird would get along very well without us, although the cockroach would miss us most.";
//	    String text = "City parks should be well illuminated during the evenings and nighttime.";
//		String text = "Buses on public transport should reach each stop within thirty minutes.";
//	    String text = "City parks should be equipped with street light during the evenings and nighttime.";
//		String text = "The camera should be on from 5:00 to 16:00";
//		String text = "the lightening flashed, the rain fell, and the wind blew";
//		String text = "Trash and waste should be removed from the street within sixty minutes.";
		
		System.out.println("Demo. Simple cases...");
		System.out.println("\nSpecification: "+text);
		
	    //	Annotation document = new Annotation(parserHelper.removeEndPunctuation(text));
		Annotation document = new Annotation(text);
	    // run all Annotators on this text
	    parserHelper.getPipeline().annotate(document);
	    String parsedTree = document.get(SentencesAnnotation.class).get(0).get(TreeAnnotation.class).toString();
	    System.out.println(parsedTree);

//	    System.out.println(document.get(SentencesAnnotation.class).get(0).get(BasicDependenciesAnnotation.class).toString());
	    
	    KnowledgeBase knowledgeBase = new KnowledgeBase();
	    knowledgeBase.addSampleSensorActuators();
//	    HashMap<String, String> namedEntities = ParserUtility.extractNamedEntities(text);
	    
	    
	    runThroughSimpleCases(text, knowledgeBase);
	   
	}
	
	public static HashMap<String, String> organizeClauses(String specification, int sentenceType){
		HashMap<String, String> clauses = new HashMap<String, String>();
		if(sentenceType == 0) { // Simple
			clauses.put("simple", specification);
		} else if (sentenceType == 1) { // Complex
			String dependentClause = ParserUtility.extractDependentClause(specification);
	    		String independentClause = specification.replaceAll(dependentClause, "");
	    		System.out.println("Independent Clause: "+independentClause);
	    		clauses.put("dependentClause", dependentClause);
	    		clauses.put("independentClause", independentClause);
		} else if (sentenceType == 2) { // Compound
			ArrayList<String> independentClauses = ParserUtility.extractIndependentClauses(specification);
			String key = "independentClause";
			int i = 0;
			for(String c: independentClauses) {
				i++;
				String id = key+Integer.toString(i);
				clauses.put(id, c);
	    		}
		} else if (sentenceType == 3) {
			System.out.println("Overly complicated statement...");
		}
		
		return clauses;
	}
	
	// A testing case where location and threshold should be provided in the specification.
	public static void runThroughSimpleCases(String specification, KnowledgeBase knowledgeBase) {
		
		// Step 1: identify sentence structure: 
		// 		   simple -> clause, 
		//         compound -> independent clause + independent clause, 
		//         complex -> independent clause + dependent clause
		int sentenceType = ParserUtility.determineSentenceType(specification);
		 
		// Step 2: extract clauses from specification, and put them into a hashmap
		HashMap<String, String> clauses = organizeClauses(specification, sentenceType);
		
		// Step 3: loop through clauses and match sensor/actuators.
		HashMap<String, SensorActuator> clauseSAMap = new HashMap<String, SensorActuator>();
		for (HashMap.Entry<String, String> entry : clauses.entrySet()) {
		    String key = entry.getKey();
		    String clause = entry.getValue();
		    SensorActuator sensorActuator = knowledgeBase.identifySensorActuator(clause);
		    
		    if(sensorActuator!=null) {
    	    			clauseSAMap.put(key, sensorActuator);
		    }
		}
		
		// Step 4: loop through clauses and extract named entities such as location and duration
		HashMap<String, HashMap<String, String>> clauseNEMap = new HashMap<String, HashMap<String, String>>();
		for (HashMap.Entry<String, String> entry : clauses.entrySet()) {
		    String key = entry.getKey();
		    String clause = entry.getValue();
		    HashMap<String, String> namedEntities = ParserUtility.extractNamedEntities(clause);
		    
		    if(namedEntities.size()!=0) {
    	    			clauseNEMap.put(key, namedEntities);
		    }
		}
		
		// Step 5: TO-DO:
		if(sentenceType == 0) {
			// check threshold value
			
		}
		
		
	}
	
	 
	
}
