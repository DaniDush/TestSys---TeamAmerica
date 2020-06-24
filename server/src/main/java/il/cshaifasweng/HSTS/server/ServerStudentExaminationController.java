package il.cshaifasweng.HSTS.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import il.cshaifasweng.HSTS.entities.Carrier;
import il.cshaifasweng.HSTS.entities.Course;
import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.Examination;
import il.cshaifasweng.HSTS.entities.ExaminationStatus;
import il.cshaifasweng.HSTS.entities.ExaminationStudent;
import il.cshaifasweng.HSTS.entities.Question;
import il.cshaifasweng.HSTS.entities.User;

public class ServerStudentExaminationController {
	
	protected static ExaminationStudent commitToDB(Carrier carrier) {
			
		int studentId = (int) carrier.carrierMessageMap.get("studentId");
		int examinationId = (int) carrier.carrierMessageMap.get("examinationId");
		
		switch (ConnectToDB.checkIfSubmitted(studentId, examinationId)) {
		case "New": {	// New StudentExamination
			System.out.println("Not submitted yet");
			ExaminationStudent exmnStudent = ConnectToDB.saveExmnStudent(studentId, examinationId);
			return exmnStudent;
		}
		case "Submit": {	// Student tries to Submit
			// TODO add method to calculate grade and submit examination 
			return null;
		}
		case "Already submited": {	// Student already submitted exam
			return null;
		}
		default:
			return null;
		}	
	}
	
	// Function to calculate grade of student examination
	private static void calcGrades(ExaminationStudent studentExam) {
		int grade = 0;
		Exam exam = studentExam.getExamination().getExam();
		Set<Question> examQuestions = exam.getQuestionList();
		ArrayList<Question> questionList = new ArrayList<Question>(examQuestions);
		List<Integer> studentAnswers = studentExam.getStudentsAnswers();

	    for (int index = 0; index < examQuestions.size(); index++) {
	        if(questionList.get(index).getCorrectAnswer() == studentAnswers.get(index)) {
	        	grade += exam.getScoringList()[index];
	        }
	    }
	    studentExam.setGrade(grade);
	}
	
	public static List<ExaminationStudent> getExamsinationsByAtrribute(String attribute, int value) {
		try {
			List<ExaminationStudent> eList = ConnectToDB.getByAttribute(ExaminationStudent.class, attribute, value);	// Getting by Teacher id
			return eList;
	    	
		} catch (Exception illegalArgumentException) {	// No examination match this attrubute
			return null;
		}
	}
	
	// ExaminationStudent for teacher to view scores
	public static List<ExaminationStudent> getByTeacherExams(int teacherId) {
		Session session = ConnectToDB.getNewSession();
		User user = session.get(User.class, teacherId);
		List<ExaminationStudent> examinationStudents = new ArrayList<ExaminationStudent>();

		for (Course course: user.getCoursesTeaching()) {
			Set<Examination> examinations = course.getExaminationList(); 	// Getting examination by teacher
			for (Examination examination: examinations) {
				if(examination.getExam().getTeacherId() == teacherId) {
					for(ExaminationStudent examinationStudent: examination.getExamineesList()) {
						if(examinationStudent.getStatus() == ExaminationStatus.FINALIZED) {
							examinationStudents.add(examinationStudent);
						}
					}
				}
			}
		}
		
		ConnectToDB.closeOuterSession(session);
		return examinationStudents;
	}
	
	// ExaminationStudent to grade by teacher
	public static List<ExaminationStudent> getByTeacher(int teacherId, ExaminationStatus status) {
		Session session = ConnectToDB.getNewSession();
		User user = session.get(User.class, teacherId);
		Set<Examination> examinations = user.getExaminationInstigated(); 	// Getting examination by teacher
		List<ExaminationStudent> examinationStudents = new ArrayList<ExaminationStudent>();
		for (Examination examination: examinations) {
			for(ExaminationStudent examinationStudent: examination.getExamineesList()) {
				if(examinationStudent.getStatus() == status) {
					examinationStudents.add(examinationStudent);
				}
			}
		}
		ConnectToDB.closeOuterSession(session);
		return examinationStudents;
	}
	
	// ExaminationStudent to grade by teacher
	public static List<ExaminationStudent> getByCourse(int courseId, ExaminationStatus status) {
		Session session = ConnectToDB.getNewSession();
		Course course = session.get(Course.class, courseId);
		Set<Examination> examinations = course.getExaminationList(); 	// Getting examination by teacher
		List<ExaminationStudent> examinationStudents = new ArrayList<ExaminationStudent>();
		for (Examination examination: examinations) {
			for(ExaminationStudent examinationStudent: examination.getExamineesList()) {
				if(examinationStudent.getStatus() == status) {
					examinationStudents.add(examinationStudent);
				}
			}
		}
		ConnectToDB.closeOuterSession(session);
		return examinationStudents;
	}
	
}
