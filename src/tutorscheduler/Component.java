/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tutorscheduler;

/**
 * Represents a tutor, supervisor, or a room.
 * 
 * @author Stephanie Deen
 */
public class Component {
    
    private String name;
    private int maxHours;
    private int status;
    
    public static int SUPERVISOR = 0;
    public static int TUTOR = 1;
    public static int ROOM = 2;
    
    public Component(String name, int status) {
        this.name = name;
        this.status = status;
    }

    public Component(String name, int status, int maxHours) {
        this.name = name;
        this.maxHours = maxHours;
        this.status = status;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        String statement;
        statement = this.name;
        if (this.status == SUPERVISOR) { 
            statement += " Max Hours: " + this.maxHours;
            statement += " Status: Supervisor"; }
        else if (this.status == TUTOR) {
            statement += " Max Hours: " + this.maxHours;
            statement += " Status: Tutor"; 
        } else { 
            statement += " Status: Room"; 
        }
        
        return statement;
    }    
     
}
