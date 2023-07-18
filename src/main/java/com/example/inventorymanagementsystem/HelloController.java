package com.example.inventorymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {

    //to close application
    @FXML
    private Button close;
    public void close(){
        System.exit(0);
    }

    @FXML
    private Button loginbtn;

    @FXML
    private StackPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    //Database part
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void loginAdmin(){
        String sql = "SELECT * FROM admin WHERE username = ? and password = ? ";
        connect = database.connectDB();
        try{
            prepare = connect.prepareStatement(sql);
            prepare.setString(1,username.getText());
            prepare.setString(2,password.getText());

            result = prepare.executeQuery();
            Alert alert;

            if (username.getText().isEmpty() || password.getText().isEmpty()){
              alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Credentials Error");
              alert.setHeaderText(null);
              alert.setContentText("You needs to fill in the blanks");
              alert.showAndWait();
            }
            else{
                if (result.next()){
                    //When logins are correct
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Info");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful!");
                    alert.showAndWait();
                    //Chale you for specify the file else errors :-)
                    //This links to the dashboard section
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));

                    Stage stage = new Stage();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();

                    loginbtn.getScene().getWindow().hide();
                }
                else{
                    //When logins are wrong
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Username or Password is wrong");
                    alert.showAndWait();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}