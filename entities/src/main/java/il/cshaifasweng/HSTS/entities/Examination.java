package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="examination_table")
public class Examination implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int examination_id;		//primary key
	
	// exam examination relation - Unidirectional
	@Column(name = "examination_key")
	private int examId;
	
	@Column
	private Duration duration;
	
	@Column
	private LocalDate examDate;
	
	@Column
	private LocalTime examStartTime;
	
	@Column
	private LocalTime examEndTime;
	
	@Column
	private AddTimeRequest addTimeRequest;
	
	@Column
	private ExamType examType;	// move to connecting table? 
	
	@Column
	private int studentsStarted;
	
	@Column
	private int studentsFinished;
	
	@Column
	private int studentsNotFinsished;
	
	@ManyToOne
	@JoinColumn(name = "exam_id")
	private Exam exam;

	// teacher exams relation - Unidirectional
	@Column(name = "teacher_id")
	private int teacherId;
	
	@OneToMany(mappedBy = "examination")
    private Set<ExaminationStudent> examineesList = new HashSet<ExaminationStudent>();
	
	public Examination(int teacherId,ExamType examType, Duration duration, int examId) {
		this.studentsStarted = 0;
		this.studentsFinished = 0;
		this.studentsNotFinsished = 0;
		this.examineesList = new HashSet<ExaminationStudent>(); 
		this.teacherId = teacherId;
		this.examType = examType;
		this.addTimeRequest = null; 
		this.examId = examId;
		setDuration(duration);
	}
	
	public Examination() {
		
	}
	
	// TODO - ask Liel if it's OK
	public void addStudent(ExaminationStudent examinationStudent) {
		examineesList.add(examinationStudent);
	}
	
	
	public void endExamination() {
		/* TODO - reach the connecting table and check:
		 * 		if student started the test
		 * 		if student ended by themselves
		 * 		if student was forced to end (only in computerized test)
		*/		
	}	
	
	public int getExamination_id() {
		return examination_id;
	}

	public int getExamId() {
		return examId;
	}

	public Duration getDuration() {
		return duration;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}
	
	public void setDuration(Duration duration) {
		this.duration = duration;
		updateExamEndTime();
	}

	public AddTimeRequest getAddTimeRequest() {
		return addTimeRequest;
	}

	public void setAddTimeRequest(AddTimeRequest addTimeRequest) {
		this.addTimeRequest = new AddTimeRequest(addTimeRequest);
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public int getStudentsStarted() {
		return studentsStarted;
	}

	public void setStudentsStarted(int studentsStarted) {
		this.studentsStarted = studentsStarted;
	}

	public int getStudentsFinished() {
		return studentsFinished;
	}

	public void setStudentsFinished(int studentsFinished) {
		this.studentsFinished = studentsFinished;
	}

	public int getStudentsNotFinsished() {
		return studentsNotFinsished;
	}

	public void setStudentsNotFinsished(int studentsNotFinsished) {
		this.studentsNotFinsished = studentsNotFinsished;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}


	public LocalDate getExamDate() {
		return examDate;
	}

	public void setExamDate(LocalDate examDate) {
		this.examDate = examDate;
	}

	public LocalTime getExamStartTime() {
		return examStartTime;
	}

	public void setExamStartTime(LocalTime examStartTime) {
		this.examStartTime = examStartTime;
	}

	public LocalTime getExamEndTime() {
		return examEndTime;
	}
	
	private void updateExamEndTime() {
		examEndTime.plusHours(duration.toHours());
		examEndTime.plusMinutes(duration.toMinutes());
	}
	
}


