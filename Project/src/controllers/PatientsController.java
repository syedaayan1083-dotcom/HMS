package controllers;

import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Patient;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DatabaseConnection;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;

public class PatientsController implements Initializable { // this "implements ....." enabled fade animation for patients

    // id's and local variables/values ?? 
    @FXML
    private AnchorPane patientsPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField diseaseField;

    @FXML
    private TextField contactField;

    @FXML
    private TableView<Patient> patientTable;

    @FXML
    private TableColumn<Patient, Integer> idColumn;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, Integer> ageColumn;

    @FXML
    private TableColumn<Patient, String> genderColumn;

    @FXML
    private TableColumn<Patient, String> diseaseColumn;

    @FXML
    private TableColumn<Patient, String> contactColumn;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

     @FXML
    private Button clearBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Button dashboardBtn;
    @FXML
    private Button patientsBtn;
    @FXML
    private Button doctorsBtn;
    @FXML
    private Button appointmentsBtn;
    @FXML
    private Button billingBtn;
    @FXML
    private Button logoutBtn;

    @FXML
    private Pane panel1;
    @FXML
    private Pane panel2;

    // navigation start
    @FXML
    private void openDashboard() {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/views/dashboard.fxml")
            );

            Stage stage = (Stage) patientsPane.getScene().getWindow();

            Scene scene = new Scene(root, 1400, 800);

            stage.setScene(scene);

            stage.setMaximized(true);

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void openPatients() {

        // already on patients screen
    }

    @FXML
    private void openDoctors() {
        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/views/doctors.fxml")
            );

            Stage stage = (Stage) patientsPane.getScene().getWindow();

            Scene scene = new Scene(root, 1400, 800);

            stage.setScene(scene);

            stage.setMaximized(true);

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void openAppointments() {
        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/views/appointments.fxml")
            );

            Stage stage = (Stage) patientsPane.getScene().getWindow();

            Scene scene = new Scene(root, 1400, 800);

            stage.setScene(scene);

            stage.setMaximized(true);

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void openBilling() {
        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/views/billing.fxml")
            );

            Stage stage = (Stage) patientsPane.getScene().getWindow();

            Scene scene = new Scene(root, 1400, 800);

            stage.setScene(scene);

            stage.setMaximized(true);

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleLogout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> result =alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {

            try 
            {
                Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                Stage stage =(Stage) patientsPane.getScene().getWindow();
                Scene scene =new Scene(root, 1000, 600);
                stage.setScene(scene);
                stage.setTitle("Healthcare Management System");
                stage.setWidth(1018);
                stage.setHeight(640);
                stage.centerOnScreen();
                stage.show();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Patients ka self content:

    // Observable List
    private ObservableList<Patient> patientList =
            FXCollections.observableArrayList();

    //     Patient ID Counter
    //private int patientId = 1; "no need cuz now PostgreSQL will handle it"
    private Patient selectedPatient; // currently selected row ko store from "Tableview" (very imp for "update" & "delete")

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition fade = new FadeTransition();

        fade.setDuration(Duration.seconds(0.8));
        fade.setNode(patientsPane);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        addHoverEffect(dashboardBtn);
        addHoverEffect(patientsBtn);
        addHoverEffect(doctorsBtn);
        addHoverEffect(appointmentsBtn);
        addHoverEffect(billingBtn);
        addHoverEffect(logoutBtn);

        animateCard(panel1, 1.0);
        animateCard(panel2, 1.2);
        addHoverEffect(addBtn);
        addHoverEffect(clearBtn);
        addHoverEffect(updateBtn);
        addHoverEffect(deleteBtn);

        initializeTable();
        initializeComboBox();
        initializeSearch();
        //patientTable.setItems(patientList); removed cuz "initialize search"
        loadPatients();

        patientTable.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, oldValue, newValue) -> {

            if(newValue != null) {

                selectedPatient = newValue;

                populateFields(newValue);
            }
        });
    }

    private void initializeTable() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        ageColumn.setCellValueFactory(
                new PropertyValueFactory<>("age")
        );

        genderColumn.setCellValueFactory(
                new PropertyValueFactory<>("gender")
        );

        diseaseColumn.setCellValueFactory(
                new PropertyValueFactory<>("disease")
        );

        contactColumn.setCellValueFactory(
                new PropertyValueFactory<>("contact")
        );
    }




    private void initializeComboBox() {

        genderComboBox.getItems().addAll(
                "Male",
                "Female",
                "Other"
        );
    }
    private void initializeSearch() {

        FilteredList<Patient> filteredData =
                new FilteredList<>(patientList, b -> true);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                filteredData.setPredicate(patient -> {

                    // SHOW ALL IF SEARCH EMPTY
                    if(newValue == null || newValue.isEmpty()) {

                        return true;
                    }

                    String lowerCaseFilter =
                            newValue.toLowerCase();

                    // SEARCH BY NAME
                    if(patient.getName()
                            .toLowerCase()
                            .contains(lowerCaseFilter)) {

                        return true;
                    }

                    return false;
            });
        });
        SortedList<Patient> sortedData =
                new SortedList<>(filteredData);

        sortedData.comparatorProperty()
                .bind(patientTable.comparatorProperty());

        patientTable.setItems(sortedData); // main point of initializeSearch()??
    }
    private void loadPatients() 
    {
        patientList.clear();

            try {

                Connection connection =
                        DatabaseConnection.connect();

                String sql = "SELECT * FROM patients";

                PreparedStatement preparedStatement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        preparedStatement.executeQuery();

                while(resultSet.next()) {

                    Patient patient = new Patient(

                            resultSet.getInt("id"),

                            resultSet.getString("name"),

                            resultSet.getInt("age"),

                            resultSet.getString("gender"),

                            resultSet.getString("disease"),

                            resultSet.getString("contact")
                    );

                    patientList.add(patient);
                }

            }
            catch(Exception e) {

                e.printStackTrace();
            }
    }


    // ADD PATIENT
    @FXML
    private void addPatient() {

        String name = nameField.getText().trim();

        String ageText = ageField.getText().trim();

        String gender = genderComboBox.getValue();

        String disease = diseaseField.getText().trim();

        String contact = contactField.getText().trim();
        // EMPTY FIELD VALIDATION
        if(name.isEmpty() ||
        ageText.isEmpty() ||
        gender == null ||
        disease.isEmpty() ||
        contact.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please fill all fields."
            );

            return;
        }

        // AGE VALIDATION
        int age;
        try 
        {
            age = Integer.parseInt(ageText);
            if (age <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Age", "Age must be greater than zero.");
                return;
            }
        }
        catch(NumberFormatException e) 
        {
            showAlert(Alert.AlertType.ERROR,"Invalid Age","Age must be numeric.");
            return;
        }


        if(!contact.matches("\\d{11}")) {
        showAlert(Alert.AlertType.ERROR,"Invalid Contact","Contact number must contain exactly 11 digits.");
        return;
        }
        // CREATE PATIENT
        try {

            Connection connection =
                    DatabaseConnection.connect();

            String sql = "INSERT INTO patients(name, age, gender, disease, contact) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setString(1, name);

            preparedStatement.setInt(2, age);

            preparedStatement.setString(3, gender);

            preparedStatement.setString(4, disease);

            preparedStatement.setString(5, contact);

            preparedStatement.executeUpdate();

            System.out.println(
                    "Patient Added To Database!"
            );

            clearFields();

            loadPatients();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Patient added successfully."
            );

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    // CLEAR FIELDS
    @FXML
    private void clearFields() {
        
        nameField.clear();
        ageField.clear();
        genderComboBox.setValue(null);
        diseaseField.clear();
        contactField.clear();
        patientTable.getSelectionModel().clearSelection();
        selectedPatient = null;
    }

    private void populateFields(Patient patient) {

        nameField.setText(
                patient.getName()
        );

        ageField.setText(
                String.valueOf(patient.getAge())
        );

        genderComboBox.setValue(
                patient.getGender()
        );

        diseaseField.setText(
                patient.getDisease()
        );

        contactField.setText(
                patient.getContact()
        );
    }
    private void showAlert(Alert.AlertType type,
                       String title,
                       String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // UPDATE PATIENT
    @FXML
    private void updatePatient() {

        if(selectedPatient == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a patient to update."
            );

            return;
        }

        String name = nameField.getText().trim();

        String ageText = ageField.getText().trim();

        String gender = genderComboBox.getValue();

        String disease = diseaseField.getText().trim();

        String contact = contactField.getText().trim();

        // VALIDATION
        if(name.isEmpty() ||
        ageText.isEmpty() ||
        gender == null ||
        disease.isEmpty() ||
        contact.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please fill all fields."
            );

            return;
        }
        if(!contact.matches("\\d{11}")) {
        showAlert(Alert.AlertType.ERROR,"Invalid Contact","Contact number must contain exactly 11 digits.");
        return;
        }
        int age;
        try {

            age = Integer.parseInt(ageText);
            if (age <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Age", "Age must be greater than zero.");
                return;
            }

        }
        catch(NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Age",
                    "Age must be numeric."
            );

            return;
        }
        try {

            Connection connection =
                    DatabaseConnection.connect();

            String sql =
                    "UPDATE patients SET "
                    + "name = ?, "
                    + "age = ?, "
                    + "gender = ?, "
                    + "disease = ?, "
                    + "contact = ? "
                    + "WHERE id = ?";

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setString(1, name);

            preparedStatement.setInt(2, age);

            preparedStatement.setString(3, gender);

            preparedStatement.setString(4, disease);

            preparedStatement.setString(5, contact);

            preparedStatement.setInt(
                    6,
                    selectedPatient.getId()
            );

            preparedStatement.executeUpdate();

            System.out.println(
                    "Patient Updated In Database!"
            );

            loadPatients();

            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Updated",
                    "Patient updated successfully."
            );

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    // Delete Patient automatically cuz "ObservableList is bound to TableView" ??
    // Now, moving from list---> PostgreSQL
    @FXML
    private void deletePatient() {

        Patient selected =
                patientTable.getSelectionModel()
                        .getSelectedItem();

        if(selected == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a patient to delete."
            );

            return;
        }
        try {

            Connection connection =
                    DatabaseConnection.connect();

            String sql =
                    "DELETE FROM patients WHERE id = ?";

            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setInt(
                    1,
                    selected.getId()
            );

            preparedStatement.executeUpdate();

            System.out.println(
                    "Patient Deleted From Database!"
            );

            loadPatients();

            clearFields();

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Deleted",
                    "Patient deleted successfully."
            );

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    private void addHoverEffect(Button btn) {

        ScaleTransition scaleUp =new ScaleTransition(Duration.millis(150), btn);
        scaleUp.setToX(1.15);
        scaleUp.setToY(1.15);
        ScaleTransition scaleDown =new ScaleTransition(Duration.millis(150), btn);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        btn.setOnMouseEntered(e -> scaleUp.playFromStart());
        btn.setOnMouseExited(e -> scaleDown.playFromStart());
    }
    private void animateCard(Pane card, double delay) {

        card.setOpacity(0);
        card.setTranslateY(40);
        FadeTransition fade =new FadeTransition(Duration.seconds(0.7), card);
        fade.setFromValue(0);
        fade.setToValue(1);
        TranslateTransition slide =new TranslateTransition(Duration.seconds(0.7), card);
        slide.setFromY(40);
        slide.setToY(0);
        fade.setDelay(Duration.seconds(delay));
        slide.setDelay(Duration.seconds(delay));
        fade.play();
        slide.play();
    }
}