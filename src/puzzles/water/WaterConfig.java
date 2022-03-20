package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a configuration of the water puzzle
 *
 * @author Hritik "Ricky" Gupta
 */
public class WaterConfig implements Configuration {
    /** total capacities of all the buckets */
    private ArrayList<Integer> totalCapacities;
    /** current capacities of all the buckets */
    private ArrayList<Integer> currentCapacities;
    /** target amount of water */
    private int goal;

    /**
     * Creates a config of a water puzzle
     *
     * @param totalCapacities ArrayList of integers, of the total bucket capacities
     * @param currentCapacities ArrayList of integers, of current bucket capacities
     * @param goal exact amount of water desired
     */
    public WaterConfig(ArrayList<Integer> totalCapacities, ArrayList<Integer> currentCapacities, int goal) {
        this.totalCapacities = totalCapacities;
        this.currentCapacities = currentCapacities;
        this.goal = goal;
    }

    /**
     * Copy constructor for WaterConfig.
     *
     * @param other WaterConfig to be copied
     */
    public WaterConfig(WaterConfig other) {
        this.totalCapacities = new ArrayList<>(other.totalCapacities);
        this.currentCapacities = new ArrayList<>(other.currentCapacities);
        this.goal = other.goal;
    }


    @Override
    public boolean isSolution() {
        for (int bucket : this.currentCapacities) {
            if (bucket == this.goal) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        ArrayList<Configuration> successors = new ArrayList<>();

        for (int i = 0; i < this.currentCapacities.size(); ++i) {
            int currentCapacity = this.currentCapacities.get(i);
            int maxCapacity = this.totalCapacities.get(i);
            int minCapacity = 0;

            WaterConfig filled = new WaterConfig(this);
            WaterConfig emptied = new WaterConfig(this);

            filled.currentCapacities.set(i, maxCapacity);
            emptied.currentCapacities.set(i, minCapacity);

            successors.add(filled);
            successors.add(emptied);

            for (int j = 0; j < this.currentCapacities.size(); ++j) {

                if (j == i) {
                    continue;
                }

                int newBucketCapacity = this.currentCapacities.get(j) + currentCapacity;
                int newCurrentCapacity = 0;

                if (newBucketCapacity > this.totalCapacities.get(j)) {
                    newBucketCapacity = this.totalCapacities.get(j);
                    newCurrentCapacity = currentCapacity - (this.totalCapacities.get(j) - this.currentCapacities.get(j));
                }

                if (newCurrentCapacity < 0) {
                    newCurrentCapacity = 0;
                }

                WaterConfig w = new WaterConfig(this);

                w.currentCapacities.set(i, newCurrentCapacity);
                w.currentCapacities.set(j, newBucketCapacity);

                successors.add(w);
            }
        }
        return successors;
    }

    @Override
    public String display() {
        return this.currentCapacities.toString();
    }

    @Override
    public Configuration getGoalConfig() {
        //since there are multiple solutions, sets goal initially to impossible value
        //Solver reassigns value if real goal config is found
        return new WaterConfig(new ArrayList<Integer>(), new ArrayList<Integer>(), 0);
    }

    @Override
    public boolean equals(Object o) {
        boolean result = false;
        if (o instanceof WaterConfig) {
            WaterConfig w = (WaterConfig) o;
            result = this.totalCapacities.equals(w.totalCapacities) &&
                    this.currentCapacities.equals(w.currentCapacities) &&
                    this.goal == w.goal;
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCapacities, currentCapacities, goal);
    }

    @Override
    public String toString() {
        return "WaterConfig{" +
                "totalCapacities=" + totalCapacities +
                ", currentCapacities=" + currentCapacities +
                ", goal=" + goal +
                '}';
    }
}
