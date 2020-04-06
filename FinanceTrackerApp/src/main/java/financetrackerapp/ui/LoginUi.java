/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;

import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author iPegii
 */
public class LoginUi {
    
    private DaoService daoService;
    private boolean createUserIsTrue;
    
    public LoginUi(DaoService daoService, Parent page) {
        this.daoService = daoService;
        createUserIsTrue = false;
    }
    
    public Parent getLoginScreen() {
      BorderPane baseOverlay = new BorderPane();
        
        
        
      GridPane loginOverlay = new GridPane();
      loginOverlay.setAlignment(Pos.CENTER);
      loginOverlay.setVgap(10);
      loginOverlay.setHgap(10);
      loginOverlay.setPadding(new Insets(10, 10, 10, 10));

      baseOverlay.setCenter(loginOverlay);
      Label loginWindowText = new Label("Login to your user");
      Label loginUsername = new Label("Username: ");
      TextField loginUsernameField = new TextField();
      Button loginButton = new Button("Login");
      Button createButtonSwitch = new Button("Create");
      
      loginOverlay.add(loginWindowText, 0, 0);
      loginOverlay.add(loginUsername, 0, 1);
      loginOverlay.add(loginUsernameField, 1, 1);
      
      
      loginOverlay.add(loginButton, 0, 2);
      loginOverlay.add(createButtonSwitch, 1, 2);
      

      loginButton.setOnAction((event) -> {
      daoService.login(loginUsernameField.getText());
      });
      
      
      GridPane createUserOverlay = new GridPane();
        createUserOverlay.setAlignment(Pos.CENTER);
      createUserOverlay.setVgap(10);
      createUserOverlay.setHgap(10);
      createUserOverlay.setPadding(new Insets(10, 10, 10, 10));
      Label newUserWindowText = new Label("Create new user!");
      Label usernameTextText = new Label("Username: ");
      TextField usernameField = new TextField();
      Label nameText = new Label("Name: ");
      TextField nameField = new TextField();
      Button userCreateButton = new Button("Create");
      Button cancelButton = new Button("Cancel");
      cancelButton.setOnAction((event) -> {
      baseOverlay.setCenter(loginOverlay);
      });
      
      createUserOverlay.add(newUserWindowText, 0, 0);
      
      createUserOverlay.add(usernameTextText, 0, 1);
      createUserOverlay.add(usernameField, 1, 1);
      
      createUserOverlay.add(nameText, 0, 2);
      createUserOverlay.add(nameField, 1, 2);
      
      createUserOverlay.add(userCreateButton, 0, 3);
      createUserOverlay.add(cancelButton, 1, 3);
      
      createButtonSwitch.setOnAction((event) -> {
      baseOverlay.setCenter(createUserOverlay);
      });
      
      userCreateButton.setOnAction((event) -> {
      daoService.createUser(new User(usernameField.getText(), nameField.getText()));
      });
      
      
      
      return baseOverlay;
    }
    
    
}
