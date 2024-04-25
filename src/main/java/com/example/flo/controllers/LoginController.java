package com.example.flo.controllers;

import com.example.flo.MainApp;
import com.example.flo.utils.AlertUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    
    @FXML
    public void front(ActionEvent ignored) {
        MainApp.getInstance().loadFront();
    }
    
    @FXML
    public void back(ActionEvent ignored) {
        MainApp.getInstance().loadBack();
    }
}
