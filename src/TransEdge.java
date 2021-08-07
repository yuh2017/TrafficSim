
public class TransEdge {
	
	public int   EndID, BeginID, edgeID, LANES, Speed_limit, TWOWAY;
	public double  DISTANCE;
	public double  beginx , beginy, endx, endy;
	
	
	public TransEdge(int i) {
		this.edgeID = i;
	}
	
	public void setEdge(String[] dataline) {
		
		this.BeginID = (int) Double.parseDouble(dataline[0]);
		this.EndID = (int) Double.parseDouble(dataline[3]);
		
		this.beginx = Double.parseDouble(dataline[1]);
		this.beginy = Double.parseDouble(dataline[2]);
		this.endx = Double.parseDouble(dataline[4]);
		this.endy = Double.parseDouble(dataline[5]);
		
		this.DISTANCE = Double.parseDouble(dataline[6]);
		this.LANES = (int)Double.parseDouble(dataline[7]);
		
		this.edgeID = (int)Double.parseDouble(dataline[8]);
		this.Speed_limit = (int)Double.parseDouble(dataline[9]);
		
		if(this.Speed_limit < 10){
			this.Speed_limit = 15;
		}
		
		if(this.LANES < 1){
			this.LANES = 1;
		}
		
		this.TWOWAY = (int)Double.parseDouble(dataline[10]);
		
	}
}
