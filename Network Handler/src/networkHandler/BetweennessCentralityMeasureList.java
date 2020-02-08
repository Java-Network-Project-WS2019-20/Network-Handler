package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class BetweennessCentralityMeasureList implements GraphProperty<ArrayList<BetweennessCentralityMeasure>>{
	private	Graph									graph;
	private	ShortestPathList						shortestPathsList;
	private	ArrayList<BetweennessCentralityMeasure>	betweennessCentralityMeasureListValue;
	private	BetweennessCentralityMeasure			singleBetweennessCentralityMeasureValue;
	private	boolean									calculateAll;
	private	boolean									calculateSingle;
	private	int										singleCalculationNodeId;
	private final Logger mylog = LogManager.getLogger(BetweennessCentralityMeasureList.class);


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

	public	void									run() {
		
		if (this.calculateAll) {
			this.betweennessCentralityMeasureListValue = new ArrayList<BetweennessCentralityMeasure>();
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

					mylog.error("Something went wrong: " +e.getMessage());
//					e.printStackTrace();
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
				mylog.error("Something went wrong: " +e.getMessage());

//				e.printStackTrace();
			}
		}
		
	}
	

	public	void									printToConsole() {
		for(int i = 0; i < this.betweennessCentralityMeasureListValue.size(); i++) {
			this.betweennessCentralityMeasureListValue.get(i).printToConsole();
		}
	}
	
	public	void									printToConsoleSingle() {
		this.singleBetweennessCentralityMeasureValue.printToConsole();
	}
	
	
}
