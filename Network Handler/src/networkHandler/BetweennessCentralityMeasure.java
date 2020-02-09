package networkHandler;

import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * <p>This class is responsible for calculating the Betweenness Centrality Measure of a {@link Node} in a {@link Graph} with help of a provided {@link ShortestPathList}.
 * <p>The calculation is performed by calling {@link #run()}, which is inherited from the {@link Runnable} interface, therefore the class is suitable to be run as a separate {@link Thread}.
 * <p>In case of a given {@link #nodeId} not existing in the given Graph, the calling of the {@link #printToConsole()} method will result in returning an error message informing about the wrong input,
 * since calculation will be aborted.
 * @author Fabian Grun
 * @see GraphProperty
 *
 */
public class BetweennessCentralityMeasure implements GraphProperty<Double>{
	
	private Graph graph;
	private ShortestPathList shortestPathList;
	private int nodeId;
	private Double betweennessCentralityMeasureValue;
	private boolean calculationIsSuccessfull;
	private final Logger mylog = LogManager.getLogger(BetweennessCentralityMeasure.class);
	
	public BetweennessCentralityMeasure(Graph graph, ShortestPathList shortestPathList, int nodeId) {
		this.graph = graph;
		this.shortestPathList = shortestPathList;
		this.betweennessCentralityMeasureValue = 0.0;
		this.nodeId = nodeId;
	}
	
	/**
	 * {@inheritDoc}
	 * @return Double
	 */
	public Double getValue() {
		return this.betweennessCentralityMeasureValue;
	}
	
	/**
	 * <p>{@inheritDoc}
	 * <p>The method first checks whether the given {@link #nodeId} exists in the given {@link Graph}.
	 * If the nodeId is not present, calculation is aborted and {@link #calculationIsSuccessfull} set to false.
	 * 
	 */
	public void run() {
		if(this.nodeId > -1 && this.nodeId < this.graph.getNodeCount()) {
			mylog.debug("Started calculation for BCM of Node n" + nodeId + ".");
			double[][] countsOfAllPaths = new double[this.graph.getNodeCount()][this.graph.getNodeCount()];
			double[][] countsOfPathsContainingNode = new double[this.graph.getNodeCount()][this.graph.getNodeCount()];
			
			Iterator<Path> pathIterator = this.shortestPathList.getValue().iterator();
			while(pathIterator.hasNext()) {
				Path nextPath = pathIterator.next();
				countsOfAllPaths[nextPath.getOriginNode()][nextPath.getDestinationNode()]++;
				if(nextPath.contains(nodeId)) {
					countsOfPathsContainingNode[nextPath.getOriginNode()][nextPath.getDestinationNode()]++;
				}
			}
			for(int i = 0; i < this.graph.getNodeCount(); i++) {
				if(i != nodeId) {
					for(int j = i + 1; j < this.graph.getNodeCount(); j++) {
						if(j != nodeId) {
							this.betweennessCentralityMeasureValue = this.betweennessCentralityMeasureValue + (countsOfPathsContainingNode[i][j] / countsOfAllPaths[i][j]);
						}
					}
				}
			}
			this.calculationIsSuccessfull = true;
			mylog.debug("Successfully calculated BCM of Node n" + nodeId + ".");
		}else {
			this.calculationIsSuccessfull = false;
			mylog.debug("Aborted calculation for BCM of Node n" + nodeId + ". Node does not exist in given Graph.");
		}
	}
	
	/**
	 * <p>{@inheritDoc}
	 * <p>If the calculation was aborted, a message (marked as an error) explaining the reason is printed instead.
	 */
	public void printToConsole() {
		if(this.calculationIsSuccessfull) {
			mylog.info("The Betweenness Centrality Measure for Node n" + this.nodeId + " is " + getValue().toString());
		}else {
			mylog.error("Calculation for Node n\" + this.nodeId +\" was not possible. Node does not exist in given graph.");
		}
	}
}
