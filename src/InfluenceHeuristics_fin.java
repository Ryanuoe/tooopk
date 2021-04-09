import util.Neighbor;
import util.Node;

import java.util.ArrayList;

public class InfluenceHeuristics_fin {

	 ArrayList<Node> nodeList;
	 int k;
	 int[] seeds;
     int[] realseeds;
	 //initialize
	 public InfluenceHeuristics_fin(int sizeOfSeedSet) {
		super();
		//read hep
		//ReadHep readHepData = new ReadHep();
		//readHepData.run();
		//this.nodeList = readHepData.getNodeList();
		
		//read phy
//		ReadPhy readPhyData = new ReadPhy();
//		readPhyData.run();
//		this.nodeList = readPhyData.getNodeList();
		
		//read dblp
		ReadDblp readDblpData = new ReadDblp();
		readDblpData.run();
		this.nodeList = readDblpData.getNodeList();
		
		//read Epinions
//		ReadEpinions readEpinionsData = new ReadEpinions();
//		readEpinionsData.run();
//		this.nodeList = readEpinionsData.getNodeList();
		
		//read Amazon
		//ReadAmazon readAmazonData = new ReadAmazon();
		//readAmazonData.run();
		//this.nodeList = readAmazonData.getNodeList();
		
		k = sizeOfSeedSet;
		seeds = new int[2*k];
		realseeds=new int [k];
		for(int i=0; i<2*k; i++){
			seeds[i] = 0;
		}
		 for(int i=0; i<k; i++){
			 realseeds[i] = 0;
		 }
	}
	 
	 public void run(){
		 //MaxDegreeSelectSeed();
		 //RandomSelectSeed();
		 System.out.println("---------Process on initSelect-----------------------------");
		 DegreeDiscountIC();
		 System.out.println("---------Process on Greedy-----------------------------");
		 Greedy();
		 System.out.println("---------InfluenceSpread-----------------------------");
		 InfluenceSpread();
	 }


	 //Degree Discount IC
	 public void DegreeDiscountIC(){
		 for(Node n:nodeList){
			 n.setDegree(n.getNeighborList().size());
			 n.setActiveNeighbors(0);
		 }
		 
		 for(int i=0; i<2*k; i++){
			 int tempID = MaxCurrentDegree();
			 seeds[i] = tempID;
			 nodeList.get(tempID).setSeed(true);
			 nodeList.get(tempID).setActive(true);
			 System.out.println("Seed: " + i + " : " + tempID + " Degree: " + nodeList.get(tempID).getNeighborList().size() + " RealDegree: " +  nodeList.get(tempID).getDegree());
			 DegreeDiscountProcess(tempID);
			 clearActive();
		 }
	 }
	 
	 public int MaxCurrentDegree(){
		 int tempID = 0;
		 int tempDegree = 0;
		 for(Node n:nodeList){
			 if(!n.isSeed()&&n.getDegree() > tempDegree){
				 tempDegree = n.getDegree();
				 tempID = n.getNodeID();
			 }
		 }
		 return tempID;
	 }
	 
	 public void DegreeDiscountProcess(int nodeId){
		 for(Neighbor e: nodeList.get(nodeId).getNeighborList()){
			 Node n = nodeList.get(e.getNodeId());
			 if(!n.isSeed()){
				 n.setActiveNeighbors(n.getActiveNeighbors()+1);
				 n.setDegree((int)(n.getNeighborList().size()
						 - 2 * n.getActiveNeighbors() -(n.getNeighborList().size()
						 -  n.getActiveNeighbors())* n.getActiveNeighbors()*n.getNeighborList().get(0).getWeight()));
			 }
		 }
	 }
	 
	 
	 // greedy
	 public void Greedy(){
	 	clearTempSeeds();
		 for(int i=0; i<k; i++){
			 int tempID = 0;
			 tempID = MaxIncreaseSpread(i);
			 realseeds[i] = tempID;
			 nodeList.get(tempID).setSeed(true);
			 System.out.println("Seed: " + i + " : " + tempID + " degree: " + nodeList.get(tempID).getNeighborList().size());
			 //Spread(tempID);
		 }
		 clearActive();
		 for(int i=0; i<k; i++){
			 nodeList.get(realseeds[i]).setSeed(true);
			 nodeList.get(realseeds[i]).setActive(true);
		 }
	 }
	 
	 
	 public int MaxIncreaseSpread(int number){
		 int tempID = 0;
		 int repetition = 10;
		 long spreadNum = 0;
		 //int [][] rd=new int [k][k];
		 for(int k=0;k<seeds.length;k++){
		 	 Node n=nodeList.get(seeds[k]);
		 	// System.out.println(n.getNodeID());
			 if(!n.isSeed()){
				 //System.out.println(n.getNodeID());
				 long temp = 0;
				 for(int i=0; i<repetition; i++){
					 clearTempActive();
					 temp += InfluenceSpreadTrial(n.getNodeID(), number);

				 }
				 temp = temp / repetition;
				 //System.out.println(temp);

				 if(temp > spreadNum){
					 tempID = n.getNodeID();
					 spreadNum = temp;
				 }
			 }
		 }
		 System.out.println("spreadNum: " + spreadNum);
		 System.out.println(tempID);
		 return tempID;
	 }
	 
	 public int InfluenceSpreadTrial(int nodeID, int num){
		 for(int i=0; i<num; i++){
			 SpreadTrial(seeds[i]);
		 }
		 SpreadTrial(nodeID);
		 
		 //Statistics
		 int activeNum = 0;
		 for(Node n: nodeList){
			 if(n.isTempActive()&&!n.isActive()){
				 activeNum++;
			 }
		 }
		 return activeNum;
	 }
	 
	 public void SpreadTrial(int nodeId){
		 for(Neighbor e: nodeList.get(nodeId).getNeighborList()){
			 double r = Math.random();
			 //if the neighbor is not active and r<e then activate it.
			 if(!nodeList.get(e.getNodeId()).isTempActive()&&!nodeList.get(e.getNodeId()).isActive()&&r<=e.getWeight()){
				 nodeList.get(e.getNodeId()).setTempActive(true);
				 SpreadTrial(e.getNodeId());
			 }
		 }
	 }
	 
	 public void clearActive(){
		 for(Node n: nodeList){
			 n.setActive(false);
		 }
	 }
	 
	 public void clearTempActive(){
		 for(Node n: nodeList){
			 n.setTempActive(false);
		 }
	 }
	public void clearTempSeeds(){
		for(Node n: nodeList){
			n.setSeed(false);
		}
	}
	 
	 public int InfluenceSpread(){
		 for(int i=0; i<k; i++){
			 //System.out.println(realseeds[i]);
			 Spread(realseeds[i]);
		 }
		 
		 //Statistics
		 int activeNum = 0;

		 for(Node n: nodeList){
			 if(n.isActive()){
				 activeNum++;
			 }
		 }
		// System.out.println("Spread: " + activeNum);
		 return activeNum;
	 }
	 
	 //DFS递归蒙特卡洛模拟
	 public void Spread(int nodeId){
		 for(Neighbor neighbor: nodeList.get(nodeId).getNeighborList()){
			 double random = Math.random();
			 //if the neighbor is not active and random < neighbor then activate it.
			 if(!nodeList.get(neighbor.getNodeId()).isActive() && random <= neighbor.getWeight())
             {
				 nodeList.get(neighbor.getNodeId()).setActive(true);
				 Spread(neighbor.getNodeId());
			 }
		 }
	 }

    //IC Model
	 public static void main(String args[])
     {
		 System.out.println("start running");
		 int spread = 0;
		 int reps = 10;//init reps 10
		 long  startTime = System.currentTimeMillis();
		 for(int i=0; i<reps; i++){
		 	System.out.println("process on "+i+" step");
		 	System.out.println("--------------------------------------");
		 	InfluenceHeuristics_fin g = new InfluenceHeuristics_fin(10);// init sizeOfSeedSet 30
		 	g.run();
			spread += g.InfluenceSpread();
		 } 
		 spread = spread/reps;
		 long endTime = System.currentTimeMillis();
		 System.out.println("--------------------------------------");
		 System.out.println("Influence Spread: " + spread);
		 System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
	}
}
