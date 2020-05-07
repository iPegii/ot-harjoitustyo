/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;

import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import financetrackerapp.ui.FinanceTableUi.FinanceRowObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

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
    private ObservableList<FinanceRowObject> financeTable;
    private Label balanceText;
    
    public FinanceTableUi(DaoService daoService) {
        this.daoService = daoService;
    }
    

    
    public Parent getFinanceTable(Stage mainStage) {
        this.userStatus = daoService.loggedIn();
        financesList = daoService.getAllFinances();
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
        
        balanceText = new Label("Balance: " + daoService.getBalance());
        balanceText.setFont( new Font("Arial", 20));
        
        
        EventHandler<ActionEvent> createEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            if(checkFields(priceField.getText(), eventField.getText(), dateField.getValue())) {
            String userId = userStatus.getId();
            double price = Double.valueOf(priceField.getText());
            String event = eventField.getText();
            String date = String.valueOf(dateField.getValue());
            daoService.createFinance(price, event, date, userId);
            financesList = daoService.getAllFinances();;
            financeTable = FXCollections.observableArrayList();
            financesList.stream()
                .forEach(finance -> {
                    financeTable.add(new FinanceRowObject(finance.getId(),finance.getPrice(),
                            finance.getFormattedPrice(), finance.getEvent(), finance.getDate(), finance.getUser()));
                });
            tableView.setItems(financeTable);
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
        
        financeTable = FXCollections.observableArrayList();
        financesList.stream()
                .forEach(finance -> {
                    financeTable.add(new FinanceRowObject(finance.getId(),finance.getPrice(),
                            finance.getFormattedPrice(), finance.getEvent(), finance.getDate(), finance.getUser()));
                });
        
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
    
    public class ModifyButton extends Button {

        public ModifyButton(String id, String formattedPrice, Double price, String event, String date, String user) {
            super("Modify");
            setOnAction((ActionEvent action) -> {
                Dialog<Pair<String,String>> dialog = new Dialog<>();
                dialog.setTitle("Finance modification");
                dialog.setHeaderText("You're modifying: ");
           //     dialog.setHeaderText("You're modifying \"" + price + " - " + event + " - " + date + "\"");
                VBox vbox = new VBox();
                GridPane grid = new GridPane();
                
                Text financeToModify = new Text("\"" + formattedPrice + " - " + event + " - " + date + "\"");
                financeToModify.setWrappingWidth(200);
                vbox.getChildren().addAll(financeToModify, grid);
                dialog.getDialogPane().setContent(vbox);
                
                Label priceLabel = new Label("Price");
                TextField priceField = new TextField();
                
                Label eventLabel = new Label("Event");
                TextField eventField = new TextField();
                
                Label dateLabel = new Label("Date");
                DatePicker dateField = new DatePicker();
                
                dateField.setEditable(false);
                
                grid.add(priceLabel, 0, 0);
                grid.add(priceField, 1, 0);
                
                grid.add(eventLabel, 0, 1);
                grid.add(eventField, 1, 1);
                
                grid.add(dateLabel, 0, 2);
                grid.add(dateField, 1, 2);
                
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
                Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.setDisable(true);
                
                BooleanBinding priceFieldBoolean = Bindings.createBooleanBinding(() -> {
                if(priceField.textProperty().getValue() != null || priceField.textProperty().getValue().trim().length() > 0) {
                    try {
                        Double priceValidation = Double.parseDouble(priceField.textProperty().getValue());
                    } catch(NumberFormatException e) {
                        return false;
                    }
                   return true;
               } else {
                    return false;
                }
               }, priceField.textProperty());
                
                BooleanBinding eventFieldBoolean = Bindings.createBooleanBinding(() -> {
                if(eventField.textProperty().getValue()!= null || eventField.textProperty().getValue().trim().length() > 2) {
                    return true;
                } else {
                    return false;
                }
               }, eventField.textProperty());
                
                BooleanBinding dateFieldBoolean = Bindings.createBooleanBinding(() -> {
                if(dateField.valueProperty().getValue() == null || dateField.valueProperty().getValue().toString().length() != 10) {
                    return false;
               } else {
                    return true;
                }
               }, dateField.valueProperty());
                confirmButton.disableProperty().bind(priceFieldBoolean.not()
                        .or(eventFieldBoolean.not()
                        .or(dateFieldBoolean.not()
                )));
                  
                dialog.setResultConverter((ButtonType dialogButton) -> {
                    if(dialogButton == confirmButtonType) {
                        Double newPrice = Double.parseDouble(priceField.getText());
                        String newEvent = eventField.getText();
                        String newDate = dateField.getValue().toString();
                        Finance finance = new Finance(id,newPrice,newEvent,newDate,user);
                        System.out.println(newPrice + " : " + newEvent);
                        System.out.println(finance);
                        if(daoService.updateFinance(finance) == true) {
                            List<FinanceRowObject> newList = new ArrayList<>();
                            for(FinanceRowObject f: financeTable) {
                                if(f.getId().getValue().equals(id)) {
                                    newList.add(new FinanceRowObject(id,newPrice,DaoService.formatPrice(newPrice),newEvent,newDate,user));
                                    System.out.println("added");
                                } else {
                                    newList.add(f);
                                    System.out.println("normal");
                                }
                            }
                            financeTable = FXCollections.observableArrayList(newList);
                            tableView.setItems(financeTable);
                            financesList = daoService.getAllFinances();
                            balanceText.setText("Balance: " + daoService.getBalance());
                            setNotification("Event modified succesfully", 5, "INFO");
                        }
                    }
                return null;
                });
                dialog.showAndWait();
            });
        }
    }
    
    public class RemoveButton extends Button {

        public RemoveButton(String id, String formattedPrice, Double price, String event, String date, String user) {
            super("Remove");
            setOnAction((action) -> {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("You're removing: ");
                Text financeToRemove = new Text(" \"" + formattedPrice + " - " + event + " - " + date + "\"");
                financeToRemove.setWrappingWidth(200);
                alert.getDialogPane().setContent(financeToRemove);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK) {
                    daoService.deleteFinance(id);
                    List<FinanceRowObject> newList = new ArrayList<>();
                    for(FinanceRowObject f: financeTable) {
                        if(f.getId().getValue().equals(id)) {
                            } else {
                                newList.add(f);
                            }
                            }
                            financeTable = FXCollections.observableArrayList(newList);
                            tableView.setItems(financeTable);
                            financesList = daoService.getAllFinances();
                            balanceText.setText("Balance: " + daoService.getBalance());
                            setNotification("Event removed succesfully!", 5, "INFO");
                } else {
                // go back
                }
            });
        }
    }
    
    public class eventText extends Text {
        public  eventText(String text) {
            super(text);
            wrappingWidthProperty().bind(tableView.widthProperty().multiply(0.635));
        }
    }

    public class FinanceRowObject {
        
        private final SimpleStringProperty id;
        private final SimpleDoubleProperty price;
        private final SimpleStringProperty formattedPrice;
        private final SimpleStringProperty date;
        private final SimpleObjectProperty event;
        private final SimpleStringProperty user;
        private final SimpleObjectProperty<ModifyButton> modifyButton;
        private final SimpleObjectProperty<RemoveButton> removeButton;

        public FinanceRowObject(String id, Double price, String formattedPrice, String event, String date, String user) {
            this.id = new SimpleStringProperty(id);
            this.formattedPrice = new SimpleStringProperty(formattedPrice);
            this.price = new SimpleDoubleProperty(price);
            this.event = new SimpleObjectProperty(new eventText(event));
            this.date = new SimpleStringProperty(date);
            this.modifyButton = new SimpleObjectProperty(new ModifyButton(id,formattedPrice, price, event, date, user));
            this.removeButton = new SimpleObjectProperty(new RemoveButton(id,formattedPrice, price, event, date, user));
            this.user = new SimpleStringProperty(user);
        }
        
        public StringProperty formattedPriceProperty() {
            return formattedPrice;
        }
        public StringProperty dateProperty() {
            return date;
        }
        
        public SimpleObjectProperty eventProperty() {
            return event;
        }
        
        public ObjectProperty modifyButtonProperty() {
            return modifyButton;
        }
        
        public ObjectProperty removeButtonProperty() {
            return removeButton;
        }

        public SimpleStringProperty getId() {
            return id;
        }

        public SimpleStringProperty getFormattedPrice() {
            return formattedPrice;
        }

        public SimpleStringProperty getDate() {
            return date;
        }

        public SimpleObjectProperty getEvent() {
            return event;
        }

        @Override
        public String toString() {
            return "FinanceRowObject{" + "id=" + id + ", price=" + price + ", formattedPrice=" + formattedPrice + ", date=" + date + ", event=" + event + ", user=" + user + ", modifyButton=" + modifyButton + ", removeButton=" + removeButton + '}';
        }

        public SimpleDoubleProperty getPrice() {
            return price;
        }

        public SimpleStringProperty getUser() {
            return user;
        }

        public SimpleObjectProperty<ModifyButton> getModifyButton() {
            return modifyButton;
        }

        public SimpleObjectProperty<RemoveButton> getRemoveButton() {
            return removeButton;
        }
        
        public void setPrice(String newPrice) {
            this.formattedPrice.set(newPrice);
        }
        
        public void setEvent(String newEvent) {
            this.event.set(newEvent);
        }
        
        public void setDate(String newDate) {
            this.date.set(newDate);
        }
    }
}


