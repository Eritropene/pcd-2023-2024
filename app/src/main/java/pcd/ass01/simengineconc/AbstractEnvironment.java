package pcd.ass01.simengineconc;

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
	
	protected AbstractEnvironment(String id) {
		this.id = id;		
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * 
	 * Called at the beginning of the simulation
	 */
	public abstract void init();
	
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
	public void doAction(String agentId, Action act) {
		rwLock.readLock().lock();
		doActionTemplate(agentId, act);
		rwLock.readLock().unlock();
	}
	protected abstract void doActionTemplate(String agentId, Action act);
}
