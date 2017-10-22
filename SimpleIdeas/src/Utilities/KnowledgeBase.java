package Utilities;

import java.util.Set;

/**
 * The knowledgeBase instantiate and maintain a series of sensors/actuators.
 * It contains sensor/actuator/signal/event definition and patterns in natural language specification.
 * @author lingxiwu
 *
 */
public class KnowledgeBase {
	
	public class VehicleNumber extends SensorActuator{

		private final int HISTORICAL_AVERAGE = 25;
		
		public VehicleNumber(String domain, String name, String returnValueType, String paramType) {
			super(domain, name, returnValueType, paramType);
		}
		
		public int getHistoricalAvg() {
			return HISTORICAL_AVERAGE;
		}
		
	}
	
	public class CarbonMonoxide extends SensorActuator{

		public CarbonMonoxide(String domain, String name, String returnValueType, String paramType) {
			super(domain, name, returnValueType, paramType);
		}
		
	}
	
}
