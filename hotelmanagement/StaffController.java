package com.hotel.hotelmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffController {
    private DBConnection dbConnection;
    private ObservableList<Staff> staffList;

    public StaffController() {
        dbConnection = new DBConnection();
        staffList = FXCollections.observableArrayList();
    }

    public ObservableList<Staff> getAllStaff() {
        try (Connection conn = dbConnection.getConnection()) {
            String query = "SELECT * FROM staff";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet resultSet = stmt.executeQuery();

            staffList.clear(); // Réinitialise la liste

            while (resultSet.next()) {
                int staffID = resultSet.getInt("staffID");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String position = resultSet.getString("position");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");

                staffList.add(new Staff(staffID, name, age, gender, position, phone, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public void addStaff(Staff staff) {
        try (Connection conn = dbConnection.getConnection()) {
            String query = "INSERT INTO staff (name, age, gender, position, phone, email) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, staff.getName());
            stmt.setInt(2, staff.getAge());
            stmt.setString(3, staff.getGender());
            stmt.setString(4, staff.getPosition());
            stmt.setString(5, staff.getPhone());
            stmt.setString(6, staff.getEmail());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Staff added successfully", AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Error", "Unable to add staff", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void updateStaff(Staff staff) {
        try (Connection conn = dbConnection.getConnection()) {
            String query = "UPDATE staff SET name = ?, age = ?, gender = ?, position = ?, phone = ?, email = ? WHERE staffID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, staff.getName());
            stmt.setInt(2, staff.getAge());
            stmt.setString(3, staff.getGender());
            stmt.setString(4, staff.getPosition());
            stmt.setString(5, staff.getPhone());
            stmt.setString(6, staff.getEmail());
            stmt.setInt(7, staff.getStaffID());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Staff updated successfully", AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Error", "Unable to update staff", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void deleteStaff(int staffID) {
        try (Connection conn = dbConnection.getConnection()) {
            String query = "DELETE FROM staff WHERE staffID = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, staffID);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Staff deleted successfully", AlertType.INFORMATION);
            }
        } catch (SQLException e) {
            showAlert("Error", "Unable to delete staff", AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
