import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.time.Duration;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;

public class PopGen2 {

	
	
	public static void main(String args[]) {
		Scenario scenario = createPopulationFromFile("C:\\Users\\zanwa\\OneDrive\\Desktop\\SouthFlorida\\MiamiSimData\\GeneratedTrips_popChange.csv");

		/*  "C:\\Users\\yuh46\\Desktop\\Tampa\\ReadResults\\GeneratedTrips.csv"
		 * Write population to file.
		 */
		PopulationWriter populationWriter = new PopulationWriter(scenario.getPopulation(), scenario.getNetwork());
		populationWriter.write("C:\\Users\\zanwa\\OneDrive\\Desktop\\SouthFlorida\\MiamiSimData\\Miamipopulation_popChange.xml");
	}
	
	private static Scenario createPopulationFromFile(String censusFile) {
		Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

		List<PersonInfo> TripEntries = new PopTrip().readFile(censusFile);
		System.out.println("The number of agents is " + TripEntries.size());
		Population population = scenario.getPopulation();
		PopulationFactory populationFactory = population.getFactory();
		
		Map<Integer, List<PersonInfo>> personEntryMapping = new TreeMap<>();
		
		for (PersonInfo tripEntry : TripEntries) {
			List<PersonInfo> entries = personEntryMapping.get(tripEntry.id_person);

			/*
			 * If no mapping exists -> create a new one
			 */
			if (entries == null) {
				entries = new ArrayList<>();
				personEntryMapping.put(tripEntry.id_person, entries);
				}
			entries.add(tripEntry);
		}
		
		for (List<PersonInfo> personEntries : personEntryMapping.values()) {
			PersonInfo entry = personEntries.get(0);
			/*
			 * Get id of the person from the censusEntry.
			 */
			int idPerson = entry.id_person;
			Person person = populationFactory.createPerson(Id.create(idPerson, Person.class));
			
			Coord homeCoord = new Coord(entry.h_x, entry.h_y);
			Coord endCoord = null;
			String transportMode = null;
			Leg leg = null;
			Activity activity = null;	
			for (PersonInfo personEntry : personEntries) {
				
				Plan plan = populationFactory.createPlan();
				
				Activity previousActivity = populationFactory.createActivityFromCoord("home", homeCoord);
				previousActivity.setEndTime( entry.starttime*60*60 );
				plan.addActivity(previousActivity);
				plan.addLeg(populationFactory.createLeg(TransportMode.car));
				
				endCoord = new Coord(personEntry.d_x, personEntry.d_y);
				String activityType = getActivityType( personEntry.trippurpose);
				Activity workActivity = populationFactory.createActivityFromCoord(activityType, endCoord);
				workActivity.setEndTime(entry.endtime*60*60);
				plan.addActivity(workActivity);
				plan.addLeg(populationFactory.createLeg(TransportMode.car));
				
				Activity homeEvening = populationFactory.createActivityFromCoord("home", homeCoord);
				plan.addActivity(homeEvening);
				
				person.addPlan(plan);
				
			}
			population.addPerson(person);
		}
		return scenario;

	}
	private static Duration randomize(Duration time) {
		long minutes = (long) ((Math.random() * 60) - 30);
		return time.plusMinutes(minutes);
	}
	private static String getTransportMode(int mode) {
		switch (mode) {
		case 1: return TransportMode.walk;
		case 2: return TransportMode.bike;
		case 3: return TransportMode.car;
		case 4: return TransportMode.pt;
		case 5: return "undefined";
		default: return "undefined";
		}
	}

	private static String getActivityType(int activityType) {
		switch (activityType) {
		case 1: return "work";
		case 2: return "education";
		case 3: return "shop";
		case 4: return "leisure";
		case 5: return "other";
		default: return "undefined";
		}
}
}
