package tutorscheduler;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents a single schedule (a chromosome) as an ArrayList.  Each cell
 * is a time slot ranging from the first day of the week at the first time 
 * to the last day of the week at the last time.
 * 
 * @author Stephanie Deen
 */
public class Schedule implements Comparable<Schedule> {
    
    private final Session[] schedule;
    private int fitness;
    private final int size = TutorScheduler.HOURS_A_DAY*TutorScheduler.DAYS_A_WEEK;
    private final Data data;
    private final HashMap<Component, MutableInt> people = new HashMap<>(); // used for storing hours on schedule

    /**
     * Constructor
     * Creates a schedule by filling the slots with random or blank sessions, 
     * taking into account the maximum hours that can be scheduled for the week.  
     * Also initializes map used to store tutors and supervisors hours worked
     * per schedule and calculates the fitness. 
     * 
     * @param data 
     * @param onStartup true if this is the first round of schedules for the 
     * first population, false if creating schedules from the genetic algorithm
     * class
     */
    public Schedule(Data data, boolean onStartup) {
        this.data = data;
        schedule = new Session[TutorScheduler.HOURS_A_DAY*TutorScheduler.DAYS_A_WEEK]; 
        
        // if first population, assign a randomly generated session, or a blank session
        if (onStartup) {
            int count = 0; // used for keeping track of how many blank sessions to make

            for (int i=0; i<schedule.length; i++) {
                // decide whether to add dummy session or real session
                if (Math.random() > .2 && count < TutorScheduler.HOURS_A_WEEK) {
                    schedule[i] = new Session(data);
                    count++;
                } else {
                    schedule[i] = new Session(null, null, null);
                }
            }
        }
        
        // initialize map by iterating over tutor and supervisor list
        Iterator<Component> supers = data.supervisors.iterator();
        Iterator<Component> tutors = data.tutors.iterator();
        while (supers.hasNext()) 
            people.put(supers.next(), new MutableInt());
        while (tutors.hasNext()) 
            people.put(tutors.next(), new MutableInt());
        
        if (onStartup) calcFitness();
    }
         
    /**
     * Calculates the fitness of the schedule by calculating the percentage
     * of matches in availability and whether people are over their max hours. 
     */
    public void calcFitness() {
        
        Session session;
        Component tutor;
        Component supervisor;
        String name;
        String availability;
        int match = 0;  // keep track of how many matches there are in the schedule
        int sessions = 0; // keep track of how many non null sessions there are
        
        for (int i=0; i<schedule.length; i++) {
           session = schedule[i];
            if (session.getRoom() != null) { // if the session isn't dummy/null
                sessions++; 
                
                // update total hours in the hashmap
                // tutors
                tutor = session.getTutor();
                people.get(tutor).increment();
                // supervisors                
                supervisor = session.getSupervisor();
                people.get(supervisor).increment();
                
                
                // compare availability to schedule
                
                // tutor
                name = tutor.getName();                
                availability = data.tutorAvailability[i];
                if (availability != null && availability.contains(name))                     
                    match++;  // tutor found in avail, count success
                
                // supervisor
                name = supervisor.getName();                
                availability = data.supervisorAvailability[i];
                if (availability != null && availability.contains(name))                     
                    match++;  // supervisor found in avail, count success
                
                // room
                name = session.getRoom().getName();                
                availability = data.roomAvailability[i];
                if (availability != null && availability.contains(name))                     
                    match++;  // room found in avail, count success
            }
        }
        
        int score = ((100*match)/(sessions*3)); // score for availability matches
        int maxHoursScore = 100;
        if (sessions > TutorScheduler.HOURS_A_WEEK) 
            maxHoursScore = 0; // change maxHours if schedule goes over max hours allowed
        int hours = calcHours(); // check if people are over their max hours
        fitness = (score + hours + maxHoursScore)/3;
    }
    
    /**
     * Compares the number of hours on the schedule to each persons (tutor and
     * supervisor) max hours allowed to work. 
     * 
     * @return score that is used to calculate overall fitness
     */
    private int calcHours() {
        Component supervisor;
        Component tutor;
        int score = 0;
        int hours;
        int maxHours;
                
        // Iterate through supervisor and tutor list and check if hours on 
        // schedule is greater than max hours, if so, subtract from score
        
        // supervisors
        Iterator<Component> supers = data.supervisors.iterator();
        while (supers.hasNext()) {
           supervisor = supers.next();
           maxHours = supervisor.getMaxHours();
           hours = people.get(supervisor).getHours();
           if (hours > 0 && hours < maxHours) 
               score++; // hours are good, add to score
        }
        
        // tutors
        Iterator<Component> tutors = data.tutors.iterator();
        while (tutors.hasNext()) {
           tutor = tutors.next();
           maxHours = tutor.getMaxHours();
           hours = people.get(tutor).getHours(); 
           if (hours > 0 && hours < maxHours) 
               score++; // hours are good, add to score
        }
        
        // divide total number of matches of good hours and divide by total
        // number of people
        return ((100*score)/(data.supervisors.size()+data.tutors.size()));
    }
    
    public int getFitness() {
        return fitness;
    }

    /**
     * 
     * @return # of sessions
     */
    public int size() {
        return size;
    }
    
    /**
     * 
     * @param i index of session to return
     * @return session
     */
    public Session getSession(int i) {
        return schedule[i];
    }
    
    /**
     * 
     * @param s session to add to schedule
     * @param i index to add session at
     */
    public void setSession(Session s, int i) {
        schedule[i] = s;
    }

    /**
     * @return array of sessions for schedule
     */
    public Session[] getSchedule() {      
        return schedule;
    }
    
    /**
     * For sorting schedules in population by fitness.
     * @param schedule
     * @return 
     */
    @Override
    public int compareTo(Schedule schedule) {
        return this.getFitness() < schedule.getFitness() ? 1 : (this.getFitness() > schedule.getFitness() ) ? -1 : 0;
    }
        
    /**
     * Horribly atrocious table format of schedule.  Used primarily for testing
     * purposes. 
     * 
     * @return string representation of object
     */
    @Override
    public String toString() {
        String statement = "";
        //String[] days = {"M", "T", "W", "Th", "F", ""};
        String[] times = {"8-9","9-10","10-11","11-12","12-1","1-2","2-3","3-4","4-5"};
        
        statement += "\t| \t\t M            | \t\t T               | \t\t W            |\n";
        
        // print rows
        for (int i=0; i<times.length; i++) {
            statement += times[i] + "\t";
            
            for (int k=0; k<30; k=k+10) {
                if (schedule[k+i].printSession() == null) 
                    statement += "|\t\t\t      ";
                else
                    statement += "| " + schedule[k+i].printSession() + " ";
            }
            statement += "\n";
        }
        
        return statement;
    }
    
    /**
     * Converts schedule into HTML document.
     * @return string containing the full HTML document
     */
    public String convertToHTML() {
        String html = "<html><head><link rel=\"stylesheet\" href=\"style.css\"><title>Tutor Schedule</title></head><body>";
        String[] times = {"8-9","9-10","10-11","11-12","12-1","1-2","2-3","3-4","4-5","5-6"};
        String[] days = {"M", "T", "W", "Th", "F"};
        
        html += "<table><thead><th></th>";
        for (int i=0; i<TutorScheduler.DAYS_A_WEEK; i++) {
            html += "<th>" + days[i] + "</th>";
        }
        html += "</thead><tbody>";
        
        for (int i=0; i<times.length; i++) {
            html += "<tr>";
            html += "<td class='times'>" + times[i] + "</td>";
            
            for (int k=0; k<50; k=k+10) {
                if (schedule[k+i].printSession() == null) 
                    html += "<td></td>";
                else
                    html += "<td>" + schedule[k+i].printSession() + "</td>";
            }
            html += "</tr>";
        }
        
        html += "</tbody></table>";
        
        return html += "</body></html>";
    }
    
    /**
     * Private class made to hold and increment number of hours for each person 
     * on the schedule.
     */
    class MutableInt {
        int value = 0; 
        public void increment () { ++value; }
        public int  getHours ()  { return value; }
    }
}

