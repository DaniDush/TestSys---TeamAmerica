package il.cshaifasweng.HSTS.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.HSTS.client.ocsf.AbstractClient;
import il.cshaifasweng.HSTS.entities.AddTimeRequest;
import il.cshaifasweng.HSTS.entities.Carrier;
import il.cshaifasweng.HSTS.entities.CarrierType;
import il.cshaifasweng.HSTS.entities.Exam;
import il.cshaifasweng.HSTS.entities.Examination;

import il.cshaifasweng.HSTS.entities.ExaminationStatus;

import il.cshaifasweng.HSTS.entities.ExaminationStudent;
import il.cshaifasweng.HSTS.entities.Question;
import il.cshaifasweng.HSTS.entities.Role;


public class SimpleClient extends AbstractClient  {
	
	private static SimpleClient client = null;
	private ArrayList<Carrier> expected = new ArrayList<Carrier>(3);
	private Carrier received;
	private int waitTime = 50;
	private boolean cancel = false;

	private SimpleClient(String host, int port) {
		super(host, port);
		cancel = false;
	}
	
	private synchronized void receiveCarrier(Object message){
		  if (expected.contains(message)) {
			  expected.clear();
			  received = (Carrier) message;
		  }
	}
	  
	@Override
	protected void handleMessageFromServer(Object message){
		if (!expected.isEmpty()) {
			receiveCarrier(message);
		}
		else {
			received = (Carrier) message;
			String msg = (String) received.carrierMessageMap.get("message");
			System.out.println(msg);
			if (msg.equals("principle answer for requests") && StudentMenuController.examination != null) 
			{	
				System.out.println("Updating student");
				int examId = (int) received.carrierMessageMap.get("exam");
				if(StudentMenuController.examination.getExamination_id() == examId) {
					StudentMenuController.examination.timeAddition((Duration) received.carrierMessageMap.get("duration"));					
				}
			}
		}
	}
	  
	public synchronized Object sendAndWaitForReply(Object message, Object expectedObject) throws Exception {
		expected.clear();
		expected.add((Carrier) expectedObject);	    
		return sendAndWaitForReply(message, null);
		}
	
	public synchronized Object sendAndWaitForReply(Object message, List expectedListOfObject) throws Exception {

	    if (expectedListOfObject!=null) {
	      expected.clear();
	      expected.addAll(expectedListOfObject);
	    }
	
	    this.sendToServer(message);
	
	    while (!expected.isEmpty() && !this.cancel)
	    {
	      wait(waitTime);
	    }
	    if (cancel) {
	    	cancel = true;
	    	System.out.println("Server disconnected, message did not recived. ");
	    }
	    
        return received;
  }

	protected void handleLogOut(int userId) {
		Carrier logoutCarrier =  new Carrier();
		logoutCarrier.carrierType = CarrierType.USER;
		logoutCarrier.carrierMessageMap.put("message", "Log me out");
		logoutCarrier.carrierMessageMap.put("ID", userId);
	
		try {
			this.sendToServer(logoutCarrier);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	  
	protected Carrier handleMessageFromLogInController(String first_name, String pass) {
		Carrier logInCarrier =  new Carrier();
		logInCarrier.carrierType = CarrierType.USER;
		logInCarrier.carrierMessageMap.put("message", "Log me in");
		logInCarrier.carrierMessageMap.put("userName", first_name);
		logInCarrier.carrierMessageMap.put("password", pass);

		try {
			logInCarrier = (Carrier) this.sendAndWaitForReply(logInCarrier, logInCarrier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(logInCarrier);
		System.out.println("Message recived from server. ");
		return logInCarrier;
	}
	
	protected Carrier handleMessageFromClientQuestionController(String message, int id, Question question) {
		Carrier questionCarrier =  new Carrier();
		questionCarrier.carrierType = CarrierType.QUESTION;	
		
		if (message.equals("get all questions")) 
		{
			questionCarrier.carrierMessageMap.put("message", "get all questions");
		}
		else if (message.equals("get all teacher questions"))
		{
			questionCarrier.carrierMessageMap.put("teacher", id);
			questionCarrier.carrierMessageMap.put("message", "get all teacher questions");
		}
		else if (message.equals("create question")) 
		{
			questionCarrier.carrierMessageMap.put("question", question);
			questionCarrier.carrierMessageMap.put("ID", id);
			questionCarrier.carrierMessageMap.put("message", "create question");
		}
		else if (message.equals("get all course questions"))
		{
			questionCarrier.carrierMessageMap.put("course", id);
			questionCarrier.carrierMessageMap.put("message", "get all course questions");
		}
		else if(message.equals("delete question"))
		{
			questionCarrier.carrierMessageMap.put("question", question);
			questionCarrier.carrierMessageMap.put("message", "delete question");
		}
		
		try {
			questionCarrier = (Carrier) this.sendAndWaitForReply(questionCarrier, questionCarrier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("recieved from server");
		return questionCarrier;

	}
	
	protected Carrier handleMessageFromClientExamController(String message, int id, Exam exam) {
		Carrier examCarrier =  new Carrier();
		examCarrier.carrierType = CarrierType.EXAM;	
		
		if (message.equals("get all exams")) 
		{
			examCarrier.carrierMessageMap.put("message", "get all exams");
		}
		else if (message.equals("get all teacher exams"))
		{
			examCarrier.carrierMessageMap.put("teacher", id);
			examCarrier.carrierMessageMap.put("message", "get all teacher exams");
		}
		else if (message.equals("create exam")) 
		{
			examCarrier.carrierMessageMap.put("exam", exam);
			examCarrier.carrierMessageMap.put("message", "create exam");
		}
		else if (message.equals("get all course exams"))
		{
			examCarrier.carrierMessageMap.put("course", id);
			examCarrier.carrierMessageMap.put("message", "get all course exams");
		}
		else if(message.equals("delete exam"))
		{
			examCarrier.carrierMessageMap.put("exam", exam);
			examCarrier.carrierMessageMap.put("message", "delete exam");
		}
		
		try {
			examCarrier = (Carrier) this.sendAndWaitForReply(examCarrier, examCarrier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return examCarrier;
	}
	
	protected Carrier handleMessageFromClientExaminationController(String message, int id, Examination examination) {
		Carrier examCarrier =  new Carrier();
		examCarrier.carrierType = CarrierType.EXAMINATION;	
		
		if (message.equals("create examination")) {
			examCarrier.carrierMessageMap.put("examination", examination);
			examCarrier.carrierMessageMap.put("message", "create examination");
		} else if (message.equals("get teacher examinations")) {
			examCarrier.carrierMessageMap.put("teacher", id);
			examCarrier.carrierMessageMap.put("message", "get teacher examinations");
		}
		
		try {
			examCarrier = (Carrier) this.sendAndWaitForReply(examCarrier, examCarrier);
			System.out.println(examCarrier.carrierMessageMap.get("examinations"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return examCarrier;
	}
	
	protected Carrier handleMessageFromClientTimeRequestController(String message, AddTimeRequest timeRequest) {
		Carrier timeRequestCarrier =  new Carrier();
		timeRequestCarrier.carrierType = CarrierType.TIME_REQUEST;	
		
		switch (message) {
		case "ask for time request":
			timeRequestCarrier.carrierMessageMap.put("request", timeRequest);
			timeRequestCarrier.carrierMessageMap.put("message", "ask for time request");
			try {
				this.sendToServer(timeRequestCarrier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return timeRequestCarrier;
			
		case "principle ask for requests":
			//timeRequestCarrier.carrierMessageMap.put("request", timeRequest);
			timeRequestCarrier.carrierMessageMap.put("message", "principle ask for requests");
			try {
				timeRequestCarrier = (Carrier) this.sendAndWaitForReply(timeRequestCarrier, timeRequestCarrier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return timeRequestCarrier;

		case "principle answer for requests":
			timeRequestCarrier.carrierMessageMap.put("request", timeRequest);
			timeRequestCarrier.carrierMessageMap.put("message", "principle answer for requests");
			try {
				this.sendToServer(timeRequestCarrier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return timeRequestCarrier;
		}
		return timeRequestCarrier;
	}
	
	protected Carrier handleMessageStudentExaminationsFromClientExamsController(String message, int teacherId,
			ExaminationStatus examinationStatus, int courseId, ExaminationStudent studentExamination) {
		Carrier examCarrier = new Carrier();
		examCarrier.carrierType = CarrierType.STUDENT_EXAMINATION;
		
		if (message.equals("get all course student examinations")) {
			examCarrier.carrierMessageMap.put("course", courseId);
			examCarrier.carrierMessageMap.put("teacher", teacherId);
			examCarrier.carrierMessageMap.put("status", examinationStatus);
			examCarrier.carrierMessageMap.put("message", "get all course student examinations");
		} else if (message.equals("get all teacher student examinations")) {
			examCarrier.carrierMessageMap.put("teacher", teacherId);
			examCarrier.carrierMessageMap.put("status", examinationStatus);
			examCarrier.carrierMessageMap.put("message", "get all teacher student examinations");
		} else if (message.equals("grade student examination")) {
			examCarrier.carrierMessageMap.put("student examination", studentExamination);
			examCarrier.carrierMessageMap.put("message", "grade student examination");
		} else if (message.equals("get final course student examinations")) {
			examCarrier.carrierMessageMap.put("courseId", courseId);
			examCarrier.carrierMessageMap.put("teacherId", teacherId);
			examCarrier.carrierMessageMap.put("message", "get final course student examinations");
		} else if (message.equals("get final course student examinations")) {
			examCarrier.carrierMessageMap.put("courseId", courseId);
			examCarrier.carrierMessageMap.put("teacherId", teacherId);
			examCarrier.carrierMessageMap.put("message", "get final course student examinations");
		} else if (message.equals("get all student examinations")) {
			examCarrier.carrierMessageMap.put("message", "get all student examinations");
		}
		
		try {
			examCarrier = (Carrier) this.sendAndWaitForReply(examCarrier, examCarrier);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return examCarrier;
	}
	protected Carrier handleMessageFromClientStudentController(String message, int id, Examination examination, Carrier carrier) {
    
		Carrier studentCarrier =  new Carrier();
		if (carrier != null) {	
			studentCarrier =  carrier;
		}
	
		studentCarrier.carrierType = CarrierType.EXAMINATION;	
		
		
		switch(message) {
				
		case "get course examinations":
			System.out.println("get course examinations");
			studentCarrier.carrierMessageMap.put("message", message);
			studentCarrier.carrierMessageMap.put("course", id);		// AMIT changed "ID" to "course"
			
			try {
				studentCarrier = (Carrier) this.sendAndWaitForReply(studentCarrier, studentCarrier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			break;
			
		case "start student examination":	
			System.out.println("client - start student examination");	
			studentCarrier.carrierMessageMap.put("message", message);
			studentCarrier.carrierMessageMap.put("examinationId", examination.getExamination_id());		
			studentCarrier.carrierMessageMap.put("studentId", id);
			
			try {
				studentCarrier = (Carrier) this.sendAndWaitForReply(studentCarrier, studentCarrier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		
		case "submit student examination":
			System.out.println("submit student examination");
			studentCarrier.carrierMessageMap.put("message", message);
			studentCarrier.carrierMessageMap.put("examinationId", examination.getExamination_id());		
			studentCarrier.carrierMessageMap.put("studentId", id);
			
			try {
				this.sendToServer(studentCarrier);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
					
		}
		
		return studentCarrier;
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3002);
		}
		return client;
	}
	
	@Override
	protected void connectionClosed() {
		// TODO Auto-generated method stub	
		System.out.println("Disconnected from server. ");
		cancel = true;
		super.connectionClosed();
	}

}