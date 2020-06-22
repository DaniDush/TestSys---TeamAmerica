package il.cshaifasweng.HSTS.server.utilities;

import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;

import org.hibernate.Session;

import il.cshaifasweng.HSTS.entities.*;


public class InitializeDB {
	
	public  Session session;
	
	public InitializeDB(Session session) throws Exception {
		this.session = session;
		initData();
	}
	
	
	public boolean isTableEmpty(String tableName) {
		return session.createQuery("select 1 from "+tableName).setFetchSize(1).list().isEmpty(); 
	}
	
	
	public void initData() throws NoSuchAlgorithmException  {
		
		// initialize users data	
					
		User principle = new User("Professor", "X", Hashing.hashing("wheels"), Role.PRINCIPLE);
		
		User teacher_1 = new User("Barak", "Obama", Hashing.hashing("plonter"), Role.TEACHER);
		User teacher_2 = new User("Rudy", "Giuliani", Hashing.hashing("theBigApple"), Role.TEACHER);
		User teacher_3 = new User("Linus", "Torvalds", Hashing.hashing("Linux"), Role.TEACHER);
		User student_1 = new User("Donald", "Trump", Hashing.hashing("duckface"), Role.STUDENT);
		User student_2 = new User("John", "Rockefeller", Hashing.hashing("stillRichest"), Role.STUDENT);
		User student_3 = new User("Mayer", "Amschel Rothschild", Hashing.hashing("conspiracy101"), Role.STUDENT); 
		User student_4 = new User("kyle", "broflovski", Hashing.hashing("ginGer"), Role.STUDENT);
		User student_5 = new User("Gregory", "House", Hashing.hashing("meningitis"), Role.STUDENT);
		User student_6 = new User("raymond", "reddington", Hashing.hashing("TheCabal"),Role.STUDENT);
		User student_7 = new User("Rick", "Sanchez", Hashing.hashing("MeeSeeks"),Role.STUDENT);
		User student_8 = new User("Abigail", "Lawnmower", Hashing.hashing("GrassIsGreen"),Role.STUDENT);
		User student_9 = new User("Sonny", "Mythroast", Hashing.hashing("chickenPie"),Role.STUDENT);
		
		if (isTableEmpty("User") == true) {
			session.save(teacher_1);
			session.save(teacher_2);
			session.save(teacher_3);
			session.save(principle);
			session.save(student_1);
			session.save(student_2);
			session.save(student_3);
			session.save(student_4);
			session.save(student_5);
			session.save(student_6);
			session.save(student_7);
			session.save(student_8);
			session.save(student_9);
			
			session.flush();
		}	
		
		// initialize courses data
		
		Subject subject_1 = new Subject("Core courses");
		Subject subject_2 = new Subject("Humanities courses");
		
		session.save(subject_1);
		session.save(subject_2);
		
		session.flush();
		
		// initialize courses data
		Course course_1 = new Course("introduction to CS", subject_1, teacher_3);
		Course course_2 = new Course("Algorithms", subject_1, teacher_1);
		Course course_3 = new Course("OOP", subject_2, teacher_3);
		Course course_4 = new Course("Data structures", subject_2, teacher_2);
		
		session.save(course_1);
		session.save(course_2);
		session.save(course_3);
		session.save(course_4);
		
		session.flush();
		
		// enroll students to courses - with: "public void addCoursesToStudent(Course... courses)"
		student_1.addCoursesToStudent(course_1,course_3);
		student_2.addCoursesToStudent(course_2,course_3);
		student_3.addCoursesToStudent(course_3,course_4);
		student_4.addCoursesToStudent(course_2);
		student_5.addCoursesToStudent(course_1,course_2,course_4);
		student_6.addCoursesToStudent(course_3);
		student_7.addCoursesToStudent(course_4,course_3);
		student_8.addCoursesToStudent(course_1,course_2,course_3,course_4);
		student_9.addCoursesToStudent(course_2,course_3);
		
		session.save(student_1);
		session.save(student_2);
		session.save(student_3);
		session.save(student_4);
		session.save(student_5);
		session.save(student_6);
		session.save(student_7);
		session.save(student_8);
		session.save(student_9);
		
		session.flush();
		
		
		// initialize questions data 
		
		String[] answers_1 = {"Minecraft","Call of duty","Worms","FIFA"};
		int correct_answer = 1;
		String question = "Which game did Rick and Morty play in the final episode of season 3?";
		String instructions = "Watch rick and mortey";
		Question question_1 = new Question(course_1.getCourseId(), question, answers_1, instructions, 
											correct_answer,teacher_1.getUserId());
		
		String[] answers_2 = {"Pickle","Portal gun","Morites shirt","He doesn't travel through the different universes"};
		correct_answer = 2;
		question = "Which item helps Rick travel through the different universes?";
		instructions = "Watch rick and mortey";
		Question question_2 = new Question(course_2.getCourseId(), question, answers_2, instructions,
											correct_answer,teacher_1.getUserId());
		
		String[] answers_3 = {"Green","Black","Yellow","Red"};
		correct_answer = 3;
		Question question_3 = new Question(course_3.getCourseId(), "What is the color of Morties shirt?", answers_3,
											"Watch rick and mortey", correct_answer,teacher_2.getUserId());
		
		String[] answers_4 = {"Gerry smit","Rick sanchez","Bird man","He don't have a father"};
		correct_answer = 1;
		Question question_4 = new Question(course_4.getCourseId(), "What is the name of Morties father?", answers_4,
											"Watch rick and mortey", correct_answer,teacher_2.getUserId());
		
		String[] answers_5 = {"Tomato","Onion","Pickle","Rice"};
		correct_answer = 3;
		Question question_5 = new Question(course_1.getCourseId(), "In Season 3, Episode 3 Rick turn himself into?", 
											answers_5, "Watch rick and mortey", correct_answer,teacher_3.getUserId());
		
		String[] answers_6 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_6 = new Question(course_2.getCourseId(), "What is Rick's \"universe number\"?", answers_6, 
											"Watch rick and mortey", correct_answer,teacher_3.getUserId());
		
		String[] answers_7 = {"Bill Gates","LinusTorvalds","Sammy Mitchel","Louis Rodrigez"};
		correct_answer = 2;
		Question question_7 = new Question(course_4.getCourseId(), "Who founded Github", answers_7, 
											"google it", correct_answer,teacher_2.getUserId());
		
		String[] answers_8 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_8 = new Question(course_2.getCourseId(), "What is Rick's \"universe number\"?", answers_8, 
											"Watch rick and mortey", correct_answer,teacher_1.getUserId());
		
		String[] answers_9 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_9 = new Question(course_2.getCourseId(), "What is Rick's \"universe number\"?", answers_9, 
											"Watch rick and mortey", correct_answer,teacher_1.getUserId());
		
		String[] answers_10 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_10 = new Question(course_3.getCourseId(), "What is Rick's \"universe number\"?", answers_10, 
											"Watch rick and mortey", correct_answer,teacher_3.getUserId());
		
		String[] answers_11 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_11 = new Question(course_4.getCourseId(), "What is Rick's \"universe number\"?", answers_11, 
											"Watch rick and mortey", correct_answer,teacher_2.getUserId());
		
		String[] answers_12 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_12 = new Question(course_1.getCourseId(), "What is Rick's \"universe number\"?", answers_12, 
											"Watch rick and mortey", correct_answer,teacher_2.getUserId());
		
		String[] answers_13 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_13 = new Question(course_1.getCourseId(), "What is Rick's \"universe number\"?", answers_13, 
											"Watch rick and mortey", correct_answer,teacher_3.getUserId());
		
		String[] answers_14 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_14 = new Question(course_3.getCourseId(), "What is Rick's \"universe number\"?", answers_14, 
											"Watch rick and mortey", correct_answer,teacher_3.getUserId());
		
		String[] answers_15 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_15 = new Question(course_3.getCourseId(), "What is Rick's \"universe number\"?", answers_15, 
											"Watch rick and mortey", correct_answer,teacher_3.getUserId());

		String[] answers_16 = {"T-999","N-97","C-137","D-142"};
		correct_answer = 3;
		Question question_16 = new Question(course_4.getCourseId(), "What is Rick's \"universe number\"?", answers_16, 
											"Watch rick and mortey", correct_answer,teacher_2.getUserId());
		
		session.save(question_1);
		session.save(question_2);
		session.save(question_3);
		session.save(question_4);
		session.save(question_5);
		session.save(question_6);
		session.save(question_7);
		session.save(question_8);
		session.save(question_9);
		session.save(question_10);
		session.save(question_11);
		session.save(question_12);
		session.save(question_13);
		session.save(question_14);
		session.save(question_15);
		session.save(question_16);
		
		
		question_1.setQuestionId();	
		question_2.setQuestionId();
		question_3.setQuestionId();
		question_4.setQuestionId();
		question_5.setQuestionId();
		question_6.setQuestionId();
		question_7.setQuestionId();
		question_8.setQuestionId();
		question_9.setQuestionId();
		question_10.setQuestionId();
		question_11.setQuestionId();
		question_12.setQuestionId();
		question_13.setQuestionId();
		question_14.setQuestionId();
		question_15.setQuestionId();
		question_16.setQuestionId();
		session.flush();
		
		// initialize exams
		
		Duration duration_1 = Duration.ofMinutes(90);
		Duration duration_2 = Duration.ofMinutes(120);
		
		Integer[] scoringList = {25,25,25,25};
		
		String studentInstructions = "do the test";
		String teacherInstructions = "this exam is not easy";
		
		Set<Question> questionList_1 = new HashSet<Question>();
		questionList_1.add(question_1);
		questionList_1.add(question_5);
		questionList_1.add(question_12);
		questionList_1.add(question_13);
		Exam exam_1 = new Exam(teacher_3.getUserId(), 1, questionList_1, scoringList, studentInstructions, teacherInstructions, duration_1);
		
		Set<Question> questionList_2 = new HashSet<Question>();
		questionList_2.add(question_2);
		questionList_2.add(question_6);
		questionList_2.add(question_8);
		questionList_2.add(question_9);
		Exam exam_2 = new Exam(teacher_1.getUserId(), 2, questionList_2, scoringList, studentInstructions, teacherInstructions, duration_1);
		
		Set<Question> questionList_3 = new HashSet<Question>();
		questionList_3.add(question_3);
		questionList_3.add(question_10);
		questionList_3.add(question_14);
		questionList_3.add(question_15);
		Exam exam_3 = new Exam(teacher_3.getUserId(), 3, questionList_3, scoringList, studentInstructions, teacherInstructions, duration_1);
		
		Set<Question> questionList_4 = new HashSet<Question>();
		questionList_4.add(question_4);
		questionList_4.add(question_7);
		questionList_4.add(question_11);
		questionList_4.add(question_16);
		Exam exam_4 = new Exam(teacher_2.getUserId(), 4, questionList_4, scoringList, studentInstructions, teacherInstructions, duration_1);
		
		session.save(exam_1);
		session.save(exam_2);
		session.save(exam_3);
		session.save(exam_4);
		exam_1.setExamId();
		exam_2.setExamId();
		exam_3.setExamId();
		exam_4.setExamId();
		
		session.flush();
		
		// initialize examination
//		public Examination(int execuationCode, int teacherId,ExamType examType, Duration duration, 
//		LocalDate examDate, LocalTime examStartTime, Exam exam)
		
		int execCode;
		int teacherId;
		ExamType examType;
		LocalDate examDate;
		LocalTime examStartTime;
		Exam exam;
		
		execCode = 1234;
		teacherId = exam_1.getTeacherId();
		examType = ExamType.COMPUTERIZED;
		examDate = LocalDate.of(2020, Month.JUNE, 23);
		examStartTime = LocalTime.of(8,20);
		Examination examintaion_1 = new Examination(execCode, teacherId, examType,  examDate, examStartTime, exam_1);
	
		execCode = 4517;
		teacherId = exam_2.getTeacherId();
		examType = ExamType.MANUAL;
		examDate = LocalDate.of(2020, Month.JUNE, 23);
		examStartTime = LocalTime.of(10,30);
		Examination examintaion_2 = new Examination(execCode, teacherId, examType, examDate, examStartTime, exam_2);
		
		session.save(examintaion_1);
		session.save(examintaion_2);
		session.flush();
		
		System.out.println(examintaion_1.getExamination_id());
	}
	
	
}
