package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.animation.ScaleTransition;


public class LoginController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition fade = new FadeTransition();

        fade.setDuration(Duration.seconds(4.0));

        fade.setNode(rootPane);

        fade.setFromValue(0);

        fade.setToValue(1);

        fade.play();
        addHoverEffect(loginButton);
    }

    @FXML
    private void handleLogin() {

        String username = usernameField.getText();

        String password = passwordField.getText();

        if(username.equals("admin") && password.equals("1234")) {

            try {

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/views/dashboard.fxml")
                );

                Parent root = loader.load();

                Stage stage = (Stage) loginButton.getScene().getWindow();

                Scene scene = new Scene(root, 1400, 800);

                stage.setScene(scene);

                stage.setMaximized(true);

                stage.show();

            }
            catch(Exception e) {

                e.printStackTrace();
            }

        }
        else {

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Login Failed");

            alert.setHeaderText(null);

            alert.setContentText("Invalid Username or Password");

            alert.show();
        }
    }

    private void addHoverEffect(Button btn) {

        ScaleTransition scaleUp =new ScaleTransition(Duration.millis(150), btn);
        scaleUp.setToX(1.10);
        scaleUp.setToY(1.10);
        ScaleTransition scaleDown =new ScaleTransition(Duration.millis(150), btn);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        btn.setOnMouseEntered(e -> scaleUp.playFromStart());
        btn.setOnMouseExited(e -> scaleDown.playFromStart());
    }

}