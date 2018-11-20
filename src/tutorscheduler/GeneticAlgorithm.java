package tutorscheduler;

import java.util.Random;

/**
 * Responsible for handling the evolution (crossover and mutations) of the 
 * populations.
 * 
 * @author Stephanie Deen
 */
public class GeneticAlgorithm {
    
    Random rand;
    Data data;
    private final int populationSize;
    public static double MUTATION_RATE = .1;
    public static double CROSSOVER_RATE = 0.3;

    /**
     * Constructor
     * @param data 
     */
    public GeneticAlgorithm(Data data) {
        this.populationSize = TutorScheduler.POPULATION_SIZE;
        rand = new Random();
        this.data = data;
    }
       
    /**
     * Evolves the current population.  The population is sorted by fitness, 
     * the first half is kept, while the second (less fitter) portion is mutated.
     * Crossover is then performed N (population size) times.  Crossover involves
     * selecting two individuals through tournament selection, filling a child
     * schedule with sessions from each parent schedule, and putting the child
     * in the new population.
     * 
     * @param population the population of schedules to evolve
     * @return new population of evolved individuals
     */
    public Population evolve(Population population) {
        Population newPopulation = new Population();
        population.sort(); // sort by fitness
        // add fittest individual to new population
        newPopulation.addSchedule(population.getSchedule(1));
        
        // mutate second half (less fit individuals) of population
        for (int i=populationSize/2; i<populationSize; i++) {
            mutate(population.getSchedule(i));
        }
        
        // crossover schedule N (population size) times, add child to new pop
        for (int i=1; i<populationSize; i++) {            
            Schedule s1 = tournamentSelection(population);
            Schedule s2 = tournamentSelection(population);
            newPopulation.addSchedule(crossover(s1, s2));            
        }
        
        // update fittest individual in population
        newPopulation.findFittest();
        
        return newPopulation;
    }
    
    /**
     * For each session in a schedule, a mutation is performed if the random
     * number is less than the mutation rate.  If a mutation is performed,
     * then there is a 50% chance that the new session will be a dummy session
     * and a 50% chance the new session will be a new randomly created session.
     * 
     * @param s schedule to mutate
     * @return mutated schedule
     */
    public Schedule mutate(Schedule s) {
        // iterate through sessions in schedule
        for (int i=0; i<s.size(); i++) {
            // if random number is less than mutation rate, reassign dummy or 
            // new random session to slot
            if (rand.nextDouble() < MUTATION_RATE) {
                if (rand.nextDouble() < .5) // mutate to dummy
                    s.setSession(new Session(null,null,null), i);
                else // mutate to new random session
                    s.setSession(new Session(data), i);
            }                
        }
        
        // update schedules fitness after mutation
       s.calcFitness();
       return s;
    }
    
    /**
     * For each session in a schedule, a random number determines whether a 
     * crossover event occurs.  A new blank schedule is created and for each
     * slot, a random number determines whether the slot will be filled with 
     * the fitter or the less fit of the two parents passed in.  If the random
     * number is greater than the crossover rate, than the fitter parent's 
     * slot is used, if not, the less fit parent's slot is used.
     * 
     * @param s1 one of the schedules to crossover
     * @param s2 another schedule to crossover
     */
    public Schedule crossover(Schedule s1, Schedule s2) {
        Schedule newSchedule = new Schedule(data, false); // new empty schedule
        Schedule fitter, lessFit;
        // find the fitter schedule
        if (s1.getFitness() < s2.getFitness()) {
            fitter = s2; lessFit = s1;
        } else {
            fitter = s1; lessFit = s2;
        }
        
        // iterate through the schedule, if random num is less than crossover 
        // rate, fill in lessfit session, else fill in fitter session
        for (int i=0; i<newSchedule.size(); i++) {
            if (rand.nextDouble() < CROSSOVER_RATE) {
                newSchedule.setSession(lessFit.getSession(i), i);
            } else {
                newSchedule.setSession(fitter.getSession(i), i);
            }
        }
        
        newSchedule.calcFitness();
        return newSchedule;
    }
    
    /**
     * Randomly selects two individuals from the population and returns the best
     * of the two.
     * 
     * @param population group to select individuals from
     * @return the schedule with the highest fitness from the ones that were 
     * selected
     */
    private Schedule tournamentSelection(Population population) {
        Schedule best = null;
        Schedule ind;
        for (int i=0; i<2; i++) {
            ind = population.getSchedule(rand.nextInt(populationSize));
            if (best == null || best.getFitness() < ind.getFitness()) 
                 best = ind;
        }
        return best;
    }   
}
