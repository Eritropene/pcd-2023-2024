package pcd.ass01.simengineconc;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *   
 * Base class to define the environment of the simulation
 *   
 */
public abstract class AbstractEnvironment {

	private final String id;
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final ParallelList<Map.Entry<String, Action>> actions;
	private Random gen;
	
	protected AbstractEnvironment(String id) {
		this.id = id;
		this.actions = new ParallelList<>(AbstractConcurrentSimulation.numberOfThreads());
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * Called at the beginning of the simulation
	 */
	public void init(Random gen) {
		this.gen = gen;
	};
	
	/**
	 * 
	 * Called at each step of the simulation
	 * 
	 * @param dt
	 */
	public abstract void step(int dt);

	/**
	 * 
	 * Called by an agent to get its percepts 
	 * 
	 * @param agentId - identifier of the agent
	 * @return agent percept
	 */
	public Percept getCurrentPercepts(String agentId) {
		rwLock.readLock().lock();
		Percept value = getCurrentPerceptsTemplate(agentId);
		rwLock.readLock().unlock();
		return value;
	}
	protected abstract Percept getCurrentPerceptsTemplate(String agentId);

	/**
	 * 
	 * Called by agent to submit an action to the environment
	 * 
	 * @param agentId - identifier of the agent doing the action
	 * @param act - the action
	 */
	public void registerAction(String agentId, Action act) {
		rwLock.writeLock().lock();
		this.actions.add(Map.entry(agentId, act));
		rwLock.writeLock().unlock();
	}
	/**
	 *
	 * Called by the simulation to execute all the actions registered
	 */
	public void executeAllActions() {
		this.actions.forEach(e -> doActionTemplate(e.getKey(), e.getValue()));
		this.actions.clear();
	}
	protected abstract void doActionTemplate(String agentId, Action act);

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
