package Utilities;

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
		this.regexPatterns = regexPatterns;
	}
	public Set<String> getParseTreeRules() {
		return parseTreeRules;
	}
	public void setParseTreeRules(Set<String> parseTreeRules) {
		this.parseTreeRules = parseTreeRules;
	}
	
//	public static void regexChecker(String theRegex, String str2Check) {
//		Pattern checkRegex = Pattern.compile(theRegex);
//		Matcher regexMatcher = checkRegex.matcher(str2Check);
//		
//		while(regexMatcher.find()) {
//			if(regexMatcher.group().length() != 0) {
//				System.out.println(regexMatcher.group().trim());
//			}
//		}
//	}
	
	
}
