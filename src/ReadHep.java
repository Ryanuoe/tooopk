import java.io.*;
import java.util.ArrayList;
import util.*;

public class ReadHep {

    int numOfNodes = 15233;
 
    //Adjacent lists邻接表
    ArrayList<Node> nodeList = new ArrayList<Node>();

	 
	public void run(){ 
	    try  
	    {  
	        FileInputStream fstream = new FileInputStream("data/hep.txt");  
	       
	        DataInputStream in = new DataInputStream(fstream);  
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
	        String strLine;

        	
	        while ((strLine = br.readLine()) != null)  
		    {
	        
	        	String[] str = strLine.split(" ");
	        	int[] values = new int[str.length];
	        	for(int i=0;i<str.length;i++){
	        		values[i] = Integer.valueOf(str[i]);
					//System.out.println(values[i]);
	        	}

	        	
	        	//first line show how many nodes there are
	            if(values[0]==15233&&values[1]==58891){
	        		//create the list
	        		for(int i=0; i<values[0]; i++){
	        			Node node = new Node();
	        			node.setNodeID(i);
	        			nodeList.add(node);
	        		}
	        		continue;
	        	}

	        	
	        	
	        	if(values[0]>=0&&values[0]<=numOfNodes&&values[1]>=0&&values[1]<=numOfNodes){
	        		Neighbor neighbor = new Neighbor();
	        		neighbor.setNodeId(values[1]);
	        		boolean isNewPair = true;
	        		for(Neighbor e: nodeList.get(values[0]).getNeighborList()){
	        			if(neighbor.equals(e)){
	        				isNewPair = false;
	        			}
	        		}
	        		if(isNewPair){
	        			neighbor.setWeight(trivalencyModel());
	        			nodeList.get(values[0]).getNeighborList().add(neighbor);
	        			Neighbor neighbor1 = new Neighbor();
	        			neighbor1.setNodeId(values[0]);
	        			neighbor1.setWeight(trivalencyModel());
	        			nodeList.get(values[1]).getNeighborList().add(neighbor1);
	        		}
	        	}
		    }
	        
	        
	        in.close();  
	    }  
	    catch(Exception e)  
	    {  
	        System.err.println("Error: " + e.getMessage());  
	    }  
	  }

	
	//return a random value from 0.1, 0.01, 0.001.
	public double trivalencyModel(){
		
		//TRIVALENCY model 2
		int p = (int)(Math.random()*3);
		double[] choice = {0.2,0.04,0.008};
		return choice[p];

	}
	
	
	/**
	 * @return the numOfNodes
	 */
	public int getNumOfNodes() {
		return numOfNodes;
	}



	/**
	 * @param numOfNodes the numOfNodes to set
	 */
	public void setNumOfNodes(int numOfNodes) {
		this.numOfNodes = numOfNodes;
	}





	/**
	 * @return the nodeList
	 */
	public ArrayList<Node> getNodeList() {
		return nodeList;
	}



	/**
	 * @param nodeList the nodeList to set
	 */
	public void setNodeList(ArrayList<Node> nodeList) {
		this.nodeList = nodeList;
	}



	public static void main(String args[]){

		ReadHep readHepData = new ReadHep();
		readHepData.run();
		ArrayList<Node> list = readHepData.getNodeList();
		for(Node n: list){
			System.out.print(n.getNodeID() + " neighbors: ");
			for(Neighbor e: n.getNeighborList()){
				System.out.print(e.getNodeId() + " " + e.getWeight() + " ");
			}
			System.out.println();
		}
	}

}
