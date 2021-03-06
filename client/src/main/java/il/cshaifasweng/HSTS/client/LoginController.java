/**
 * Sample Skeleton for 'primary.fxml' Controller Class
 */

package il.cshaifasweng.HSTS.client;

import il.cshaifasweng.HSTS.entities.Carrier;
import il.cshaifasweng.HSTS.entities.Role;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.HashMap;
import javafx.event.ActionEvent;


public class LoginController {
	
	static public SimpleClient client;
	private Carrier localCarrier = null;
	static public String userReceviedfullName = null;
	static public Integer userReceviedID = null;
	static public HashMap<String, Integer> userReceviedCourses = null;
	Role userReceviedRole = null;
	
    @FXML // fx:id="usernameTF"
    private TextField usernameTF; // Value injected by FXMLLoader

    @FXML // fx:id="passwordPF"
    private PasswordField passwordPF; // Value injected by FXMLLoader

    @FXML // fx:id="loginButton"
    private Button loginButton; // Value injected by FXMLLoader
    
    @FXML // fx:id="incorrectUsernamePasswordLabel"
    private Label incorrectUsernamePasswordLabel; // Value injected by FXMLLoader
    
    @FXML // fx:id="alreadyLoggedInLabel"
    private Label alreadyLoggedInLabel; // Value injected by FXMLLoader

    @FXML
    void validate(ActionEvent event) throws IOException {
    	// TODO delete when connecting to remote
    	client = SimpleClient.getClient();
    	client.openConnection();
    	
    	localCarrier = client.handleMessageFromLogInController(usernameTF.getText(), passwordPF.getText());
    	System.out.println("message from LogInController Handled");
		userReceviedfullName = (String) localCarrier.carrierMessageMap.get("fullName");
		userReceviedRole = (Role) localCarrier.carrierMessageMap.get("Role");
		userReceviedID = (Integer) localCarrier.carrierMessageMap.get("ID");
		userReceviedCourses = (HashMap<String, Integer>) localCarrier.carrierMessageMap.get("Courses");
		
		System.out.println("data From SimpleServer :" +  userReceviedRole +" "+ userReceviedID + "");

		switch (userReceviedRole) {
		case STUDENT:
			App.setRoot("StudentMenu");
			break;
		case TEACHER:
			App.setRoot("TeacherMenu");
			break;
		case PRINCIPLE:
			App.setRoot("PrincipleMenu");
			break;
		case INVALID:
			if (userReceviedID == -2) {
				incorrectUsernamePasswordLabel.setVisible(false);
				alreadyLoggedInLabel.setVisible(true);
				passwordPF.clear();
			}
			else {
				alreadyLoggedInLabel.setVisible(false);
				incorrectUsernamePasswordLabel.setVisible(true);
				passwordPF.clear();
			}
			break;
		}
    	
    }
    static void logMeOut() {
    	System.out.println("logging out");
    	client.handleLogOut(userReceviedID);
    }

}
