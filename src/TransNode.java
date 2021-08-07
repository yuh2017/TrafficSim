
public class TransNode {
	
	public int    NodeID ;
	public double    x , y;
	
	
	public TransNode() {
		
	}
	
	public void setCoords(String[] dataline) {
		this.NodeID = (int)Double.parseDouble(dataline[0]);
        this.x = Double.parseDouble(dataline[1] );
        this.y = Double.parseDouble(dataline[2]);
    }
}
