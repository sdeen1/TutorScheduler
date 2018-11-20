package tutorscheduler;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds all of the schedules (individuals) for each generation (a population).
 * @author Stephanie Deen
 */
public class Population {
    public final ArrayList<Schedule> population;
    private Schedule fittest; // schedule with highest fitness
    private int fittestScore; // the highest fitness score of the schedules

    /**
     * Population constructor takes data and creates an ArrayList of random 
     * schedules.
     * 
     * @param data (required) data from the text files
     */
    public Population(Data data) {
        population = new ArrayList<>();
        for (int i=0; i<TutorScheduler.POPULATION_SIZE; i++) {
            population.add(new Schedule(data, true));
        }
        findFittest();
    }

    /**
     * Constructor for creating a new empty population.
     */
    public Population() {
        population = new ArrayList<>();
    }
    
    /**
     * Looks through schedules to find the one with the highest fitness.
     * 
     * @return the schedule that has the highest fitness score
     */
    public Schedule findFittest() {
        Schedule schedule;
        int fitness;
        for (int i=0; i<population.size(); i++) {
            schedule = population.get(i);
            fitness = schedule.getFitness();
            if (fitness > this.fittestScore) { 
                this.fittest = schedule;
                this.fittestScore = fitness;
            }
        }        
        return fittest;
    }

    /**
     * Add a schedule to a new population.
     * @param s schedule to add
     */
    public void addSchedule(Schedule s) {
        population.add(s);
    }
    
    /**
     * 
     * @return schedule with highest fitness in population
     */
    public Schedule getFittest() {
        return fittest;
    }
    
    /**
     * 
     * @param i index of schedule to return in population
     * @return schedule at i index
     */
    public Schedule getSchedule(int i) {
        return population.get(i);
    }

    /**
     * 
     * @return number of schedules in population
     */
    public int size() {
        return population.size();
    }
    
    /**
     * 
     * @return the highest score for the schedules
     */
    public int fittestScore() {
        return fittestScore;
    }
          
    /**
     * Sort schedules by fitness. 
     */
    public void sort() {
        Collections.sort(population);
    }
}
