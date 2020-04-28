/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;

import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.User;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author iPegii
 */
public class LoginUi {
    
    private DaoService daoService;
    private boolean createUserIsTrue;
    private Stage mainStage;
    private HBox notificationBox;
    private Label notification;
    
    
    public LoginUi(DaoService daoService) {
        this.daoService = daoService;
        this.createUserIsTrue = false;
        this.mainStage = mainStage;
    }
    
    public Parent getLoginScreen(Stage mainStage) {
      BorderPane baseOverlay = new BorderPane();
      
      VBox loginScreenPane = new VBox();
      HBox loginButtonLayer = new HBox();
      GridPane loginOverlay = new GridPane();
      loginOverlay.setAlignment(Pos.CENTER);
      loginOverlay.setVgap(10);
      loginOverlay.setHgap(10);
      loginOverlay.setPadding(new Insets(10, 10, 10, 10));

      baseOverlay.setCenter(loginScreenPane);
      
      Label loginWindowText = new Label("Login to your user");
      Label loginUsername = new Label("Username: ");
      TextField loginUsernameField = new TextField();
      Button loginButton = new Button("Login");
      Button createButtonSwitch = new Button("Create");
      
      notificationBox = new HBox();
      notification = new Label("");
      notificationBox.setVisible(false);
      notificationBox.setManaged(false);
      notification.setFont(Font.font ("Verdana", 15));
      notification.setTextFill(Color.WHITE);
      notificationBox.getChildren().add(notification);
      baseOverlay.setTop(notificationBox);
      
      loginScreenPane.getChildren().add(loginOverlay);
      loginScreenPane.getChildren().add(loginButtonLayer);

      
      loginOverlay.add(loginWindowText, 0, 0);
      loginOverlay.add(loginUsername, 0, 1);
      loginOverlay.add(loginUsernameField, 1, 1);
      
      loginButtonLayer.getChildren().add(loginButton);
      loginButtonLayer.getChildren().add(createButtonSwitch);
      
      loginButton.setOnAction((event) -> {
      User user = daoService.login(loginUsernameField.getText());
      if(user != null) {
      FinanceUi.setFinanceScene();
      }
      setNotification("User not found", 5, "WARN");
      });
      
      VBox createUserPane = new VBox();
      HBox createUserButtonLayer = new HBox();
      GridPane createUserOverlay = new GridPane();
      createUserOverlay.setAlignment(Pos.CENTER);
      createUserOverlay.setVgap(10);
      createUserOverlay.setHgap(10);
      createUserOverlay.setPadding(new Insets(10, 10, 10, 10));
      
      
      createUserPane.getChildren().add(createUserOverlay);
      createUserPane.getChildren().add(createUserButtonLayer);
      
      HBox.setMargin(loginButton, new Insets(0,0,0,150));
      HBox.setMargin(createButtonSwitch, new Insets(0,0,0,25));
      
      Label newUserWindowText = new Label("Create new user!");
      
      Label usernameTextText = new Label("Username: ");
      TextField usernameField = new TextField();
      
      Label nameText = new Label("Name: ");
      TextField nameField = new TextField();
      
      Button userCreateButton = new Button("Create");
      Button cancelButton = new Button("Cancel");
      
      cancelButton.setOnAction((event) -> {
      baseOverlay.setCenter(loginScreenPane);
      });
      
      createUserOverlay.add(newUserWindowText, 0, 0);
      
      createUserOverlay.add(usernameTextText, 0, 1);
      createUserOverlay.add(usernameField, 1, 1);
      
      createUserOverlay.add(nameText, 0, 2);
      createUserOverlay.add(nameField, 1, 2);
      
      createUserButtonLayer.getChildren().add(userCreateButton);
      createUserButtonLayer.getChildren().add(cancelButton);
      
      HBox.setMargin(userCreateButton, new Insets(0,0,0,150));
      HBox.setMargin(cancelButton, new Insets(0,0,0,25));
      
      createButtonSwitch.setOnAction((event) -> {
      baseOverlay.setCenter(createUserPane);
      });
      
      userCreateButton.setOnAction((event) -> {
       //   System.out.println(usernameField.getText() + " : " + nameField.getText());
      if(daoService.createUser(usernameField.getText(), nameField.getText()) != null) {
      setNotification("User created", 5, "INFO");
      } else {
      setNotification("This username is already taken", 5, "WARN");
      }
      });
      
      
      
      return baseOverlay;
    }
    
    public void setNotification(String notificationText, int time, String type) {
        PauseTransition delay = new PauseTransition();
        delay.setDuration(Duration.seconds(time));
        delay.setOnFinished(event ->  {
            notification.setText(null);
            notificationBox.setVisible(false);
            notificationBox.setManaged(false);
        });
        
        if(type.equals("INFO")) {
            Background backgroundColor = new Background(new BackgroundFill(Color.rgb(47, 201, 0), CornerRadii.EMPTY, Insets.EMPTY));
            notificationBox.setBackground(backgroundColor);
        } else if(type.equals("ERROR")) {
            Background backgroundColor = new Background(new BackgroundFill(Color.rgb(224, 9, 9), CornerRadii.EMPTY, Insets.EMPTY));
            notificationBox.setBackground(backgroundColor);
        } else if(type.equals("WARN")){
            Background backgroundColor = new Background(new BackgroundFill(Color.rgb(59, 58, 74), CornerRadii.EMPTY, Insets.EMPTY));
            notificationBox.setBackground(backgroundColor);  
        }
        
        notification.setText(notificationText);
        notificationBox.setManaged(true);
        notificationBox.setVisible(true);
        delay.play();
    }
    
    
    
}
