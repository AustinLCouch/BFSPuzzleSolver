package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a configuration of the clock.
 *
 * @author Hritik "Ricky" Gupta | rg4825@rit.edu
 */
public class ClockConfig implements Configuration {
    /** total number of hours on the clock */
    private int hours;
    /** current hour on the clock */
    private int current;
    /** goal hour on the clock */
    private int goal;

    /**
     * Construct the initial configuration of a clock.
     *
     * @param hours total number of hours on the clock
     * @param current current hour clock is at in this config
     * @param goal hour clock wants to get to
     */
    public ClockConfig(int hours, int current, int goal) {
        this.hours = hours;
        this.current = current;
        this.goal = goal;
    }

    /**
     * Copy constructor to get possible next configurations.
     *
     * @param other ClockConfig to copy data from
     */
    public ClockConfig(ClockConfig other, boolean addHour) {
        this.hours = other.hours;
        this.goal = other.goal;

        if (addHour) {
            this.current = (other.current + 1) % other.hours;
        } else {
            this.current = (other.current - 1) % other.hours;
        }

        if (this.current == 0) {
            this.current = this.hours;
        }
    }

    @Override
    public boolean isSolution() {
        return this.current == this.goal;
    }

    @Override
    public Collection<Configuration> getSuccessors() {
        ArrayList<Configuration> successors = new ArrayList<>();

        ClockConfig c1 = new ClockConfig(this, true);
        ClockConfig c2 = new ClockConfig(this, false);

        successors.add(c1);
        successors.add(c2);

        return successors;
    }

    @Override
    public String display() {
        return Integer.toString(this.current);
    }

    @Override
    public Configuration getGoalConfig() {
        return new ClockConfig(this.hours, this.goal, this.goal);
    }

    @Override
    public boolean equals(Object o) {
       boolean result = false;
       if (o instanceof ClockConfig) {
           ClockConfig c = (ClockConfig) o;
           result = this.hours == c.hours &&
                   this.current == c.current &&
                   this.goal == c.goal;
       }
       return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, current, goal);
    }

    @Override
    public String toString() {
        return "ClockConfig{" +
                "hours=" + hours +
                ", current=" + current +
                ", goal=" + goal +
                '}';
    }
}
