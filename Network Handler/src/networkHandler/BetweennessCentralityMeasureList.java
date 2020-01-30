package networkHandler;

import java.util.ArrayList;

public class BetweennessCentralityMeasureList implements GraphProperty<ArrayList<BetweennessCentralityMeasure>>{
	private	Graph									graph;
	private	ShortestPathList						shortestPathsList;
	private	ArrayList<BetweennessCentralityMeasure>	betweennessCentralityMeasureListValue;
	
	public											BetweennessCentralityMeasureList(Graph graph, ShortestPathList shortestPathList) {
		this.graph				= graph;
		this.shortestPathsList	= shortestPathList;
	}

	public	ArrayList<BetweennessCentralityMeasure>	getValue() {
		return this.betweennessCentralityMeasureListValue;
	}
	

	public	void									run() {
		ArrayList<Thread>	threads = new ArrayList<Thread>();
		for(int i = 0; i < this.graph.getNodeCount(); i++) {
			this.betweennessCentralityMeasureListValue.add(new BetweennessCentralityMeasure(this.graph, this.shortestPathsList,i));
			threads.add(new Thread(this.betweennessCentralityMeasureListValue.get(i), ("BCM Calculation Node " + i)));
			threads.get(i).start();
		}
	}
	

	public	void									printToConsole() {
		for(int i = 0; i < this.betweennessCentralityMeasureListValue.size(); i++) {
			this.betweennessCentralityMeasureListValue.get(i).printToConsole();
		}
	}
	
	public	void									printToConsole(int nodeId) {
		this.betweennessCentralityMeasureListValue.get(nodeId).printToConsole();
	}
}
