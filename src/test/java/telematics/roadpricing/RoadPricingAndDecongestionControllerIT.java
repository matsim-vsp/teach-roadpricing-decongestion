package telematics.roadpricing;

import org.junit.Rule;
import org.matsim.contrib.decongestion.DecongestionConfigGroup;
import org.matsim.contrib.roadpricing.RoadPricingConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.ConfigUtils;
import org.matsim.testcases.MatsimTestUtils;

import static org.junit.Assert.*;

public class RoadPricingAndDecongestionControllerIT {

	@Rule
	public MatsimTestUtils utils = new MatsimTestUtils();

	@org.junit.Test
	public void run() {
		Config config = ConfigUtils.loadConfig("scenarios/2routes/roadpricingDecongestionConfig.xml", new ConfigGroup[]{new DecongestionConfigGroup(), new RoadPricingConfigGroup()});
		config.controler().setLastIteration(3);
		config.controler().setOutputDirectory(utils.getOutputDirectory());
		RoadPricingAndDecongestionController.run(config);
	}
}