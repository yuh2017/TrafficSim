import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PersonTrip {
	
	private String separator = ",";
	private Charset charset = Charset.forName("UTF-8");
	
	public PersonTrip() {
		
	}
	public static void printArray(String[] rows) {
	      for (int i = 0; i < rows.length; i++) {
	         System.out.println(i + " , " +rows[i] + '\n');
	      }
	      System.out.println("\nlength of the array is "+ rows.length);
	      }
	
	public List<PersonInfo> readFile(String inFile)
	{
		List<PersonInfo> entries = new ArrayList<PersonInfo>();
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
	    BufferedReader br = null;
    	try 
    	{
    		fis = new FileInputStream(inFile);
    		isr = new InputStreamReader(fis, charset);
			br = new BufferedReader(isr);
			
			// skip first Line
			br.readLine();
			 
			String line;
			while((line = br.readLine()) != null)
				
			{
				PersonInfo HouseholdEntry = new PersonInfo();
				
				String[] cols = line.split(separator);
				
//				printArray(cols);
				
				HouseholdEntry.id_person = (int) Double.parseDouble( cols[0] );
				
				HouseholdEntry.TAZ = (int) Double.parseDouble( cols[7] );
				
				
				HouseholdEntry.h_x = parseDouble(cols[5]);
				HouseholdEntry.h_y = parseDouble(cols[6]);
				HouseholdEntry.s_x = parseDouble(cols[5]);
				HouseholdEntry.s_y = parseDouble(cols[6]);
				HouseholdEntry.d_x = parseDouble(cols[2]);
				HouseholdEntry.d_y = parseDouble(cols[3]);
				
				if( cols[11] == "06:00:00") {
					HouseholdEntry.starttime = 6;
				}else if(cols[11] == "07:00:00") {
					HouseholdEntry.starttime = 7;
				}else if (cols[11] == "08:00:00") {
					HouseholdEntry.starttime = 8;
				}else if(cols[11] == "09:00:00"){
					HouseholdEntry.starttime = 9;
				}else {
					HouseholdEntry.starttime = 10;
				}
			
				if( cols[9] == "07:00:00") {
					HouseholdEntry.endtime = 7;
				}else if(cols[9] == "08:00:00") {
					HouseholdEntry.endtime = 8;
				}else if (cols[9] == "09:00:00") {
					HouseholdEntry.endtime = 9;
				}else if(cols[9] == "10:00:00"){
					HouseholdEntry.endtime = 10;
				}else {
					HouseholdEntry.endtime = 11;
				}
				
				HouseholdEntry.tripmode = 3;
				if(cols[12] == "work") {
					HouseholdEntry.trippurpose = 1;
				}else if( cols[12] == "education" ) {
					HouseholdEntry.trippurpose = 2;
				}else if( cols[12] == "shop" ) {
					HouseholdEntry.trippurpose = 3;
				}else {
					HouseholdEntry.trippurpose = 4;
				}
				
				HouseholdEntry.tripdistance = Math.sqrt( Math.pow(HouseholdEntry.s_x - HouseholdEntry.d_x, 2) + Math.pow(HouseholdEntry.s_y - HouseholdEntry.d_y, 2));
				HouseholdEntry.tripduration = (int) HouseholdEntry.tripdistance / 1000/ 30  ;
				HouseholdEntry.id_tour = parseInteger(cols[0]);
								
				entries.add(HouseholdEntry);
			}
			
			br.close();
			isr.close();
			fis.close();
    	}
    	catch (FileNotFoundException e) 
    	{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return entries;
	}
	
	private int parseInteger(String string)
	{
		if (string == null) return 0;
		else if (string.trim().isEmpty()) return 0;
		else return Integer.valueOf(string);
	}
	
	private double parseDouble(String string){
		if (string == null) return 0.0;
		else if (string.trim().isEmpty()) return 0.0;
		else return Double.valueOf(string);
	}

}

