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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private TableView tableView;
    private ObservableList<FinanceCellObject> financeTable;
    private Label balanceText;
    private FinanceTableUi financeTableUi;
    
    public FinanceTableUi(DaoService daoService) {
        this.daoService = daoService;
    }
    

    
    public Parent getFinanceTable(Stage mainStage) {
        this.userStatus = daoService.loggedIn();
        financesList = daoService.getAllFinances();
        balanceText = new Label();
        BorderPane mainScene = new BorderPane();
        Background backgroundColor = new Background(new BackgroundFill(Color.rgb(153, 153, 153), CornerRadii.EMPTY, Insets.EMPTY));
        mainScene.setBackground(backgroundColor);
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
        dateField.setEditable(false);
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
        
        tableView = new TableView();
        balanceText.setFont( new Font("Arial", 20));
        updateBalance();
        
        EventHandler<ActionEvent> createEvent = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if(checkFields(priceField.getText(), eventField.getText(), dateField.getValue())) {
                    String userId = userStatus.getId();
                    double price = Double.valueOf(priceField.getText());
                    String event = eventField.getText();
                    String date = String.valueOf(dateField.getValue());
                    daoService.createFinance(price, event, date, userId);
                    financesList = daoService.getAllFinances();;
                    financesToFinanceTable();
                    tableView.setItems(financeTable);
                    priceField.clear();
                    eventField.clear();
                    updateBalance();
                    setNotification("New event created!", 5, "INFO");
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
        
        HBox.setMargin(createButton, new Insets(0,0,0,70));
        HBox.setMargin(clearButton, new Insets(0,0,0,13));
        
        topBar.setBottom(createForm);
        // display finance stats
        BorderPane financeStats = new BorderPane();
        page.setCenter(financeStats);
        
        tableView.setPlaceholder(new Label("No data to display"));
        
        financeTable = FXCollections.observableArrayList();
        financesToFinanceTable();
        
        TableColumn price = new TableColumn("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("formattedPrice"));
        price.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        
        TableColumn financeEvent = new TableColumn("Event");
        financeEvent.setCellValueFactory(new PropertyValueFactory<>("event"));
        financeEvent.prefWidthProperty().bind(tableView.widthProperty().multiply(0.645));
        
        TableColumn date = new TableColumn("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        
        TableColumn modify = new TableColumn("Modify");
        modify.setCellValueFactory(new PropertyValueFactory<>("modifyButton"));
        modify.prefWidthProperty().bind(tableView.widthProperty().multiply(0.048));
        
        TableColumn remove = new TableColumn("Remove");
        remove.setCellValueFactory(new PropertyValueFactory<>("removeButton"));
        remove.prefWidthProperty().bind(tableView.widthProperty().multiply(0.048));
        
        tableView.getColumns().addAll(price,financeEvent,date,modify,remove);

        tableView.setItems(financeTable);
        
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
    if(priceString == null || priceString.trim().length() == 0) {
        setNotification("Price cannot be empty", 5, "WARN");
        return false;
    }
    try {
        Double price = Double.parseDouble(priceString);
    } catch(NumberFormatException e) {
        System.out.println(e.getMessage());
        setNotification("Price can only contain numbers and decimal point", 5, "ERROR");
        return false;
    }
    return true;
    }
    
    public boolean checkEvent(String event) {
        if(event == null || event.trim().length() <= 2) {
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
        switch (type) {
            case "INFO":
                {
                    Background backgroundColor = new Background(new BackgroundFill(Color.rgb(47, 201, 0), CornerRadii.EMPTY, Insets.EMPTY));
                    notificationBox.setBackground(backgroundColor);
                    break;
                }
            case "ERROR":
                {  
                    Background backgroundColor = new Background(new BackgroundFill(Color.rgb(224, 9, 9), CornerRadii.EMPTY, Insets.EMPTY));
                    notificationBox.setBackground(backgroundColor);
                    break;
                }
            case "WARN":
                {
                    Background backgroundColor = new Background(new BackgroundFill(Color.rgb(59, 58, 74), CornerRadii.EMPTY, Insets.EMPTY));
                    notificationBox.setBackground(backgroundColor);
                    break;
                }
            default:
                break;
        }
        notification.setText(notificationText);
        notificationBox.setManaged(true);
        notificationBox.setVisible(true);
        delay.play();
    }
    
    public void updateBalance() {
        balanceText.setText("Balance: " + daoService.getBalance());   
    }
    
    public void financesToFinanceTable() {
        financesList = daoService.getAllFinances();
        financeTable.clear();
        financesList.stream()
                .forEach(finance -> {
                    financeTable.add(new FinanceCellObject(financeTableUi, finance));
                });
    }
    
    public void setInstance(FinanceTableUi financeTableUi) {
        this.financeTableUi = financeTableUi;
    }
    
    public DaoService getDaoService() {
        return this.daoService;
    }

    public List<Finance> getFinancesList() {
        return financesList;
    }

    public TableView getTableView() {
        return tableView;
    }

    public ObservableList<FinanceCellObject> getFinanceTable() {
        return financeTable;
    }

    public void setFinancesList(List<Finance> financesList) {
        this.financesList = financesList;
    }

    public void setTableView(TableView tableView) {
        this.tableView = tableView;
    }

    public void setFinanceTable(ObservableList<FinanceCellObject> financeTable) {
        this.financeTable = financeTable;
    }
}


