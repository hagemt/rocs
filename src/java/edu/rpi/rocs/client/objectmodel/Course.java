package edu.rpi.rocs.client.objectmodel;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Describes a course object to the GWT client.
 *
 * @author elsajg
 * @author ewpatton
 *
 */
public class Course extends MajorMinorRevisionObject implements Comparable<Course> {

	public static class CourseComparator implements Comparator<Course> {
		public final int compare(Course a, Course b) {
			int result;
			if(a==null&&b==null) return 0;
			if(b==null) return 1;
			if(a==null) return -1;
			result = a.getDept().compareTo(b.getDept());
			if(result!=0) return result;
			return a.getNum()-b.getNum();
		}
	}

    /**
	 * UID for Serializable interface
	 */
	private static final long serialVersionUID = -4156341852068456290L;

	/** Protected data set by @see edu.rpi.rocs.server.objectmodel.CourseImpl */
    protected String name;
    protected String dept;
    protected int num;
    protected int credmin;
    protected int credmax;
    protected String gradetype;
    protected ArrayList<String> notes;
    protected ArrayList<Section> sections;

    /**
     * Empty constructor needed by interface Serializable
     */
    public Course() {
    	name = "";
    	dept = "";
    	num = 0;
    	credmin = 0;
    	credmax = 0;
    	gradetype= "";
    	notes = new ArrayList<String>();
    	sections = new ArrayList<Section>();
    }

    /**
     * Custom constructor to populate every field
     *
     * @param aName Written name of the course
     * @param aDept Department identifier
     * @param aNumber Course number
     * @param aCredMin Credit minimum
     * @param aCredMax Credit maximum
     * @param aGradeType Grade type used by this course
     * @param someNotes Any plain-text notes for end users
     */
    public Course(String aName, String aDept, int aNumber, int aCredMin, int aCredMax, String aGradeType, ArrayList<String> someNotes){
        name = aName;
        dept = aDept;
        num = aNumber;
        credmin = aCredMin;
        credmax = aCredMax;
        gradetype = aGradeType;
        notes = someNotes;
        if(someNotes == null)
        	notes = new ArrayList<String>();
        sections = new ArrayList<Section>();
    }

    /**
     * Gets the name of this course
     *
     * @return The course name
     */
    public String getName(){
        return name;
    }

    /**
     * Gets the department this course is offered in.
     *
     * @return The department ID
     */
    public String getDept(){
        return dept;
    }

    /**
     * Gets the course number for this course
     *
     * @return The course number
     */
    public int getNum(){
        return num;
    }

    /**
     * Gets the minimum number of credits this course can be
     *
     * @return Credit minimum
     */
    public int getCredmin(){
        return credmin;
    }

    /**
     * Gets the maximum number of credits this course can be
     *
     * @return Credit maximum
     */
    public int getCredmax(){
        return credmax;
    }

    /**
     * Gets the grade type of this course
     *
     * @return The grade type
     */
    public String getGradetype(){
        return gradetype;
    }

    /**
     * Gets any notes added to this course.
     *
     * @return The course notes
     */
    public ArrayList<String> getNotes() {
    	return new ArrayList<String>(notes);
    }

    /**
     * Gets a list of sections in this course
     *
     * @return Course sections
     */
    public List<Section> getSections() {
    	return new ArrayList<Section>(sections);
    }

	public int getLevel() {
		// TODO Auto-generated method stub
		return num / 1000;
	}

	   //accesssor functions
    public void setName(String newValue){
        name = newValue;
    }

    public void setDept(String newValue){
        dept = newValue;
    }

    public void setNum(int newValue){
        num = newValue;
    }

    public void setCredmin(int newValue){
        credmin = newValue;
    }

    public void setCredmax(int newValue){
        credmax = newValue;
    }

    public void setGradetype(String newValue){
        gradetype = newValue;
    }

    public void addNote(String newValue) {
    	notes.add(newValue);
    }

    public void removeNote(String note) {
    	notes.remove(note);
    }

    public void addSection(Section s) {
    	sections.add(s);
    }

    public void removeSection(Section s) {
    	sections.remove(s);
    }

    public boolean isClosed()
    {
    	for(Section s : sections) if(!s.isClosed()) return false;
    	return true;
    }

    public String getListDescription() {
    	String result="&nbsp;&nbsp;";
    	result += dept;
    	result += "&nbsp;&nbsp;";
    	result += num;
    	result += "&nbsp;&nbsp;";
    	result += name;
    	int size = 40 - name.length();
    	size = (size > 1 ? size : 1);
    	for(int i=0;i<size;i++) {
    		result += "&nbsp;";
    	}
    	result += getProfessors();
    	return result;
    }

	private String getProfessors() {
		HashSet<String> profs = new HashSet<String>();
		for(Section s : sections) {
			HashSet<String> subprofs = s.getProfessors();
			profs.addAll(subprofs);
		}
		profs.remove("** TBA **");
		if(profs.size()==0) {
			return "TBA";
		}
		profs.remove("Staff");
		if(profs.size()==0) {
			return "Staff";
		}
		boolean first=true;
		String result = "";
		for(String s : profs) {
			if(first) {
				result = s;
				first = false;
			}
			else {
				result += ", " + s;
			}
		}
		return result;
	}

	public int compareTo(Course o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private Long dbid;
	
	public Long getDbid() {
		return dbid;
	}
	
	public void setDbid(Long id) {
		dbid = id;
	}
	
	public void setSections(List<Section> list) {
		sections = new ArrayList<Section>(list);
	}

	public int getFilledSeats() {
		int count = 0;
		for(Section s : sections) {
			if(s.getCrossListing()!=null) {
				int cldiff = s.getCrossListing().getNumberOfSeats()-s.getCrossListing().getNumberOfStudents();
				int sdiff = s.getSeats()-s.getStudents();
				if(cldiff<sdiff) {
					count += s.getCrossListing().getNumberOfStudents();
				}
				else {
					count += s.getStudents();
				}
			}
			else {
				count += s.getStudents();
			}
		}
		return count;
	}

	public int getTotalSeats() {
		int count = 0;
		for(Section s : sections) {
			if(s.getCrossListing()!=null) {
				int cldiff = s.getCrossListing().getNumberOfSeats()-s.getCrossListing().getNumberOfStudents();
				int sdiff = s.getSeats()-s.getStudents();
				if(cldiff<sdiff) {
					count += s.getCrossListing().getNumberOfSeats();
				}
				else {
					count += s.getSeats();
				}
			}
			else {
				count += s.getSeats();
			}
		}
		return count;
	}

	public String getInstructorString() {
		return getProfessors();
	}
}
