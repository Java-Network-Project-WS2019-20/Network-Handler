package networkHandler;

import java.util.ArrayList;

public class BetweennessCentralityMeasureList implements GraphProperty<ArrayList<BetweennessCentralityMeasure>>{
	private	Graph									graph;
	private	ShortestPathList						shortestPathsList;
	private	ArrayList<BetweennessCentralityMeasure>	betweennessCentralityMeasureListValue;
	private	BetweennessCentralityMeasure			singleBetweennessCentralityMeasureValue;
	private	boolean									calculateAll;
	private	boolean									calculateSingle;
	private	int										singleCalculationNodeId;
	
	public											BetweennessCentralityMeasureList(Graph graph, ShortestPathList shortestPathList) {
		this.graph				= graph;
		this.shortestPathsList	= shortestPathList;
		this.calculateAll		= true;
		this.calculateSingle	= false;
	}

	public											BetweennessCentralityMeasureList(Graph graph, ShortestPathList shortestPathList, boolean calculateAll, int singleCalculationNodeId) {
		this.graph						= graph;
		this.shortestPathsList			= shortestPathList;
		this.calculateAll				= calculateAll;
		this.calculateSingle			= true;
		this.singleCalculationNodeId	= singleCalculationNodeId;
	}
	
	public	ArrayList<BetweennessCentralityMeasure>	getValue() {
		return this.betweennessCentralityMeasureListValue;
	}
	
	public	BetweennessCentralityMeasure			getSingleBetweennessCentralityMeasure() {
		return this.singleBetweennessCentralityMeasureValue;
	}
	
	public	BetweennessCentralityMeasure			getBetweennessCentralityMeasure(int nodeId) {
		return this.betweennessCentralityMeasureListValue.get(nodeId);
	}
	
	public	void									run() {
		
		if (this.calculateAll) {
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for (int i = 0; i < this.graph.getNodeCount(); i++) {
				this.betweennessCentralityMeasureListValue
						.add(new BetweennessCentralityMeasure(this.graph, this.shortestPathsList, i));
				threads.add(
						new Thread(this.betweennessCentralityMeasureListValue.get(i), ("BCM Calculation Node " + i)));
				threads.get(i).start();
			}
			for (int i = 0; i < this.graph.getNodeCount(); i++) {
				try {
					threads.get(i).join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(this.calculateSingle) {
				this.singleBetweennessCentralityMeasureValue = this.betweennessCentralityMeasureListValue.get(this.singleCalculationNodeId);
			}
		}else {
			this.singleBetweennessCentralityMeasureValue = new BetweennessCentralityMeasure(this.graph, this.shortestPathsList, this.singleCalculationNodeId);
			Thread thread = new Thread(this.singleBetweennessCentralityMeasureValue, ("BCM Calculation Node " + this.singleCalculationNodeId));
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

	public	void									printToConsole() {
		for(int i = 0; i < this.betweennessCentralityMeasureListValue.size(); i++) {
			this.betweennessCentralityMeasureListValue.get(i).printToConsole();
		}
	}
	
	public	void									printToConsoleSingle() {
		this.betweennessCentralityMeasureListValue.get(this.singleCalculationNodeId).printToConsole();
	}
	
	
}
