import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.algorithms.NetworkCleaner;
import org.matsim.core.scenario.ScenarioUtils;

public class RunFromConfigfileExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String configFile ;
		if ( args!=null && args.length>=1 ) {
			configFile = args[0] ;
		} else {
			configFile = "C:\\Users\\zanwa\\OneDrive\\Desktop\\SouthFlorida\\MiamiSimData\\example1-config.xml" ;
		}

		Config config = ConfigUtils.loadConfig(configFile);
		
		Scenario scenario = ScenarioUtils.loadScenario(config);
		NetworkCleaner cleaner = new NetworkCleaner() ;
        cleaner.run( scenario.getNetwork() ) ;
		Controler controler = new Controler(scenario);
		controler.run();
	}

}
