package tutorscheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads in text files for room availability, tutor availability, and room 
 * availability.
 * 
 * @author Stephanie Deen
 */
public class Data {
    
    String path = "src/Resources/";
    String[] textFiles = {path+"SupervisorData.txt", path+"TutorData.txt", path+"RoomData.txt"};
    
    ArrayList<Component> tutors = new ArrayList<>();
    ArrayList<Component> supervisors = new ArrayList<>();
    ArrayList<Component> rooms = new ArrayList<>();
    
    String[] tutorAvailability;
    String[] supervisorAvailability;
    String[] roomAvailability;

    public Data() 
    {       
        // give availability enough slots for every time of day, M-F          
        tutorAvailability = new String[TutorScheduler.HOURS_A_DAY*TutorScheduler.DAYS_A_WEEK]; 
        supervisorAvailability = new String[TutorScheduler.HOURS_A_DAY*TutorScheduler.DAYS_A_WEEK];
        roomAvailability = new String[TutorScheduler.HOURS_A_DAY*TutorScheduler.DAYS_A_WEEK];
        
        Scanner inFile;
        
        // for each file, fill ArrayList with info
        try {
            inFile = new Scanner(new File(textFiles[0]));
            while (inFile.hasNextLine()) {
                addSupervisor(inFile.nextLine());
            }
            
            inFile = new Scanner(new File(textFiles[1]));
            while (inFile.hasNextLine()) {
                addTutor(inFile.nextLine());
            }
            
            inFile = new Scanner(new File(textFiles[2]));
            while (inFile.hasNextLine()) {
                addRoom(inFile.nextLine());
            }
                
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    
    /**
     * Each line in text file collects information about the supervisor.  
     * Information is added to supervisor array list and the availability info
     * is added to the availability array.  Information about the availability
     * is currently stored as index for the availability array.
     * 
     * @param line line of the text file
     */
    private void addSupervisor(String line) {
        String[] supervisor = line.split(",");
        // [0] = Name, [1] = hours, [2] = M-F availability
        // add to list of supervisors
        int hours = Integer.valueOf(supervisor[1].trim());
        supervisors.add(new Component(
                supervisor[0], Component.SUPERVISOR, hours));
        
        // add supervisor availability by splitting indexes into an array
        String[] times = supervisor[2].trim().split(" ");
        for (int i=0; i<times.length; i++) {
            int index = Integer.valueOf(times[i]);
            
            // check if index in avail array already has a string in it
            if (supervisorAvailability[index] == null) {
                supervisorAvailability[index] = supervisor[0];
            } else {
                String name = supervisorAvailability[index];
                name += "," + supervisor[0];
                supervisorAvailability[index] = name;
            }
        }

    }
    
    /**
     * Add tutor to tutor arrayList and their availability to the availability
     * array. 
     * 
     * @param line each line of file
     */
    private void addTutor(String line) {
        String[] tutor = line.split(",");
        // [0] = Name, [1] = hours, [2] = M-F availability
        // add to list of tutors
        int hours = Integer.valueOf(tutor[1].trim());
        tutors.add(new Component(tutor[0], Component.TUTOR, hours));
        
        // add tutor availability by splitting indexes into an array
        String[] times = tutor[2].trim().split(" ");
        for (int i=0; i<times.length; i++) {
            int index = Integer.valueOf(times[i]);
            
            // check if index in avail array already has a string in it
            if (tutorAvailability[index] == null) {
                tutorAvailability[index] = tutor[0];
            } else {
                String name = tutorAvailability[index];
                name += "," + tutor[0];
                tutorAvailability[index] = name;
            }
        }
    }
    
    /**
     * Add a room from the text file to the rooms arrayList, and update the 
     * room availability array.
     * 
     * @param line a line in the file
     */
    private void addRoom(String line) {
        String[] room = line.split(",");
        // [0] = Name, [1] = M-F availability
        // add to list of rooms
        rooms.add(new Component(
                room[0], Component.ROOM));
        
        // add room availability by splitting indexes into an array
        String[] times = room[1].trim().split(" ");
        for (int i=0; i<times.length; i++) {
            int index = Integer.valueOf(times[i]);
            
            // check if index in avail array already has a string in it
            // if not, add room in, if so, update to include room
            if (roomAvailability[index] == null) {
                roomAvailability[index] = room[0];
            } else {
                String name = roomAvailability[index];
                name += "," + room[0];
                roomAvailability[index] = name;
            }
        }
    }
    
}

