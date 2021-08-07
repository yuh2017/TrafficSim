
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.NetworkWriter;
import org.matsim.core.scenario.ScenarioUtils;

public class createNetwork {
	private static final long CAP_FIRST_LAST = 3600; // [veh/h]
	// capacity at all other links
	private static final long CAP_MAIN = 1800; // [veh/h]

	// link length for all other links
	private static final long LINK_LENGTH = 200; // [m]
	// travel time for links that all agents have to use
	private static final double MINIMAL_LINK_TT = 1; // [s]
	
	public static void printArray(String[] rows) {
	      for (int i = 0; i < rows.length; i++) {
	         System.out.println(i + " , " +rows[i]);
	         System.out.print('\n');
	      }
	      System.out.println("\nlength of the array is "+ rows.length);
	      }
	
	public static ArrayList<TransNode> readNodes(String csvFile) {
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<TransNode> AllNodes = new ArrayList<TransNode>();
		try {
			br = new BufferedReader(new FileReader(csvFile));
			br.readLine();
			while ((line = br.readLine()) != null) {
					String[] dataline = line.split(cvsSplitBy);
//					printArray(dataline) ;
					TransNode nodei = new TransNode();
					nodei.setCoords(dataline) ;
					AllNodes.add(nodei);
	           }
			}catch (FileNotFoundException e){
				e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
		System.out.println("Finished reading Nodes!");
		return AllNodes;
	}
	
public static ArrayList<TransEdge> readEdges(String csvFile) {
	
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
	ArrayList<TransEdge> AllEdges = new ArrayList<TransEdge>();
	try {
		br = new BufferedReader(new FileReader(csvFile));
		br.readLine();
		int idx = 0;
		while ((line = br.readLine()) != null) {
				String[] dataline = line.split(cvsSplitBy);
//				printArray(dataline) ;
				TransEdge edgei = new TransEdge(idx);
				edgei.setEdge(dataline);
				idx += 1;
//                householdi.displayhousehold();
                AllEdges.add(edgei);
           }
		}catch (FileNotFoundException e){
			e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	System.out.println("Finished reading Edges!");
	return AllEdges;
}


	public static void main(String[] args) {
		String NodePth = "C:\\Users\\zanwa\\OneDrive\\Desktop\\SouthFlorida\\MiamiSimData\\MiamiNodes_100_slr.csv";	
		String edgePth = "C:\\Users\\zanwa\\OneDrive\\Desktop\\SouthFlorida\\MiamiSimData\\MiamiEdges_100_slr.csv";	
		
		ArrayList<TransNode> SFLNodes = readNodes(NodePth); 		
		ArrayList<TransEdge> SFLEdges = readEdges(edgePth); 		
		
		Config config = ConfigUtils.createConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		Network net = scenario.getNetwork();
		NetworkFactory fac = net.getFactory();

		// create nodes
		
		HashMap<Integer, Node> NodeMap = new HashMap<Integer, Node>() ;
		for (TransNode entry : SFLNodes) {
			Node n0 = fac.createNode(Id.createNodeId(entry.NodeID), new Coord(entry.x, entry.y));
			net.addNode(n0);
			NodeMap.put(entry.NodeID, n0) ;
		}
		
		// create links
		ArrayList<String> linkIDs = new ArrayList<String>();
		for (TransEdge entry2 : SFLEdges) {
			long Capacity = (long) (entry2.Speed_limit / entry2.DISTANCE ) * entry2.LANES;
			double speed = entry2.Speed_limit * 1609.34 / 3600;
			
			if( entry2.TWOWAY > 0 ){
				String newStr = entry2.BeginID + "_" + entry2.EndID;
//				String newStr2 = entry2.EndID + "_" + entry2.BeginID;
//				System.out.println(newStr + " "+ NodeMap.get(entry2.BeginID) + " "+ NodeMap.get(entry2.EndID));
				linkIDs.add(newStr);
				Link l = fac.createLink(Id.createLinkId(newStr), NodeMap.get(entry2.BeginID), NodeMap.get(entry2.EndID));
				setLinkAttributes(l, Capacity, entry2.DISTANCE * 1609.34, entry2.LANES, speed);
				net.addLink(l);
				
//				linkIDs.add(newStr2);
//				Link l2 = fac.createLink(Id.createLinkId(newStr2), NodeMap.get(entry2.EndID), NodeMap.get(entry2.BeginID));
//				setLinkAttributes(l2, Capacity, entry2.DISTANCE * 1609.34, entry2.LANES, speed);
//				net.addLink(l2);
			}else{
				String newStr = entry2.BeginID + "_" + entry2.EndID;
				linkIDs.add(newStr);
				Link l = fac.createLink(Id.createLinkId(newStr), NodeMap.get(entry2.BeginID), NodeMap.get(entry2.EndID));
				setLinkAttributes(l, Capacity, entry2.DISTANCE * 1609.34, entry2.LANES, speed);
				net.addLink(l);
			}
		}
		// write network
		new NetworkWriter(net).write("C:\\Users\\zanwa\\OneDrive\\Desktop\\SouthFlorida\\MiamiSimData\\SFLNetwork_100_SLR.xml");
		System.out.println("Net size "+SFLEdges.size());
	}

	
	private static void setLinkAttributes(Link link, double capacity, double length, int lanes, double speed) {
		link.setCapacity(capacity);
		link.setLength(length);
		link.setNumberOfLanes(lanes);
		
		// agents have to reach the end of the link before the time step ends to
		// be able to travel forward in the next time step (matsim time step logic)
		link.setFreespeed( speed );
	}

}
