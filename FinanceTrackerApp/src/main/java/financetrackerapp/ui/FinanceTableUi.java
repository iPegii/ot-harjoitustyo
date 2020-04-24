/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;

import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author iPegii
 */
public class FinanceTableUi {
    
    private User userStatus;
    private DaoService daoService;
    private List<Finance> financesList;
    
    public FinanceTableUi(DaoService daoService) {
        this.daoService = daoService;
    }
    
    public Parent getFinanceTable(Stage mainStage) {
        this.userStatus = daoService.loggedIn();
        financesList = daoService.getAll();
        BorderPane page = new BorderPane();
        BorderPane overlay = new BorderPane();
        overlay.setCenter(page);
        // top bar seup
        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(10));
        page.setTop(topBar);
        //Logout button and display of logged user
        HBox logoutComponents = new HBox();
        logoutComponents.setSpacing(10.0);
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction((event) -> {
        FinanceUi.setLoginScene();
        });
        String logoutText;
        if(userStatus == null) {
            logoutText = "";
        } else {
            logoutText = "You're logged in as " + daoService.loggedIn().getName();
        }
        Label logoutLable = new Label(logoutText);
        logoutLable.setFont(new Font("Arial", 17));
        logoutComponents.getChildren().addAll(logoutLable,logoutButton);
        topBar.setRight(logoutComponents);
        Label welcomeText = new Label("Welcome to Finance Tracker");
        topBar.setLeft(welcomeText);
        welcomeText.setFont(new Font("Arial", 17));
        
        // display fields for creating more events
        GridPane createForm = new GridPane();
        HBox formButtons = new HBox();
        createForm.add(formButtons, 1, 3);
        Label priceText = new Label("Price: ");
        TextField priceField = new TextField();
        createForm.add(priceText, 0, 0);
        createForm.add(priceField, 1, 0);
        
        Label eventText = new Label("Event: ");
        TextField eventField = new TextField();
        createForm.add(eventText, 0, 1);
        createForm.add(eventField, 1, 1);
        
        Label dateText = new Label("Date: ");
        DatePicker dateField = new DatePicker();
        //TextField dateField = new TextField();
       // DatePicker datePicker = new DatePicker
        createForm.add(dateText, 0, 2);
        createForm.add(dateField, 1, 2);
      //  dateField.
        
        Button createButton = new Button("Create");
        formButtons.getChildren().add(createButton);
        
        TableView tableView = new TableView();
        
        Label balanceText = new Label("Balance: " + daoService.getBalance());
        balanceText.setFont( new Font("Arial", 20));
        
        EventHandler<ActionEvent> createEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            String userId = userStatus.getId();
            int price = Integer.valueOf(priceField.getText());
            String event = eventField.getText();
            String date = String.valueOf(dateField.getValue());
            daoService.createFinance(price, event, date, userId);
            financesList = daoService.getAll();
            tableView.getItems().clear();
            financesList.forEach((f) -> {
            tableView.getItems().add(new Finance(f.getId(),f.getPrice(),f.getEvent(), f.getDate(), f.getUser()));
            });
            priceField.clear();
            eventField.clear();
            balanceText.setText("Balance: " + daoService.getBalance());
        //    dateField.clear();
            }
        };
        createButton.setOnAction(createEvent);
        
        Button clearButton = new Button("Clear");
        formButtons.getChildren().add(clearButton);
        EventHandler<ActionEvent> clearEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            priceField.clear();
            eventField.clear();
        //    dateField.clear();
        }
        };
        clearButton.setOnAction(clearEvent);
      //  formButtons.setSpacing(55.0);
        
        HBox.setMargin(createButton, new Insets(0,0,0,75));
        HBox.setMargin(clearButton, new Insets(0,0,0,5));
        
        topBar.setBottom(createForm);
        // display finance stats
        BorderPane financeStats = new BorderPane();
        page.setCenter(financeStats);
        
        tableView.setPlaceholder(new Label("No data to display"));
        
        TableColumn<String, Finance> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<String, Finance> financeEvent = new TableColumn<>("Event");
        financeEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        TableColumn<String, Finance> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        tableView.getColumns().add(price);
        tableView.getColumns().add(financeEvent);
        tableView.getColumns().add(date);
        /*
        if(userStatus != null) {
            String username = userStatus.getUsername();
        }
        */
        financesList.forEach((f) -> {
            tableView.getItems().add(new Finance(f.getId(),f.getPrice(),f.getEvent(), f.getDate(), f.getUser()));
        });
        
        financeStats.setCenter(tableView);
        financeStats.setTop(balanceText);
        financeStats.setMargin(balanceText,new Insets(10));
        
        return overlay;
    }
    
    
    
}
