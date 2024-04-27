package com.example.javafxlogincrudform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FormController {
    @FXML
    private BorderPane login_form;

    @FXML
    private Button si_createAccountBtn;

    @FXML
    private Button si_loginBtn;

    @FXML
    private PasswordField si_password;

    @FXML
    private TextField si_username;

    @FXML
    private BorderPane signup_form;

    @FXML
    private Button su_loginAccountBtn;

    @FXML
    private PasswordField su_password;

    @FXML
    private Button su_signupBtn;

    @FXML
    private TextField su_username;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void loginAccount(){
        String sql = "SELECT username, password FROM admin WHERE username = ? AND password = ?";
        connect = Database.connect();

        try {
            Alert alert;
            if (si_username.getText().isEmpty() || si_password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, si_username.getText());
                prepare.setString(2, si_password.getText());

                result = prepare.executeQuery();
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Login");
                    alert.showAndWait();

                    //TO HIDE THE LOGIN FORM
                    si_loginBtn.getScene().getWindow().hide();

                    //TO SHOW THE CRUD FORM
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("crudForm.fxml"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setTitle("CRUD Form and Table Operations");
                    stage.setScene(scene);
                    stage.show();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect Username/Password");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerAccount(){
        String sql = "INSERT INTO admin (username, password) VALUES(?, ?)";
        connect = Database.connect();

        try {
            Alert alert;
            if (su_username.getText().isEmpty() || su_password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }else {
                String checkData = "SELECT username FROM admin WHERE username ='" + su_username.getText() + "'";
                prepare = connect.prepareStatement(checkData);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(su_username.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    if (su_password.getText().length() < 8) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Ivalid password, atleast 8 characters needed");
                        alert.showAndWait();
                    } else {
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1, su_username.getText());
                        prepare.setString(2, su_password.getText());

                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully created a new account!");
                        alert.showAndWait();

                        login_form.setVisible(true);
                        signup_form.setVisible(false);

                        su_username.setText("");
                        su_password.setText("");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void switchForm(ActionEvent event){
        if (event.getSource() == su_loginAccountBtn){
            login_form.setVisible(true);
            signup_form.setVisible(false);
        } else if (event.getSource() == si_createAccountBtn) {
            login_form.setVisible(false);
            signup_form.setVisible(true);
        }

    }
}