package Utilities;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Define common characteristics of a sensor/actuator.
 * Parent class for all sensors and actuators.
 * @author lingxiwu
 *
 */
public class SensorActuator {

	String domain; // Environment, Emergency, Traffic, etc...
	String name; // PedestrianNumber, VehicleWaitTime, CO...
	String returnValueType; // Seconds, integer, milligram...
	String paramType; // Space, Location, SpaceAndLocation
	Set<String> regexPatterns; // Possible regular expressions in a given specification.
	Set<String> parseTreeRules; // Possible parse tree rules
	
	
	public SensorActuator(String domain, String name, String returnValueType, String paramType) {
		this.domain = domain;
		this.name = name;
		this.returnValueType = returnValueType;
		this.paramType = paramType;
	}
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReturnValueType() {
		return returnValueType;
	}
	public void setReturnValueType(String returnValueType) {
		this.returnValueType = returnValueType;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public Set<String> getRegexPatterns() {
		return regexPatterns;
	}
	public void setRegexPatterns(Set<String> regexPatterns) {
		this.regexPatterns.addAll(regexPatterns);
	}
	public Set<String> getParseTreeRules() {
		return parseTreeRules;
	}
	public void setParseTreeRules(Set<String> parseTreeRules) {
		this.parseTreeRules = parseTreeRules;
	}
	
	/**
	 * Add a single regular expression to a set of regexPatterns.
	 * @param regexPattern
	 */
	public void addRegexPattern(String regexPattern) {
		if(this.regexPatterns != null) {
			this.regexPatterns.add(regexPattern);
		} else {
			this.regexPatterns = new HashSet<String>();
			this.regexPatterns.add(regexPattern);
		}
	}
	
	public String toString() {	
		String representation = "Domain: " + this.domain + "\n" + "Name: " + this.name + "\n" 
	+ "Parameter Type: " + this.paramType + "\n" + "Return Values: " + this.returnValueType;
		return representation;
	}
	
	/**
	 * It takes a clause and tries to identify a sensor or actuator.
	 * @param clause
	 * @return
	 */
	public boolean match(String clauseToCheck) {
		System.out.println("Clause to match: " + clauseToCheck);
		Boolean matched = false;
		Pattern checkRegex;
		Matcher regexMatcher;
		Iterator<String> itr = regexPatterns.iterator();
		int i=0;
		outerloop:
		while(itr.hasNext()) {
			i++;
			String regex = itr.next();
			System.out.println("# " + i + " Regex for sensor/actuator " + this.name + ": " + regex);
			checkRegex = Pattern.compile(regex);
			regexMatcher = checkRegex.matcher(clauseToCheck);
			
			while(regexMatcher.find()) {
				if(regexMatcher.group().length() != 0) {
					System.out.println("Signal expression: " + regexMatcher.group().trim());
					matched = true;
					break outerloop;
				}
			}
		}
		return matched;
	}

}
