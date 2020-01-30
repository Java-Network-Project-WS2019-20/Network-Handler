package networkHandler;

public interface GraphProperty<T> extends Runnable{
	//	Attribute
//	protected Graph	graph;
//	protected T		value;
	
	//	Constructor
//	public					GraphProperty(Graph graph) {
//		this.graph = graph;
//	}

	//	get
	public			T		getValue();
	
	//	calculation (abstract)
	public abstract void	run();
	
	//	print
	public	void			printToConsole();
}
