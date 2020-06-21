package il.cshaifasweng.HSTS.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

import il.cshaifasweng.HSTS.entities.*;
import il.cshaifasweng.HSTS.server.utilities.InitializeDB;


public class ConnectToDB {
	
	private static Session session;
	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
    
	private static SessionFactory getSessionFactory() throws HibernateException {
		Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
		
		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Exam.class);
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Examination.class);
		configuration.addAnnotatedClass(AddTimeRequest.class);
		configuration.addAnnotatedClass(ExaminationStudent.class);
		configuration.addAnnotatedClass(Subject.class);
		//configuration.addAnnotatedClass(AddTimeRequest.class);
		/* TODO - add Entities here: "configuration.addAnnotatedClass..." */
		
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(serviceRegistry);
	}
	
	// Function to get all instances of given class
	public static <T> List<T> getAll(Class<T> object) {
		Session temp_session = sessionFactory.openSession();
        temp_session.beginTransaction();

		CriteriaBuilder builder = temp_session.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(object);
		Root<T> rootEntry = criteriaQuery.from(object);
		CriteriaQuery<T> allCriteriaQuery = criteriaQuery.select(rootEntry);
		TypedQuery<T> allQuery = temp_session.createQuery(allCriteriaQuery);
		
    	temp_session.getTransaction().commit();
    	temp_session.close();
		return allQuery.getResultList();
	}
	
	// Function to delete object from database using its id
    public static <T> void deleteById(final Class<T> type,int entityId) {
    	// Get object
        T entity = ConnectToDB.getById(type, entityId);
        // Delete object
    	Session temp_session = sessionFactory.openSession();
        temp_session.beginTransaction();
    	temp_session.delete(entity);
    	temp_session.getTransaction().commit();
    	temp_session.close();
    }
    
	// Function to delete object from database directly
    public static <T> void deleteByInstance(final Class<T> type,T entity) {
        // Delete object
    	Session temp_session = sessionFactory.openSession();
        temp_session.beginTransaction();
    	temp_session.delete(entity);
    	temp_session.getTransaction().commit();
    	temp_session.close();
    }
	
	// Function to get object using its class and id
    public static <T> T getById(final Class<T> type, int id){
    	Session temp_session = sessionFactory.openSession();
        temp_session.beginTransaction();
		T entity =  temp_session.get(type, id);
    	temp_session.getTransaction().commit();
		temp_session.close();
		return entity;
    }
    
	// Function to insert object into the database
    public static <T> int save(T o){
        Session temp_session = ConnectToDB.sessionFactory.openSession();
        temp_session.beginTransaction();
    	int new_id = (Integer) temp_session.save(o);
    	if (o.getClass() == Question.class) {
    		((Question) o).setQuestionId();
    	}
    	else if (o.getClass() == Exam.class) 
    	{	
			((Exam) o).setExamId();
		}
    	temp_session.getTransaction().commit();
    	temp_session.close();
    	return new_id;
      }
    
	// Function to update existing object
    public static <T> void update(T o){
        Session temp_session = ConnectToDB.sessionFactory.openSession();
        temp_session.beginTransaction();
    	temp_session.merge(o);
    	temp_session.getTransaction().commit();
    	temp_session.close();
      }
    
    public static <T> List<T> getByAttribute(final Class<T> type, String key, int value)  {
        Session temp_session = ConnectToDB.sessionFactory.openSession();
        temp_session.beginTransaction();
        CriteriaBuilder cb = temp_session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(type);
        Root<T> root = cr.from(type);
        cr.select(root).where(cb.equal(root.get(key), value));  // Matching the key with its
        Query<T> query = temp_session.createQuery(cr);
        List<T> result = query.getResultList();
        temp_session.getTransaction().commit();
        temp_session.close();

        return result;
  }
    
    public static <T> List<T> getByAttribute(final Class<T> type, String key, String value)  {
        Session temp_session = ConnectToDB.sessionFactory.openSession();
        temp_session.beginTransaction();
        CriteriaBuilder cb = temp_session.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(type);
        Root<T> root = cr.from(type);
        cr.select(root).where(cb.equal(root.get(key), value));  //here you pass a class field, not a table column (in this example they are called the same)
        Query<T> query = temp_session.createQuery(cr);
        List<T> result = query.getResultList();
        temp_session.getTransaction().commit();
        temp_session.close();
        return result;
  }
    
    
	public static List<Course> extractCourses(User user){
	    Session temp_session = ConnectToDB.sessionFactory.openSession();
		List<Course> myCourses = new ArrayList<Course>(); 
        temp_session.beginTransaction();
        switch (user.getRole()) {
		case STUDENT: {
			myCourses = user.getCoursesStudying();
			break;
		}
		case TEACHER:{
			myCourses = user.getCoursesTeaching();
			break;
		}
		case PRINCIPLE:
			myCourses =  null;
			break;

		default:
			break;
		}
        temp_session.getTransaction().commit();
        temp_session.close();
        return myCourses;
  }

	
	public static void printUsers() throws Exception {
		
		System.out.format("Users list: \n\n");
		List<User> usersList = getAll(User.class);
		for (User user : usersList) {
			printUser(user);	
		}
		System.out.format("\n");
	}
	
	public static void printUser(User user) throws Exception {
		Role role = user.getRole();
		System.out.format(
				"User Details:  User Id: %-3s  First name: %-12s Surename: %-20s  Role: %-10s  Password: %-12s\n",
				user.getUserId(), user.getFirstName(), user.getLastname(),user.getRole(), user.getPassword());
		if (role == Role.STUDENT) {
			System.out.format("Courses studying - ");
			printCourses(user.getCoursesStudying());
		}
		if (role == Role.TEACHER) {
			System.out.format("Courses Teaching - ");
			printCourses(user.getCoursesTeaching());
		}
	}
	
	public static void printCourses(List<Course> coursesList) throws Exception {

		System.out.format("Course list:\n");
		for (Course course : coursesList) {
			printCourse(course);	
		}
		System.out.format("\n");
	}
	
	public static void printCourses() throws Exception {

		System.out.format("\nCourse list:\n\n");
		List<Course> coursesList = getAll(Course.class);
		for (Course course : coursesList) {
			printCourse(course);	
		}
		System.out.format("\n");
	}
	
	public static void printCourse(Course course) throws Exception {
		// course_name , subject_id,  student_list, theacher_id, 
		
		System.out.format("Course name:  %-25s", course.getCourseName());
		System.out.format("course ID  :  %-7s", course.getCourseId());
		System.out.format("Subject ID :  %-7s", course.getSubjectId());
		System.out.format("Teacher ID:   %-7s\n", course.getTeacherId());
	}
	
	public static void printQuestions(List<Question> questionsList) throws Exception {
		System.out.format("\nQuestion list:\n\n");
		for (Question question : questionsList) {
			printQuestion(question);			
		}
		System.out.format("\n");
	}
	
	public static void printQuestions() throws Exception {
		System.out.format("\nQuestion list:\n\n");
		List<Question> questionsList = getAll(Question.class);
		for (Question question : questionsList) {
			printQuestion(question);			
		}
		System.out.format("\n");
	}
	
	public static void printQuestion(Question question) throws Exception {
		System.out.format("Question ID:  %-8s",question.getQuestionId());
		System.out.format("Course ID  :  %-5s",question.getCourseId());
		System.out.format("Teacher ID  :  %-5s\n",question.getTeacherId());
		System.out.format("Instructions  :  %s\n",question.getInstructions());
		System.out.format("Question:  %-30s\n", question.getQuestion());
		String[] answers = question.getAnswers();
		System.out.format("answer1:  %-15s",answers[0]);
		System.out.format("answer2:  %-15s\n",answers[1]);
		System.out.format("answer3:  %-15s",answers[2]);
		System.out.format("answer4:  %-15s\n",answers[3]);
		System.out.format("correct answer:  %d\n\n",question.getCorrectAnswer());
	}

	public static void connectToDB() {
		
		try {

			ConnectToDB.sessionFactory = getSessionFactory();
			session = sessionFactory.openSession();
			session.beginTransaction();

			Carrier carry = new Carrier();
			
			InitializeDB initializeDataDb = new InitializeDB(session);
			session.getTransaction().commit();
			
//			printUsers();	
//			printCourses();
//			printQuestions();
			
		} catch (Exception exception) {
			if (session != null) {
				session.getTransaction().rollback();
			}
			System.err.println("An error occured, changes have been rolled back.");
			exception.printStackTrace();
		} finally {
			if (session != null) {
//				StandardServiceRegistryBuilder.destroy(serviceRegistry);
				
//				ConnectToDB.sessionFactory.close();
				session.close();
			}
		}
	}
}
