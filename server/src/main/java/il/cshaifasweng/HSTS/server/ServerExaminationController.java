package il.cshaifasweng.HSTS.server;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import org.hibernate.Hibernate;
import org.hibernate.Session;

import il.cshaifasweng.HSTS.entities.Course;
import il.cshaifasweng.HSTS.entities.Examination;
import il.cshaifasweng.HSTS.entities.User;
import net.bytebuddy.dynamic.scaffold.MethodRegistry.Handler.ForAbstractMethod;

public class ServerExaminationController {			

	public static String commitExaminationToDB(Examination examination) {
		ServerExamsController.updateExam(examination.getExam());
		Session session = ConnectToDB.getNewSession();
		session.save(examination);
		Course course = session.get(Course.class, examination.getCourseId());	// Add to course
		course.addExamination(examination);
		User user = session.get(User.class, examination.getTeacherId());	// Add to course
		user.addInstigateExamination(examination);
		session.update(user);
		session.update(course);
		session.save(examination);
		ConnectToDB.closeOuterSession(session);
		return "Exam commited successfully. ";
	}
	
	public static Examination getExaminationById(int id) {	
		// Get exam by its id
		try {
			Examination exam = ConnectToDB.getById(Examination.class, id);
			return exam;	// Success
			
		} catch (Exception nullPointerException) {	// No Exam is found
			return null;
		}
	}
	
	public void calcExaminationStatistics(int exec_code) {
	}	
	
	public static Examination getByExecutionCode(int exec_code) {
		try {
			Examination exemination = (Examination) ConnectToDB.getByAttribute(Examination.class, "executionCode", exec_code);
			return exemination;
			
		} catch (Exception illegalArgumentException) {	// No examination match this attribute
			return null;
		}
	}	
	
	public static List<Examination> getExaminationsByAtrribute(String attribute, int value) {
		try {
			List<Examination> eList = ConnectToDB.getByAttribute(Examination.class, attribute, value);	// Getting by Teacher id
			return eList;
	    	
		} catch (Exception illegalArgumentException) {	// No examination match this attribute
			return null;
		}
	}
	
	// ExaminationStudent to grade by teacher
	public static Set<Examination> getExminationByTeacher(int teacherId) {
		Session tempSession = ConnectToDB.getNewSession();
		User user = tempSession.get(User.class, teacherId);
		Set<Examination> examinations = user.getExaminationInstigated(); 	// Getting examination by teacher
		Set<Examination> eSet = new HashSet<Examination>();
		for(Examination exmn: examinations) {
			if(LocalTime.now().isAfter(exmn.getExamStartTime()) && LocalTime.now().isBefore(exmn.getExamEndTime())) {
				eSet.add(exmn);
			}
		}
		ConnectToDB.closeOuterSession(tempSession);
		return eSet;
	}
	
	public static Set<Examination> getExaminationByCourse(int courseID){
		Session tempSession = ConnectToDB.getNewSession();
		Course course = tempSession.get(Course.class, courseID);
		Hibernate.initialize(course.getExaminationList());
		Set <Examination> examinations = course.getExaminationList();
		
		ConnectToDB.closeOuterSession(tempSession);
		return examinations;
	}
	
	// Delete exam from database using its id
	public static String deleteExamByEntity(Examination examination) {
		ConnectToDB.deleteByInstance(Examination.class, examination);
		return "Exam deleted successfully. ";
	}
	
}
