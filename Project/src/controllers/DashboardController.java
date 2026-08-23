package controllers;
import javafx.animation.TranslateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import java.sql.Connection; 
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import database.DatabaseConnection;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.animation.FadeTransition;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class DashboardController implements Initializable{

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Pane patientsCard;
    @FXML
    private Pane doctorsCard;
    @FXML
    private Pane appointmentsCard;
    @FXML
    private Pane billsCard;
    @FXML
    private Pane revenueCard;
    @FXML
    private Pane pieChartCard;
    @FXML
    private Pane barChartCard;

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
    private Label totalPatientsLabel;
    @FXML 
    private Label totalDoctorsLabel; 
    @FXML 
    private Label totalAppointmentsLabel; 
    @FXML 
    private Label totalBillsLabel;
    @FXML 
    private Label totalRevenueLabel; 
    @FXML 
    private PieChart billingPieChart; 
    @FXML 
    private BarChart<String, Number> appointmentBarChart;

    // navigation: start
    @FXML
    private void openDashboard() {}
    
    @FXML
    private void openPatients() {
        try 
        {
            Parent root = FXMLLoader.load(getClass().getResource("/views/patients.fxml"));
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
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
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
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
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
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
            Stage stage = (Stage) dashboardPane.getScene().getWindow();
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
                Stage stage =(Stage) dashboardPane.getScene().getWindow();
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

    // Dashboard starting point cuz of animations

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.seconds(0.8));
        fade.setNode(dashboardPane);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        animateCard(patientsCard, 1.0);
        animateCard(doctorsCard, 1.2);
        animateCard(appointmentsCard, 1.4);
        animateCard(billsCard, 1.6);
        animateCard(revenueCard, 1.8);
        animateCard(pieChartCard, 1.10);
        animateCard(barChartCard, 1.12);
        addHoverEffect(patientsCard);
        addHoverEffect(doctorsCard);
        addHoverEffect(appointmentsCard);
        addHoverEffect(billsCard);
        addHoverEffect(revenueCard);
        addHoverEffect(pieChartCard);
        addHoverEffect(barChartCard);
        addHoverEffect(dashboardBtn);
        addHoverEffect(patientsBtn);
        addHoverEffect(doctorsBtn);
        addHoverEffect(appointmentsBtn);
        addHoverEffect(billingBtn);
        addHoverEffect(logoutBtn);
        
        loadBillingPieChart(); 
        loadAppointmentBarChart();
        billingPieChart.setOpacity(0);
        appointmentBarChart.setOpacity(0);
        FadeTransition pieFade =new FadeTransition(Duration.seconds(1), billingPieChart);
        pieFade.setDelay(Duration.seconds(1.4));
        pieFade.setFromValue(0);
        pieFade.setToValue(1);
        FadeTransition barFade =new FadeTransition(Duration.seconds(1), appointmentBarChart);
        barFade.setDelay(Duration.seconds(1.8));
        barFade.setFromValue(0);
        barFade.setToValue(1);
        pieFade.play();
        barFade.play(); 
        
        loadStatistics();
    }

    private void loadStatistics() {

        try 
        {
            Connection connection =DatabaseConnection.connect();
            // TOTAL PATIENTS
            String patientSql ="SELECT COUNT(DISTINCT name) FROM patients";
            PreparedStatement patientStatement =connection.prepareStatement(patientSql);
            ResultSet patientResult =patientStatement.executeQuery();
            if(patientResult.next()) {
                //totalPatientsLabel.setText(String.valueOf(patientResult.getInt(1)));
                int patients = patientResult.getInt(1);
                animateCounter(totalPatientsLabel, patients);
            }
            // TOTAL DOCTORS
            String doctorSql ="SELECT COUNT(DISTINCT name) FROM doctors";
            PreparedStatement doctorStatement =connection.prepareStatement(doctorSql);
            ResultSet doctorResult =doctorStatement.executeQuery();
            if(doctorResult.next()) {
                //totalDoctorsLabel.setText(String.valueOf(doctorResult.getInt(1)));
                int doctors = doctorResult.getInt(1);
                animateCounter(totalDoctorsLabel, doctors);
            }
            // TOTAL APPOINTMENTS
            String appointmentSql ="SELECT COUNT(*) FROM appointments";
            PreparedStatement appointmentStatement =connection.prepareStatement(appointmentSql);
            ResultSet appointmentResult =appointmentStatement.executeQuery();
            if(appointmentResult.next()) {
                //totalAppointmentsLabel.setText(String.valueOf(appointmentResult.getInt(1)));
                int appointments = appointmentResult.getInt(1);
                animateCounter(totalAppointmentsLabel, appointments);
            }
            // TOTAL BILLS
            String billSql ="SELECT COUNT(*) FROM billing";
            PreparedStatement billStatement =connection.prepareStatement(billSql);
            ResultSet billResult =billStatement.executeQuery();
            if(billResult.next()) {
                //totalBillsLabel.setText(String.valueOf(billResult.getInt(1)));
                int bills = billResult.getInt(1);
                animateCounter(totalBillsLabel, bills);
            }
            // TOTAL REVENUE
            String revenueSql ="SELECT SUM(amount) FROM billing";
            PreparedStatement revenueStatement =connection.prepareStatement(revenueSql);
            ResultSet revenueResult =revenueStatement.executeQuery();
            if(revenueResult.next()) {
                double revenue =revenueResult.getDouble(1);
                //totalRevenueLabel.setText("$" + revenue);
                animateRevenue(totalRevenueLabel, revenue);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void loadBillingPieChart() {

        try 
        {
            Connection connection =DatabaseConnection.connect();
            String paidSql =
                    "SELECT COUNT(*) FROM billing "
                    + "WHERE payment_status = 'Paid'";
            PreparedStatement paidStatement =connection.prepareStatement(paidSql);
            ResultSet paidResult =paidStatement.executeQuery();
            int paidCount = 0;
            if(paidResult.next()) {
                paidCount =paidResult.getInt(1);
            }
            String unpaidSql =
                    "SELECT COUNT(*) FROM billing "
                    + "WHERE payment_status = 'Unpaid'";
            PreparedStatement unpaidStatement =connection.prepareStatement(unpaidSql);
            ResultSet unpaidResult =unpaidStatement.executeQuery();
            int unpaidCount = 0;
            if(unpaidResult.next()) {
                unpaidCount =unpaidResult.getInt(1);
            }
            billingPieChart.getData().add(new PieChart.Data("Paid",paidCount));
            billingPieChart.getData().add(new PieChart.Data("Unpaid",unpaidCount));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void loadAppointmentBarChart() {

        try 
        {
            Connection connection =DatabaseConnection.connect();
            String sql =
                    "SELECT status, COUNT(*) "
                    + "FROM appointments "
                    + "GROUP BY status";
            PreparedStatement preparedStatement =connection.prepareStatement(sql);
            ResultSet resultSet =preparedStatement.executeQuery();
            XYChart.Series<String, Number> series =new XYChart.Series<>();
            series.setName("Appointments");
            while(resultSet.next()) {
                series.getData().add(new XYChart.Data<>(resultSet.getString("status"),resultSet.getInt("count")));
            }
            appointmentBarChart.getData().add(series);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    // ANIMATIONS
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
    private void addHoverEffect(Pane card) {

        ScaleTransition scaleUp =new ScaleTransition(Duration.millis(150), card);
        scaleUp.setToX(1.05);
        scaleUp.setToY(1.05);
        ScaleTransition scaleDown =new ScaleTransition(Duration.millis(150), card);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        card.setOnMouseEntered(e -> scaleUp.playFromStart());
        card.setOnMouseExited(e -> scaleDown.playFromStart());
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


    private void animateCounter(Label label, int endValue) {

        Timeline timeline = new Timeline();
        int steps = 40;
        for (int i = 0; i <= steps; i++) {
            final int value = (int)((double) endValue * i / steps);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 50),
            e -> label.setText(String.valueOf(value))));
        }
        timeline.play();
    }
    private void animateRevenue(Label label, double endValue) {

        Timeline timeline = new Timeline();
        int steps = 50;
        for (int i = 0; i <= steps; i++) {
            final double value = endValue * i / steps;
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 50),
            e -> label.setText("$" + String.format("%.0f", value))));
        }
        timeline.play();
        timeline.setOnFinished(e -> popLabel(label));
    }
    private void popLabel(Label label) {

        ScaleTransition pop = new ScaleTransition(Duration.millis(250),label);
        pop.setFromX(1);
        pop.setFromY(1);
        pop.setToX(1.15);
        pop.setToY(1.15);
        pop.setAutoReverse(true);
        pop.setCycleCount(2);
        pop.play();
    }
}
