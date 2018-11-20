package tutorscheduler;

import java.util.Random;

/**
 * Each time slot in a schedule has a session in it.  
 * 
 * @author Stephanie Deen
 */
class Session {
    
    private Component room;
    private Component tutor;
    private Component supervisor;
    
    Random rand = new Random();

    /**
     * Constructor for creating a session built of random components.
     * @param data 
     */
    public Session(Data data) {
        // randomly select a room, tutor, supervisor
        int random = rand.nextInt(data.rooms.size());
        room = data.rooms.get(random);
        
        random = rand.nextInt(data.tutors.size());                
        tutor = data.tutors.get(random);
        
        random = rand.nextInt(data.supervisors.size());
        supervisor = data.supervisors.get(random);                
    }

    /**
     * Constructor primarily used for creating dummy (null) sessions. 
     * 
     * @param room
     * @param tutor
     * @param supervisor 
     */
    public Session(Component room, Component tutor, Component supervisor) {
        this.room = room;
        this.tutor = tutor;
        this.supervisor = supervisor;
    }

    public Component getRoom() {
        return room;
    }

    public void setRoom(Component room) {
        this.room = room;
    }

    public Component getTutor() {
        return tutor;
    }

    public void setTutor(Component tutor) {
        this.tutor = tutor;
    }

    public Component getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Component supervisor) {
        this.supervisor = supervisor;
    }
    
    /**
     * Used for printing the session inside of a schedule.
     * 
     * @return string of session
     */
    public String printSession() {
        if (this.room == null) { return null; }
        return room.getName() + " " + tutor.getName() + " " + supervisor.getName();
    }

    /**
     * Prints out the session in a generic context.
     * @return string of session
     */
    @Override
    public String toString() {
        if (this.room == null) { return null; }
        return "Session{" + 
                "room= " + room.getName() + 
                ", tutor= " + tutor.getName() + 
                ", supervisor= " + supervisor.getName() + '}';
    }
    
    

}
