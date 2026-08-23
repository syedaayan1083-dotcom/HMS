package controllers;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import models.Appointment;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DatabaseConnection;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentsController implements Initializable
{
    @FXML
    private AnchorPane appointmentsPane;

    @FXML
    private ComboBox<String> patientComboBox;
    @FXML
    private ComboBox<String> doctorComboBox;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private DatePicker appointmentDatePicker;

    @FXML
    private ComboBox<Integer> hourComboBox;

    @FXML
    private ComboBox<Integer> minuteComboBox;
    @FXML
    private Button addAppointmentBtn;
    @FXML
    private Button updateAppointmentBtn;
    @FXML
    private Button deleteAppointmentBtn;
    @FXML
    private Button clearAppointmentBtn;
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
    private TextField appointmentSearchField;
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> patientColumn;
    @FXML
    private TableColumn<Appointment, String> doctorColumn;
    @FXML
    private TableColumn<Appointment, String> dateColumn;
    @FXML
    private TableColumn<Appointment, String> timeColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;

    @FXML
    private Pane panel1;
    @FXML
    private Pane panel2;

    // navigation: start
    @FXML
    private void openDashboard() {
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/dashboard.fxml"));
            Stage stage = (Stage) appointmentsPane.getScene().getWindow();
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
            Stage stage = (Stage) appointmentsPane.getScene().getWindow();
            Scene scene = new Scene(root, 1400, 800);
            stage.setScene(scene);
            stage.setMaximized(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openDoctors() { 
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/doctors.fxml"));
            Stage stage = (Stage) appointmentsPane.getScene().getWindow();
            Scene scene = new Scene(root, 1400, 800);
            stage.setScene(scene);
            stage.setMaximized(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openAppointments() {}
    @FXML
    private void openBilling() {
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/billing.fxml"));
            Stage stage = (Stage) appointmentsPane.getScene().getWindow();
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
                Stage stage =(Stage) appointmentsPane.getScene().getWindow();
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

    // ============ Self content ============= //

    // Main architecture: start
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.seconds(0.8));
        fade.setNode(appointmentsPane);
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
        addHoverEffect(addAppointmentBtn);
        addHoverEffect(clearAppointmentBtn);
        addHoverEffect(updateAppointmentBtn);
        addHoverEffect(deleteAppointmentBtn);

        for (int i = 0; i < 24; i++) {
            hourComboBox.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            minuteComboBox.getItems().add(i);
        }
        hourComboBox.setValue(null); //org: (9)
        minuteComboBox.setValue(null);// org: (0)
        initializeTable();
        initializeComboBox();
        loadPatientsIntoComboBox();
        loadDoctorsIntoComboBox();
        initializeSearch(); // search's + display's content of table
        loadAppointments();
        appointmentTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null) {
                selectedAppointment = newValue;
                populateFields(newValue);
            }
        });
    }
    // Main architecture: end

    // def_ methods() : start

    private ObservableList<Appointment> appointmentList =FXCollections.observableArrayList();
    private Appointment selectedAppointment;

    private void initializeTable() {
        // organize in the order of "module" screen
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void initializeComboBox() {

        statusComboBox.getItems().addAll("Pending","Completed","Cancelled");
    }
    private void loadPatientsIntoComboBox() {

        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql = "SELECT DISTINCT name FROM patients";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()) {
                patientComboBox.getItems().add(resultSet.getString("name"));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void loadDoctorsIntoComboBox() {

        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql = "SELECT DISTINCT name FROM doctors";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()) {
                doctorComboBox.getItems().add(resultSet.getString("name"));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlert(Alert.AlertType type,String title,String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadAppointments() {

        appointmentList.clear();
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql ="SELECT * FROM appointments";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("id"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("doctor_name"),
                        resultSet.getString("appointment_date"),
                        resultSet.getString("appointment_time"),
                        resultSet.getString("status") );
                appointmentList.add(appointment);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void populateFields(Appointment appointment) {

        patientComboBox.setValue(appointment.getPatientName());
        doctorComboBox.setValue(appointment.getDoctorName());
        appointmentDatePicker.setValue(LocalDate.parse(appointment.getAppointmentDate()));
        String[] parts =appointment.getAppointmentTime().split(":");
        hourComboBox.setValue(Integer.parseInt(parts[0]));
        minuteComboBox.setValue(Integer.parseInt(parts[1]));
        statusComboBox.setValue(appointment.getStatus());
    }
    private void initializeSearch() {

        FilteredList<Appointment> filteredData =new FilteredList<>(appointmentList, b -> true);
        appointmentSearchField.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            filteredData.setPredicate(appointment -> 
                {
                    // SHOW ALL IF SEARCH EMPTY
                    if(newValue == null || newValue.isEmpty()) { return true;}
                    String lowerCaseFilter = newValue.toLowerCase();
                    // SEARCH BY NAME
                    if(appointment.getPatientName().toLowerCase().contains(lowerCaseFilter)) { return true;}

                    return false;
                });
        });
        SortedList<Appointment> sortedData =new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(appointmentTable.comparatorProperty());
        appointmentTable.setItems(sortedData); // main point of initializeSearch()??
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

    // "ADD" functionality (start)
    @FXML
    private void addAppointment() {

        String patient =patientComboBox.getValue();
        String doctor =doctorComboBox.getValue();
        LocalDate selectedDate =appointmentDatePicker.getValue();
        Integer selectedHour =hourComboBox.getValue();
        Integer selectedMinute =minuteComboBox.getValue();
        String status =statusComboBox.getValue();
        // VALIDATION
        if(patient == null ||doctor == null || selectedDate==null ||selectedHour==null|| selectedMinute==null ||status == null) {
            showAlert(Alert.AlertType.WARNING,"Validation Error","Please fill all fields.");
            return;
        }
        LocalTime selectedTime =LocalTime.of(selectedHour, selectedMinute);
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "INSERT INTO appointments("
                    + "patient_name, "
                    + "doctor_name, "
                    + "appointment_date, "
                    + "appointment_time, "
                    + "status"
                    + ") VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, patient);
            preparedStatement.setString(2, doctor);
            preparedStatement.setString(3,selectedDate.toString());
            preparedStatement.setString(4,selectedTime.toString());
            preparedStatement.setString(5,status);
            preparedStatement.executeUpdate();
            loadAppointments();
            clearAppointmentFields();
            showAlert(Alert.AlertType.INFORMATION,"Success","Appointment added successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "ADD" functionality (end)

    // "UPDATE" functionality (start)
    @FXML
    private void updateAppointment() {
         
        if(selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING,"No Selection","Please select an appointment.");
            return;
        }
        String patient =patientComboBox.getValue();
        String doctor =doctorComboBox.getValue();
        LocalDate selectedDate =appointmentDatePicker.getValue();
        Integer selectedHour =hourComboBox.getValue();
        Integer selectedMinute =minuteComboBox.getValue();
        String status =statusComboBox.getValue();
        // VALIDATION
        if(patient == null ||doctor == null || selectedDate==null ||selectedHour==null|| selectedMinute==null ||status == null) {
            showAlert(Alert.AlertType.WARNING,"Validation Error","Please fill all fields.");
            return;
        }
        LocalTime selectedTime =LocalTime.of(selectedHour, selectedMinute);
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "UPDATE appointments SET "
                    + "patient_name = ?, "
                    + "doctor_name = ?, "
                    + "appointment_date = ?, "
                    + "appointment_time = ?, "
                    + "status = ? "
                    + "WHERE id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, patient);
            preparedStatement.setString(2, doctor);
            preparedStatement.setString(3,selectedDate.toString());
            preparedStatement.setString(4,selectedTime.toString());
            preparedStatement.setString(5,status);
            preparedStatement.setInt(6,selectedAppointment.getId());
            preparedStatement.executeUpdate();
            loadAppointments();
            clearAppointmentFields();
            showAlert(Alert.AlertType.INFORMATION,"Updated","Appointment updated successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        } 
    }
    // "UPDATE" functionality (end)

    // "DELETE" functionality (start)
    @FXML
    private void deleteAppointment() {

        Appointment selected =appointmentTable.getSelectionModel().getSelectedItem();
        if(selected == null) {
            showAlert(Alert.AlertType.WARNING,"No Selection","Please select an appointment.");
            return;
        }
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "DELETE FROM appointments "
                    + "WHERE id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setInt(1,selected.getId());
            preparedStatement.executeUpdate();
            loadAppointments();
            clearAppointmentFields();
            showAlert(Alert.AlertType.INFORMATION,"Deleted","Appointment deleted successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "DELETE" functionality (end)
    
    // "CLEAR" functionality (start)
    @FXML
    private void clearAppointmentFields() {

        patientComboBox.setValue(null);
        doctorComboBox.setValue(null);
        appointmentDatePicker.setValue(null);
        hourComboBox.setValue(null);
        minuteComboBox.setValue(null);
        statusComboBox.setValue(null);
        appointmentTable.getSelectionModel().clearSelection();
        selectedAppointment = null;
    }
    // "CLEAR" functionality (end)
}