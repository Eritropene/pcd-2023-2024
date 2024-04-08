package pcd.ass01.mvcsim;

import pcd.ass01.simengineconc.AbstractConcurrentSimulation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RoadSimController {
    private Map<String, AbstractRoadSimulation> simulations = new HashMap<>();
    private Optional<AbstractRoadSimulation> selectedSimulation = Optional.empty();

    public void addSimulation(String name, AbstractRoadSimulation simulation) {
        simulations.put(name, simulation);
    }
    public void startSimulation(int steps, int cars, String environment, boolean display) {
        new Thread(() -> {
            selectedSimulation = Optional.of(simulations.get(environment));
            var simulation = selectedSimulation.get();

            // set cars
            simulation.setCars(cars);
            // initialize simulation
            simulation.setup();

            // add statistics
            RoadSimStatistics stat = new RoadSimStatistics();
            simulation.addSimulationListener(stat);
            // add visual display
            RoadSimView view = null;
            if (display) {
                view = new RoadSimView();
                view.display();
                simulation.addSimulationListener(view);
                simulation.syncWithTime(25);
            }

            // start simulation
            System.out.println("[Simulation] Starting...");
            simulation.run(steps);

            // simulation ended
            if (display) {
                view.dispose();
            }
            System.out.println("[Simulation] Done.");
        }).start();
    }
    public void stopSimulation() {
        new Thread(() -> selectedSimulation.ifPresent(s -> s.setFlag(false))).start();
    }
    public String[] getEnvironmentNames() {
        return simulations.keySet().toArray(new String[0]);
    }
    public boolean simulationIsRunning() {
        return selectedSimulation.isPresent() && selectedSimulation.get().getFlag();
    }
    public void onSimulationStart(Runnable action) {
        this.simulations.forEach((n, s) -> s.onStart(action));
    }
    public void onSimulationStop(Runnable action) {
        this.simulations.forEach((n, s) -> s.onStop(action));
    }

}
