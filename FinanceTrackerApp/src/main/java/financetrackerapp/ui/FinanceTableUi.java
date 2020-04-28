/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;

import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import java.time.LocalDate;
import java.util.List;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author iPegii
 */
public class FinanceTableUi {
    
    private User userStatus;
    private DaoService daoService;
    private List<Finance> financesList;
    private Label notification;
    private HBox notificationBox;
    
    public FinanceTableUi(DaoService daoService) {
        this.daoService = daoService;
    }
    

    
    public Parent getFinanceTable(Stage mainStage) {
        this.userStatus = daoService.loggedIn();
        financesList = daoService.getAll();
        BorderPane mainScene = new BorderPane();
        BorderPane page = new BorderPane();
        mainScene.setCenter(page);
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
        HBox buttonLayer = new HBox();
        createForm.add(buttonLayer, 1, 3);
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
        createForm.add(dateText, 0, 2);
        createForm.add(dateField, 1, 2);
        
        Tooltip priceTip = new Tooltip();
        priceTip.fontProperty().set(new Font("Arial", 15));
        priceTip.wrapTextProperty().set(true);
        priceTip.setText("Price needs to be number and \nuse decimal point as separator. \nFor example: (9.5)");
        priceField.setTooltip(priceTip);
        Tooltip eventTip = new Tooltip();
        eventTip.setText("Event needs to longer than 2 letters");
        eventField.setTooltip(eventTip);
        Tooltip dateTip = new Tooltip();
        dateTip.setText("Date can be picked from calendar or typed as \"day-month-year\". For example: (30.1.2020)");
        dateField.setTooltip(dateTip);
        
        notificationBox = new HBox();
        notification = new Label(null);
        notification.setFont(Font.font ("Verdana", 20));
        notification.setTextFill(Color.WHITE);
        notificationBox.getChildren().add(notification);
        mainScene.setTop(notificationBox);
        HBox.setMargin(notification, new Insets(0,0,0,10));
        notificationBox.setVisible(false);
        notificationBox.setManaged(false);
        
        Button createButton = new Button("Create");
        buttonLayer.getChildren().add(createButton);
        
        TableView tableView = new TableView();
        
        Label balanceText = new Label("Balance: " + daoService.getBalance());
        balanceText.setFont( new Font("Arial", 20));
        
        
        EventHandler<ActionEvent> createEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            if(checkFields(priceField.getText(), eventField.getText(), dateField.getValue())) {
            String userId = userStatus.getId();
            double price = Double.valueOf(priceField.getText());
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
            setNotification("New event created!", 5, "INFO");
            } else {
            
            }
            }
        };
        createButton.setOnAction(createEvent);
        
        
        Button clearButton = new Button("Clear");
        buttonLayer.getChildren().add(clearButton);
        EventHandler<ActionEvent> clearEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            priceField.clear();
            eventField.clear();
        }
        };
        clearButton.setOnAction(clearEvent);
        
        HBox.setMargin(createButton, new Insets(0,0,0,75));
        HBox.setMargin(clearButton, new Insets(0,0,0,5));
        
        topBar.setBottom(createForm);
        // display finance stats
        BorderPane financeStats = new BorderPane();
        page.setCenter(financeStats);
        
        tableView.setPlaceholder(new Label("No data to display"));
        
        TableColumn<Finance, String> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Finance, String> financeEvent = new TableColumn<>("Event");
        financeEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        TableColumn<Finance, String> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        tableView.getColumns().add(price);
        tableView.getColumns().add(financeEvent);
        tableView.getColumns().add(date);

        financesList.forEach((f) -> {
            tableView.getItems().add(new Finance(f.getId(),f.getPrice(),f.getEvent(), f.getDate(), f.getUser()));
        });
        
        financeStats.setCenter(tableView);
        financeStats.setTop(balanceText);
        BorderPane.setMargin(balanceText,new Insets(10));
        

        return mainScene;
    }
    
    public boolean checkFields(String price, String event, LocalDate date) {
    boolean priceCorrect = checkPrice(price);
    boolean eventCorrect = checkEvent(event);
    boolean dateCorrect = checkDate(date);
    
    return priceCorrect && eventCorrect && dateCorrect;
    }
    
    public boolean checkPrice(String priceString) {
    if(priceString == null || priceString.length() == 0) {
        setNotification("Price cannot be empty", 5, "WARN");
        return false;
    }
    System.out.println(priceString.length());
    try {
        Double price = Double.parseDouble(priceString);
    } catch(NumberFormatException e) {
        System.out.println(e.getMessage());
        setNotification("Please mouse over and check correct format for price", 5, "ERROR");
        return false;
    }
    return true;
    }
    
    public boolean checkEvent(String event) {
    if(event == null || event.length() <= 2) {
        setNotification("Please mouse over and check correct format for event", 5, "ERROR");
        return false;
    } 
     return true;   
    }
    
    public boolean checkDate(LocalDate date) {
        if(date == null) {
            setNotification("Please mouse over and check correct format for date", 5, "ERROR");
            return false;
        }
       return true; 
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
