package telematics.roadpricing;

import org.junit.Rule;
import org.matsim.contrib.decongestion.DecongestionConfigGroup;
import org.matsim.contrib.roadpricing.RoadPricingConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.ConfigUtils;
import org.matsim.testcases.MatsimTestUtils;

import static org.junit.Assert.*;

public class RoadPricingWithPredictedRouteTTControllerIT {


	@Rule
	public MatsimTestUtils utils = new MatsimTestUtils();

	@org.junit.Test
	public void run() {
		RoadPricingWithPredictedRouteTTController.main(new String[]{"scenarios/2routes/roadpricingTelematicsConfig.xml"});
	}


}