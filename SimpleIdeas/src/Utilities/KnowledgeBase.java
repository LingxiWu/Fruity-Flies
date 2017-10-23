package Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * The knowledgeBase instantiate and maintain a series of sensors/actuators.
 * It contains sensor/actuator/signal/event definition and patterns in natural language specification.
 * @author lingxiwu
 *
 */
public class KnowledgeBase {
	
	private ArrayList<SensorActuator> sensorsActuators;
	
	/**
	 * Constructor.
	 */
	public KnowledgeBase() {
		sensorsActuators = new ArrayList<SensorActuator>();		
	}
	
	public ArrayList<SensorActuator> getSensorsActuators(){
		ArrayList<SensorActuator> listOfSensorsActuators = new ArrayList<SensorActuator>();
		listOfSensorsActuators.addAll(sensorsActuators);
		return listOfSensorsActuators;
	}
	
	/**
	 * TO-DO: Eventually to be removed. Testing purpose only. For making dummy sensorsActuators and add them to knowledge base.
	 * @param sensorActuator
	 * @return
	 */
	public void addSampleSensorActuators() {
		// Make a whole bunch of sensors.
		VehicleNumber vehicleNumberSensor = new VehicleNumber("Traffic", "VehicleNumber", "Integer", "SpaceLocation");
		vehicleNumberSensor.addRegexPattern("(?i)(number|quantity)+\\s(of)\\s+(vehicles|cars|automobiles)");
		vehicleNumberSensor.addRegexPattern("(?i)([0-9]|[one|two|three|four|five|six|seven|eight|nine|ten])+\\s+(vehicles|cars|automobiles)");		
		// Add them to the sensorActuators list.
		sensorsActuators.add(vehicleNumberSensor);
	}
	
	public boolean addSensorActuator(SensorActuator sensorActuator) {
		return this.sensorsActuators.add(sensorActuator);
	}
	
	/**
	 * A type a sensor that monitors the number of vehicles in a lane.
	 * Domain: Traffic
	 * Return Value Type: Integer
	 * Param Type: space or location
	 * @author lw2ef
	 *
	 */
	public class VehicleNumber extends SensorActuator{
		
		private final int HISTORICAL_AVERAGE = 25;
		
		public VehicleNumber(String domain, String name, String returnValueType, String paramType) {
			super(domain, name, returnValueType, paramType);
		}
		
		public int getHistoricalAvg() {
			return HISTORICAL_AVERAGE;
		}	
		

	}
	
	/**
	 * A type a sensor that monitors the CO level in a lane or at around some places.
	 * @author lw2ef
	 *
	 */
	public class CarbonMonoxide extends SensorActuator{

		public CarbonMonoxide(String domain, String name, String returnValueType, String paramType) {
			super(domain, name, returnValueType, paramType);
		}
		
	}
	
}
