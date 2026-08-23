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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.util.Duration;
import models.Billing;
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



public class BillingController implements Initializable
{
    @FXML
    private AnchorPane billingPane;

    @FXML
    private ComboBox<String> billingPatientComboBox;
    @FXML
    private ComboBox<String> paymentStatusComboBox;
    @FXML
    private TextField serviceField;
    @FXML
    private TextField amountField;
    @FXML
    private Button addBillBtn;
    @FXML
    private Button updateBillBtn;
    @FXML
    private Button deleteBillBtn;
    @FXML
    private Button clearBillBtn;
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
    private TextField billingSearchField;
    @FXML
    private TableView<Billing> billingTable;
    @FXML
    private TableColumn<Billing, Integer> billingIdColumn;
    @FXML
    private TableColumn<Billing, String> billingPatientColumn;
    @FXML
    private TableColumn<Billing, String> serviceColumn;
    @FXML
    private TableColumn<Billing, String> amountColumn;
    @FXML
    private TableColumn<Billing, String> paymentStatusColumn;

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
            Stage stage = (Stage) billingPane.getScene().getWindow();
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
            Stage stage = (Stage) billingPane.getScene().getWindow();
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
            Stage stage = (Stage) billingPane.getScene().getWindow();
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
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/appointments.fxml"));
            Stage stage = (Stage) billingPane.getScene().getWindow();
            Scene scene = new Scene(root, 1400, 800);
            stage.setScene(scene);
            stage.setMaximized(true);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openBilling() {}
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
                Stage stage =(Stage) billingPane.getScene().getWindow();
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
        fade.setNode(billingPane);
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
        addHoverEffect(addBillBtn);
        addHoverEffect(clearBillBtn);
        addHoverEffect(updateBillBtn);
        addHoverEffect(deleteBillBtn);

        initializeTable();
        initializeComboBox();
        loadPatientsIntoComboBox();
        initializeSearch();
        loadBills();
        billingTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if(newValue != null) {
                selectedBill = newValue;
                populateFields(newValue);
            }
        });
    }
    // Main architecture: end

    // def_ methods() : start

    private ObservableList<Billing> billingList =FXCollections.observableArrayList();
    private Billing selectedBill;

    private void initializeTable() {
        // organize in the order of "module" screen
        billingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        billingPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
    }
    private void initializeComboBox() {

        paymentStatusComboBox.getItems().addAll("Paid","Unpaid");
    }
    private void loadPatientsIntoComboBox() {

        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql = "SELECT DISTINCT name FROM patients";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()) {
                billingPatientComboBox.getItems().add(resultSet.getString("name"));
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void loadBills() {

        billingList.clear();
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql ="SELECT * FROM billing";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            while(resultSet.next()) {
                Billing bill = new Billing(
                        resultSet.getInt("id"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("service"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("payment_status"));
                billingList.add(bill);
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
    private void initializeSearch() {

        FilteredList<Billing> filteredData =new FilteredList<>(billingList, b -> true);
        billingSearchField.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            filteredData.setPredicate(bill -> 
                {
                    // SHOW ALL IF SEARCH EMPTY
                    if(newValue == null || newValue.isEmpty()) { return true;}
                    String lowerCaseFilter = newValue.toLowerCase();
                    // SEARCH BY NAME
                    if(bill.getPatientName().toLowerCase().contains(lowerCaseFilter)) { return true;}

                    return false;
                });
        });
        SortedList<Billing> sortedData =new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(billingTable.comparatorProperty());
        billingTable.setItems(sortedData); // main point of initializeSearch()??
    }
    private void populateFields(Billing bill) {

        billingPatientComboBox.setValue(bill.getPatientName());
        paymentStatusComboBox.setValue(bill.getPaymentStatus());
        serviceField.setText(bill.getService());
        amountField.setText(String.valueOf(bill.getAmount()));
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

    // ADD (start)
    @FXML
    private void addBill() {

        String patient =billingPatientComboBox.getValue();
        String service =serviceField.getText().trim();
        String amountText =amountField.getText().trim();
        String status =paymentStatusComboBox.getValue();
        if(patient == null ||service.isEmpty() ||amountText.isEmpty() ||status == null) {
            showAlert(Alert.AlertType.WARNING,"Validation Error","Please fill all fields.");
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Amount must be greater than zero.");
                return;
            }
        }
        catch(NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Invalid Amount","Amount must be numeric.");
            return;
        }
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "INSERT INTO billing("
                    + "patient_name, "
                    + "service, "
                    + "amount, "
                    + "payment_status"
                    + ") VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1,patient);
            preparedStatement.setString(2,service);
            preparedStatement.setDouble(3,amount);
            preparedStatement.setString(4,status);
            preparedStatement.executeUpdate();
            loadBills();
            clearBillFields();
            showAlert(Alert.AlertType.INFORMATION,"Success","Bill added successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // ADD (end)

    // "UPDATE" functionality (start)
    @FXML
    private void updateBill() {

        if(selectedBill == null) {
            showAlert(Alert.AlertType.WARNING,"No Selection","Please select a bill.");
            return;
        }
        String patient =billingPatientComboBox.getValue();
        String status =paymentStatusComboBox.getValue();
        String amountText =amountField.getText().trim();
        String service =serviceField.getText().trim();
        if(patient == null ||amountText.isEmpty() ||service.isEmpty() ||status == null) {
            showAlert(Alert.AlertType.WARNING,"Validation Error","Please fill all fields.");
            return;
        }
        double amount; // in previous cases, we used "int", over here we use "double"
        try {
            amount = Double.parseDouble(amountText);
            if (amount <= 0) 
            {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Amount must be greater than zero.");
                return;
            }
        }
        catch(NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Invalid Amount","Amount must be numeric.");
            return;
        }
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "UPDATE billing SET "
                    + "patient_name = ?, "
                    + "service = ?, "
                    + "amount = ?, "
                    + "payment_status = ? " // IMPORTANT: this line won't have " , " (syntax error)
                    + "WHERE id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setString(1, patient);
            preparedStatement.setString(2, service);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, status);
            preparedStatement.setInt(5,selectedBill.getId());
            preparedStatement.executeUpdate();
            loadBills();
            clearBillFields();
            showAlert(Alert.AlertType.INFORMATION,"Updated","Bill updated successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "UPDATE" functionality (end)

    // "DELETE" functionality (start)
    @FXML
    private void deleteBill() {

        Billing selected =billingTable.getSelectionModel().getSelectedItem();
        if(selected == null) {
            showAlert(Alert.AlertType.WARNING,"No Selection","Please select a bill.");
            return;
        }
        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "DELETE FROM billing "
                    + "WHERE id = ?";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            preparedStatement.setInt(1,selected.getId());
            preparedStatement.executeUpdate();
            loadBills();
            clearBillFields();
            showAlert(Alert.AlertType.INFORMATION,"Deleted","Bill deleted successfully.");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    // "DELETE" functionality (end)

    // "CLEAR" functionality (start)
    @FXML
    private void clearBillFields() {

        billingPatientComboBox.setValue(null);
        paymentStatusComboBox.setValue(null);
        serviceField.clear();
        amountField.clear();
        billingTable.getSelectionModel().clearSelection();
        selectedBill = null;
    }
    // CLEAR (end)
}