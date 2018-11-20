package tutorscheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Stephanie Deen
 */
public class TutorScheduler {
    
    public static int POPULATION_SIZE = 20;
    public static int GENERATIONS = 20;
    public static int HOURS_A_DAY = 10;
    public static int HOURS_A_WEEK = 36;
    public static int DAYS_A_WEEK = 5;

    public static void main(String[] args) {
        Data data = new Data();  // initialize app
        GeneticAlgorithm ga = new GeneticAlgorithm(data);
        Population population;
        
        // create initial population
        population = new Population(data);
        
        for (int i=0; i<GENERATIONS; i++) {
            System.out.println("Generation " + i + ", Fittest " + population.getFittest().getFitness());
            population = ga.evolve(population);
        }
        
        System.out.println("Generation " + GENERATIONS + ", Fittest " + population.getFittest().getFitness());
        printHTML(population.getFittest().convertToHTML());
    }
    
    /**
     * Converts an HTML string into a HTML file.
     * @param html 
     */
    public static void printHTML(String html) {
        String filename;
        String path = "src/Schedules/";
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Type name of file (don't include extension):");
        filename = keyboard.nextLine();
        
        File file = new File(path+filename+".html");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(html);
            bw.close();
        } catch (IOException e) {
            System.out.println("Error: Try another filename");            
        }
        
    }
        
}
