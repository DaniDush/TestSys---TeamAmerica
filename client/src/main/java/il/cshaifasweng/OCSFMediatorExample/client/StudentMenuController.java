package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;
import javafx.fxml.FXML;

public class StudentMenuController {

    @FXML
    private void switchToLogInMenu() throws IOException {
        App.setRoot("LogInMenu");
    }
}