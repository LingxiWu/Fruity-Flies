package Utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
	
	/**
	 * Given a clause, iterate through the knowledge base and try to identify its associated sensor/actuator
	 * @param clause
	 * @return
	 */
	public SensorActuator identifySensorActuator(String clause) {

		System.out.println("Clause to match: " + clause);
		Iterator<SensorActuator> itr = sensorsActuators.iterator();
		SensorActuator matched = null;
		while(itr.hasNext()) {
			SensorActuator sa = itr.next();
			System.out.println("SensorActuator: " + sa.getName());
			if(sa.match(clause)) {
				matched = sa;
				System.out.println("SensorActuator identified: " + sa.getName());
				break;
			}
		}
		return matched;
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
		VehicleNumber vehicleNumberSensor = new VehicleNumber("Traffic", "VehicleNumber", "Numerical", "SpaceLocation");
		vehicleNumberSensor.addRegexPattern("(?i)(number|quantity)+\\s(of)\\s+(vehicles|cars|automobiles)");
		vehicleNumberSensor.addRegexPattern("(?i)([0-9]|[one|two|three|four|five|six|seven|eight|nine|ten])+\\s+(vehicles|cars|automobiles)");		
		
		CarbonMonoxide CarbonMonoxideSensor = new CarbonMonoxide("Environment", "CarbonMonoxide", "mg", "SpaceLocation");
		CarbonMonoxideSensor.addRegexPattern("(?i)(Carbon monoxide)");
		CarbonMonoxideSensor.addRegexPattern("(?i)((CO))");
		
		
		
		
		// Add them to the sensorActuators list.
		sensorsActuators.add(vehicleNumberSensor);
		sensorsActuators.add(CarbonMonoxideSensor);
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
	
	/**
	 * A type a sensor that monitors the Illumination level in a lane or at around some places.
	 * @author lw2ef
	 *
	 */
	public class Illumination extends SensorActuator{

		public Illumination(String domain, String name, String returnValueType, String paramType) {
			super(domain, name, returnValueType, paramType);
		}
		
	}
	
}
