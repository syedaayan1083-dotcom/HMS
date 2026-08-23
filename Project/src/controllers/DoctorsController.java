package controllers;

import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import java.net.URL;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Doctor;
import javafx.scene.control.Alert;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DatabaseConnection;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

public class DoctorsController implements Initializable // with "Initializable/implements", 
{                                                       // @override section correspondence?
    // intialize id's,buttons etc : start
    @FXML
    private AnchorPane doctorsPane;

    @FXML
    private TextField doctorNameField;
    @FXML
    private TextField experienceField;
    @FXML
    private TextField specializationField;
    @FXML
    private TextField doctorContactField;
    @FXML
    private ComboBox<String> doctorGenderComboBox;
    @FXML
    private Button addDoctorBtn;
    @FXML
    private Button updateDoctorBtn;
    @FXML
    private Button deleteDoctorBtn;
    @FXML
    private Button clearDoctorBtn;
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
    private TextField doctorSearchField;
    @FXML
    private TableView<Doctor> doctorTable; // refers to Doctor.java abstract class
    @FXML
    private TableColumn<Doctor, Integer> doctorIdColumn;
    @FXML
    private TableColumn<Doctor, Integer> experienceColumn;
    @FXML
    private TableColumn<Doctor, String> doctorNameColumn;
    @FXML
    private TableColumn<Doctor, String> specializationColumn;
    @FXML
    private TableColumn<Doctor, String> doctorContactColumn;
    @FXML
    private TableColumn<Doctor, String> doctorGenderColumn;

    @FXML
    private Pane panel1;
    @FXML
    private Pane panel2;

    // intialize id's,buttons etc : end

    // navigation: start
    @FXML
    private void openDashboard() {
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/dashboard.fxml"));
            Stage stage = (Stage) doctorsPane.getScene().getWindow();
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
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/patients.fxml"));
            Stage stage = (Stage) doctorsPane.getScene().getWindow();
            Scene scene = new Scene(root, 1400, 800);
            stage.setScene(scene);
            stage.setMaximized(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openDoctors() { }
    @FXML
    private void openAppointments() {
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/appointments.fxml"));
            Stage stage = (Stage) doctorsPane.getScene().getWindow();
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
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/billing.fxml"));
            Stage stage = (Stage) doctorsPane.getScene().getWindow();
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
                Stage stage =(Stage) doctorsPane.getScene().getWindow();
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
    // navigation: end

    // ============ Self content: (start) ============= //
   
    // Main architecture: start
    @Override 
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.seconds(0.8));
        fade.setNode(doctorsPane);
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
        addHoverEffect(addDoctorBtn);
        addHoverEffect(clearDoctorBtn);
        addHoverEffect(updateDoctorBtn);
        addHoverEffect(deleteDoctorBtn);

        initializeTable();
        initializeComboBox();
        initializeSearch();
        loadDoctors();
        doctorTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null) {
                selectedDoctor = newValue;
                populateFields(newValue);
            }
        });
    }
    // Main architecture: end

    // "ADD" functionality (start)
    @FXML
    private void addDoctor() {

        String name = doctorNameField.getText().trim();
        String experienceText = experienceField.getText().trim();
        String gender = doctorGenderComboBox.getValue();
        String specialization = specializationField.getText().trim();
        String contact = doctorContactField.getText().trim();
        // EMPTY FIELD VALIDATION
        if(name.isEmpty() || experienceText.isEmpty() || gender == null || specialization.isEmpty() || contact.isEmpty()) {

            showAlert(Alert.AlertType.WARNING,"Validation Error","Please fill all fields.");
            return;
        }
        if(!contact.matches("\\d{11}")) {
        showAlert(Alert.AlertType.ERROR,"Invalid Contact","Contact number must contain exactly 11 digits.");
        return;
        }
        // AGE VALIDATION
        int experience;
        try {
            experience = Integer.parseInt(experienceText);
            if (experience <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Experience", "Year/(s) must be greater than zero.");
                return;
            }
        }
        catch(NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Invalid Experience","Year/(s) must be numeric.");
            return;
        }
        // CREATE PATIENT
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql = "INSERT INTO doctors(name, experience, gender, specialization, contact) "+ "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, experience);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, specialization);
            preparedStatement.setString(5, contact);
            preparedStatement.executeUpdate();
            System.out.println( "Doctor Added To Database!");
            clearDoctorFields();
            loadDoctors();
            showAlert(Alert.AlertType.INFORMATION,"Success","Doctor added successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "ADD" functionality (end)

    // "UPDATE" functionality (start)
    @FXML
    private void updateDoctor() {

        if(selectedDoctor == null) {

            showAlert(Alert.AlertType.WARNING,"No Selection","Please select a doctor to update.");
            return;
        }
        String name = doctorNameField.getText().trim();
        String experienceText = experienceField.getText().trim();
        String gender = doctorGenderComboBox.getValue();
        String specialization = specializationField.getText().trim();
        String contact = doctorContactField.getText().trim();
        // VALIDATION
        if(name.isEmpty() || experienceText.isEmpty() || gender == null || specialization.isEmpty() || contact.isEmpty()) {

            showAlert(Alert.AlertType.WARNING,"Validation Error","Please fill all fields.");
            return;
        }
        if(!contact.matches("\\d{11}")) {
        showAlert(Alert.AlertType.ERROR,"Invalid Contact","Contact number must contain exactly 11 digits.");
        return;
        }
        int experience;
        try {
            experience = Integer.parseInt(experienceText);
            if (experience <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Experience", "Year/(s) must be greater than zero.");
                return;
            }
        }
        catch(NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Invalid Experience","Year/(s) must be numeric.");
            return;
        }
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "UPDATE doctors SET "
                    + "name = ?, "
                    + "experience = ?, "
                    + "gender = ?, "
                    + "specialization = ?, "
                    + "contact = ? "
                    + "WHERE id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, experience);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, specialization);
            preparedStatement.setString(5, contact);
            preparedStatement.setInt(6,selectedDoctor.getId());
            preparedStatement.executeUpdate();
            System.out.println("Doctor Updated In Database!");
            loadDoctors();
            clearDoctorFields();
            showAlert(Alert.AlertType.INFORMATION,"Updated","Doctor updated successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "UPDATE" functionality (end)

    // "DELETE" funcionality (start)
    // Delete Patient automatically cuz "ObservableList is bound to TableView" ??
    // Now, moving from list---> PostgreSQL
    @FXML
    private void deleteDoctor() {

        Doctor selected =doctorTable.getSelectionModel().getSelectedItem();
        if(selected == null) {
            showAlert(Alert.AlertType.WARNING,"No Selection","Please select a doctor to delete.");
            return;
        }
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql ="DELETE FROM doctors WHERE id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setInt(1,selected.getId());
            preparedStatement.executeUpdate();
            System.out.println("Doctor Deleted From Database!");
            loadDoctors();
            clearDoctorFields();
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Doctor deleted successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "DELETE" funcionality (end)

    // "CLEAR" functionality (start)
    @FXML
    private void clearDoctorFields() {
        
        doctorNameField.clear();
        experienceField.clear();
        doctorGenderComboBox.setValue(null);
        specializationField.clear();
        doctorContactField.clear();
        doctorTable.getSelectionModel().clearSelection();
        selectedDoctor = null;
    }
    // "CLEAR" functionality (end)

    // def_ methods() : start

    private ObservableList<Doctor> doctorList =FXCollections.observableArrayList();
    private Doctor selectedDoctor;

    private void initializeTable() {
        // organize in the order of "module" screen
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        experienceColumn.setCellValueFactory(new PropertyValueFactory<>("experience"));
        doctorGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        doctorContactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
    }
    private void initializeComboBox() {

        doctorGenderComboBox.getItems().addAll("Male","Female","Other");
    }
    private void initializeSearch() {

        FilteredList<Doctor> filteredData =new FilteredList<>(doctorList, b -> true);
        doctorSearchField.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            filteredData.setPredicate(doctor -> 
                {
                    // SHOW ALL IF SEARCH EMPTY
                    if(newValue == null || newValue.isEmpty()) { return true;}
                    String lowerCaseFilter = newValue.toLowerCase();
                    // SEARCH BY NAME
                    if(doctor.getName().toLowerCase().contains(lowerCaseFilter)) { return true;}

                    return false;
                });
        });
        SortedList<Doctor> sortedData =new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(doctorTable.comparatorProperty());
        doctorTable.setItems(sortedData); // main point of initializeSearch()??
    }
    private void loadDoctors() {

        doctorList.clear();
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql = "SELECT * FROM doctors"; // "doctors" ka table in PostgreSQL
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()) {
                // organize in the order of "module" screen
                Doctor doctor = new Doctor(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("experience"),
                        resultSet.getString("gender"),
                        resultSet.getString("specialization"),
                        resultSet.getString("contact")
                );
                doctorList.add(doctor);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void populateFields(Doctor doctor) {

        doctorNameField.setText(doctor.getName());
        experienceField.setText(String.valueOf(doctor.getExperience()));
        doctorGenderComboBox.setValue(doctor.getGender());
        specializationField.setText(doctor.getSpecialization());
        doctorContactField.setText(doctor.getContact());
    }
    private void showAlert(Alert.AlertType type,String title,String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    // def_ methods() : end
    // ============ Self content: (end) ============= //
}