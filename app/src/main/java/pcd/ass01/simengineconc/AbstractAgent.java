package pcd.ass01.simengineconc;

import java.util.Random;

/**
 * 
 * Base  class for defining types of agents taking part to the simulation
 * 
 */
public abstract class AbstractAgent {
	
	private String myId;
	private AbstractEnvironment env;
	private Random gen;
	
	/**
	 * Each agent has an identifier
	 * 
	 * @param id
	 */
	protected AbstractAgent(String id) {
		this.myId = id;
	}
	
	/**
	 * This method is called at the beginning of the simulation
	 * 
	 * @param env
	 */
	public void init(AbstractEnvironment env, Random gen) {
		this.env = env;
		this.gen = gen;
	}
	
	/**
	 * This method is called at each step of the simulation
	 * 
	 * @param dt - logical time step
	 */
	abstract public void step(int dt);

	public String getId() {
		return myId;
	}
	
	protected AbstractEnvironment getEnv() {
		return this.env;
	}

	protected int randInt() {
		return this.gen.nextInt();
	}
	protected double randDouble() {
		return this.gen.nextDouble();
	}
	protected boolean randBool() {
		return this.gen.nextBoolean();
	}
}
