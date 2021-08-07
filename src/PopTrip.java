import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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

public class PopTrip {
	
	private String separator = ",";
	private Charset charset = Charset.forName("UTF-8");
	
	public PopTrip() {
		
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
			String puri;
			String line;
			while((line = br.readLine()) != null  ){ 
				if (Math.random() < 1) {

					PersonInfo HouseholdEntry = new PersonInfo();
				
					String[] cols = line.split(separator);
				
//					printArray(cols);
				
					HouseholdEntry.id_person = (int) Double.parseDouble( cols[17] );
				
					HouseholdEntry.TAZ = (int) Double.parseDouble( cols[13] );
					
					HouseholdEntry.h_x = parseDouble(cols[8]);
					HouseholdEntry.h_y = parseDouble(cols[9]);
			//		HouseholdEntry.s_x = parseDouble(cols[8]);
				//HouseholdEntry.s_y = parseDouble(cols[9]);
					HouseholdEntry.d_x = parseDouble(cols[1]);
					HouseholdEntry.d_y = parseDouble(cols[2]);
					
					HouseholdEntry.starttime = (int) parseDouble( cols[23] );
					HouseholdEntry.endtime = (int) parseDouble( cols[20] );
					
					puri = cols[21];
					
					HouseholdEntry.tripmode = 3;
					if( puri.indexOf( "work") !=-1 ) {
						HouseholdEntry.trippurpose = 1;
					}else if( puri.indexOf( "school" )!=-1 ) {
						HouseholdEntry.trippurpose = 2;
					}else if( puri.indexOf("shop") !=-1) {
						HouseholdEntry.trippurpose = 3;
					}else if( puri.indexOf("recreation") !=-1) {
						HouseholdEntry.trippurpose = 4;
					}else if( puri.indexOf("other" ) !=-1){
						HouseholdEntry.trippurpose = 5;
					}else {
						System.out.println( "trip purpose has an error, which is "+cols[21] );
					}
				
					HouseholdEntry.tripdistance = Math.sqrt( Math.pow(HouseholdEntry.h_x - HouseholdEntry.d_x, 2) + Math.pow(HouseholdEntry.h_y - HouseholdEntry.d_y, 2));
					HouseholdEntry.tripduration =  (int) parseDouble(cols[18]);
					HouseholdEntry.id_tour = parseInteger(cols[0]);
					HouseholdEntry.tripmode = 3;
					entries.add(HouseholdEntry);
			}
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
