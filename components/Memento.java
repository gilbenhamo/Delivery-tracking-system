package components;
/**
 * Represents memento that keeps a state
 * @version 3.0, 14/6/2021
 * @author Itzik Rahamim - 312202351
 * @author Gil Ben Hamo - 315744557
 */
public class Memento {
	
	private State state;
	/**
	 * Create a new state
	 */
	public Memento()
	{
		state = new State();
	}
	/**
	 * @return state
	 */
	public State getState() {
		return state;
	}
}
