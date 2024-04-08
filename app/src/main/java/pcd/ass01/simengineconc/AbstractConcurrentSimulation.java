package pcd.ass01.simengineconc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for defining concrete simulations
 *  
 */
public abstract class AbstractConcurrentSimulation {

	private static int N_THREADS;
	/* environment of the simulation */
	private AbstractEnvironment env;

	private Random gen;
	
	/* list of the agents */
	private final ParallelList<AbstractAgent> agents;
	
	/* simulation listeners */
	private final List<SimulationListener> listeners;
	private final List<Runnable> onStartListeners;
	private final List<Runnable> onStopListeners;

	/* logical time step */
	private int dt;
	
	/* initial logical time */
	private int t0;

	/* in the case of sync with wall-time */
	private boolean toBeInSyncWithWallTime;
	private int nStepsPerSec;
	
	/* for time statistics*/
	private long currentWallTime;
	private long startWallTime;
	private long endWallTime;
	private long averageTimePerStep;

	/* Flag for stopping the simulation */
	private final AtomicBoolean flag = new AtomicBoolean();

	public AbstractConcurrentSimulation(int threads) {
		N_THREADS = threads;
		agents = new ParallelList<>(threads);
		listeners = new ArrayList<>();
		onStartListeners = new ArrayList<>();
		onStopListeners = new ArrayList<>();
		toBeInSyncWithWallTime = false;
		gen = new Random();
	}
	public AbstractConcurrentSimulation() {
		this(Runtime.getRuntime().availableProcessors());
	}

	public static int numberOfThreads() {
		return N_THREADS;
	}

	/**
	 * 
	 * Method used to configure the simulation, specifying env and agents
	 * 
	 */
	public abstract void setup();
	
	/**
	 * Method running the simulation for a number of steps,
	 * using a sequential approach
	 * 
	 * @param numSteps
	 */
	public void run(int numSteps) {		

		startWallTime = System.currentTimeMillis();

		/* initialize the env and the agents inside */
		int t = t0;

		env.init(new Random(gen.nextInt()));
		agents.forEach(a -> a.init(env, new Random(gen.nextInt())));

		this.notifyReset(t, agents, env);
		this.onStartListeners.forEach(Runnable::run);
		
		long timePerStep = 0;
		int nSteps = 0;

		setFlag(true);
		while (nSteps < numSteps && getFlag()) {

			currentWallTime = System.currentTimeMillis();
		
			/* make a step */
			
			env.step(dt);
			agents.parallelForEach(a -> a.step(dt));
			env.executeAllActions();
			t += dt;
			
			notifyNewStep(t, agents, env);

			nSteps++;			
			timePerStep += System.currentTimeMillis() - currentWallTime;
			
			if (toBeInSyncWithWallTime) {
				syncWithWallTime();
			}
		}
		setFlag(false);
		this.onStopListeners.forEach(Runnable::run);
		
		endWallTime = System.currentTimeMillis();
		this.averageTimePerStep = timePerStep / numSteps;
		
	}

	/**
	 * Setter for the flag
	 * @param value
	 */
	public void setFlag(boolean value) {
		this.flag.set(value);
	}
	public boolean getFlag() {
		return this.flag.get();
	}

	/**
	 * Set the seed of the simulation
	 * @param seed
	 */
	public void setSeed(long seed) {
		this.gen.setSeed(seed);
	}
	protected Random random() {
		return this.gen;
	}

	public long getSimulationDuration() {
		return endWallTime - startWallTime;
	}
	
	public long getAverageTimePerCycle() {
		return averageTimePerStep;
	}
	
	/* methods for configuring the simulation */
	
	protected void setupTimings(int t0, int dt) {
		this.dt = dt;
		this.t0 = t0;
	}
	
	public void syncWithTime(int nCyclesPerSec) {
		this.toBeInSyncWithWallTime = true;
		this.nStepsPerSec = nCyclesPerSec;
	}
	public void syncWithTime(int nCyclesPerSec, boolean active) {
		this.toBeInSyncWithWallTime = active;
		this.nStepsPerSec = nCyclesPerSec;
	}
		
	protected void setupEnvironment(AbstractEnvironment env) {
		this.env = env;
	}

	protected void addAgent(AbstractAgent agent) {
		agents.add(agent);
	}

	public void resetAgents() {
		this.agents.clear();
	}
	
	/* methods for listeners */
	
	public void addSimulationListener(SimulationListener l) {
		this.listeners.add(l);
	}

	public void resetSimulationListeners() {
		this.listeners.clear();
	}
	public void onStart(Runnable action) {
		this.onStartListeners.add(action);
	}
	public void onStop(Runnable action) {
		this.onStopListeners.add(action);
	}
	public void removeOnStart(Runnable action) {
		this.onStartListeners.remove(action);
	}
	public void removeOnStop(Runnable action) {
		this.onStopListeners.remove(action);
	}
	
	private void notifyReset(int t0, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyInit(t0, agents, env);
		}
	}

	private void notifyNewStep(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		for (var l: listeners) {
			l.notifyStepDone(t, agents, env);
		}
	}

	/* method to sync with wall time at a specified step rate */
	
	private void syncWithWallTime() {
		try {
			long newWallTime = System.currentTimeMillis();
			long delay = 1000 / this.nStepsPerSec;
			long wallTimeDT = newWallTime - currentWallTime;
			if (wallTimeDT < delay) {
				Thread.sleep(delay - wallTimeDT);
			}
		} catch (Exception ex) {}		
	}
}
