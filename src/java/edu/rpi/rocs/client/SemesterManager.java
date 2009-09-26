package edu.rpi.rocs.client;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.rocs.client.objectmodel.Semester;
import edu.rpi.rocs.client.services.coursedb.CourseDBService;


/**
 * This class handles keeping semester data up to date on the
 * client side.
 * @author jon, ewpatton
 *
 */
public class SemesterManager {
	/**
	 * A set of callbacks which should be called when the Semester Manager completes an operation.
	 */
	Set<SemesterManagerCallback> callbacks = new HashSet<SemesterManagerCallback>();
	
	/**
	 * Adds a callback for when the Semester Manager loads a semester.
	 * 
	 * @param callback
	 */
	public void addSemesterChangeListener(SemesterManagerCallback callback) {
		callbacks.add(callback);
	}
	
	/**
	 * Removes a callback from the callback set.
	 * 
	 * @param callback
	 */
	public void removeSemesterChangeListener(SemesterManagerCallback callback) {
		callbacks.remove(callback);
	}
	
	/**
	 * An interface which describes a callback class for the Semester Manager.
	 * 
	 * @author ewpatton
	 *
	 */
	public interface SemesterManagerCallback {
		public void semesterLoaded(Semester semester);
	}
	
	/**
	 * The current instance of the SemesterManager.
	 */
	private static SemesterManager instance = null;
	
	/**
	 * The current semester loaded from the server.
	 */
	private Semester currentSemester = null;
	
	/**
	 * Accesses the singleton instance of the SemesterManager.
	 * 
	 * @return The current SemesterManager
	 */
	public static SemesterManager getInstance() {
		if (instance == null) {
			instance = new SemesterManager();
		}
		return instance;
	}
	
	private SemesterManager() {
	
	}
	
	/**
	 * Callback for when the client completes the loading of a semester from the server.
	 */
	private AsyncCallback<Semester> retrieveCallback = new AsyncCallback<Semester>(){

		public void onFailure(Throwable caught) {
			
		}

		/**
		 * Sets the local current semester to the object returned by the server and calls
		 * any callbacks registered for the change in semesters.
		 * @param result The semester which was loaded from the server.
		 */
		public void onSuccess(Semester result) {
			currentSemester = result;
			for(SemesterManagerCallback caller : callbacks) {
				caller.semesterLoaded(currentSemester);
			}
		}
		
	};
	
	/**
	 * Retrieves a course database given a particular semester ID.
	 * 
	 * @param semesterId Identifier for the semester to retrieve
	 */
	public void retrieveCourseDB(Integer semesterId) {
		CourseDBService.Singleton.getInstance().getSemesterData(semesterId, retrieveCallback);
	}
	
	/**
	 * Gets the currently loaded semester.
	 * 
	 * @return The loaded semester
	 */
	public Semester getCurrentSemester() {
		return currentSemester;
	}

}
