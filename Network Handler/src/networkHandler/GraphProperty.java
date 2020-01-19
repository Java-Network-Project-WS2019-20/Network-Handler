package networkHandler;

public abstract class GraphProperty<T>{
	//	Attribute
	protected Graph	graph;
	protected T		value;
	
	//	Constructor
	public					GraphProperty(Graph graph) {
		this.graph = graph;
	}
	
	//	set
	public			void	setValue(T value) {
		this.value = value;
	}
	
	//	get
	public			T		getValue() {
		return this.value;
	}
	
	//	calculation (abstract)
	public abstract void	calculate();
}
