package org.jikesrvm;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry
import org.jikesrvm.classloader.ClassLoader; // FILL ME IN
import org.jikesrvm.classloader.ClassFileReader;
import java.util.PriorityQueue;
/**
 * TODO Substitute a brief description of what this program or library does.
 */


class Haven {
    public Haven(){}
    public static void main(String args[]){
	 for (String s: args) {
	     parseArgs(s);
        }

    }
    public static void parseArgs(String arg){
	if (arg.equals("--version")) {
	    
	    version();
	}
        else if(arg.equals("START") || arg.equals("start")){

            }
        else if(arg.equals("STOP")){
        }
        else {
            printDoc();
        }
	
    }
    public static void version(){
	System.out.println("haven version 0.0.1 (http://github.com/zitterbewegung/Haven)  ");
    }
    public static void printDoc(){
	version();
	System.out.println("Usage: haven [--version] COMMAND [ARGS]");
	System.out.println("See README and /doc files for more information.");
	System.out.println("The most commonly used haven commands are as follows");
    }
}
