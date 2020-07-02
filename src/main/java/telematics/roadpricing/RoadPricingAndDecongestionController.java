/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package telematics.roadpricing;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.decongestion.DecongestionConfigGroup;
import org.matsim.contrib.decongestion.DecongestionControlerListener;
import org.matsim.contrib.decongestion.DecongestionModule;
import org.matsim.contrib.decongestion.data.DecongestionInfo;
import org.matsim.contrib.decongestion.handler.DelayAnalysis;
import org.matsim.contrib.decongestion.handler.IntervalBasedTolling;
import org.matsim.contrib.decongestion.handler.IntervalBasedTollingAll;
import org.matsim.contrib.decongestion.handler.PersonVehicleTracker;
import org.matsim.contrib.decongestion.routing.TollTimeDistanceTravelDisutilityFactory;
import org.matsim.contrib.roadpricing.RoadPricingConfigGroup;
import org.matsim.contrib.roadpricing.RoadPricingModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.scenario.ScenarioUtils;
import telematics.common.RouteTTObserver;

public class RoadPricingAndDecongestionController {

	public static void main(String[] args) {
		Gbl.assertIf(args.length >= 1 && args[0] != "");
		run(ConfigUtils.loadConfig(args[0], new ConfigGroup[]{new DecongestionConfigGroup(), new RoadPricingConfigGroup()}));
	}

	static void run(Config config) {
//		config.plansCalcRoute().setRoutingRandomness(0.);
		Scenario scenario = ScenarioUtils.loadScenario(config);
		Controler controler = new Controler(scenario);

		DecongestionConfigGroup decongestionConfigGroup = (DecongestionConfigGroup) scenario.getConfig().getModules().get(DecongestionConfigGroup.GROUP_NAME);
		if(decongestionConfigGroup.isEnableDecongestionPricing()){
			controler.addOverridingModule(new DecongestionModule(scenario));
			final TollTimeDistanceTravelDisutilityFactory travelDisutilityFactory = new TollTimeDistanceTravelDisutilityFactory();
			travelDisutilityFactory.setSigma(config.plansCalcRoute().getRoutingRandomness());
			controler.addOverridingModule(new AbstractModule() {
				@Override
				public void install() {
					bindCarTravelDisutilityFactory().toInstance(travelDisutilityFactory);
				}
			});
		} else {
			controler.addOverridingModule(new RoadPricingModule());
			controler.addOverridingModule(new AbstractModule() {
				@Override
				public void install() {
					this.bind(DecongestionInfo.class).asEagerSingleton();

//					this.bind(IntervalBasedTollingAll.class).asEagerSingleton();
//					this.bind(IntervalBasedTolling.class).to(IntervalBasedTollingAll.class);
//					this.addEventHandlerBinding().to(IntervalBasedTollingAll.class);

					this.bind(DelayAnalysis.class).asEagerSingleton();
					this.addEventHandlerBinding().to(DelayAnalysis.class);

					this.addEventHandlerBinding().to(PersonVehicleTracker.class).asEagerSingleton();

					this.addControlerListenerBinding().to(DecongestionControlerListener.class);
				}
			});
		}
		controler.addOverridingModule(new AbstractModule() {
			public void install() {
				addControlerListenerBinding().to(RouteTTObserver.class);

			}
		});

		controler.run();
	}

}
