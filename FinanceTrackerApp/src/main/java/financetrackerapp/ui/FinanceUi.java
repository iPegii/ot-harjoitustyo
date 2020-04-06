/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;


import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.dao.UserDao;
import financetrackerapp.dao.UserDaoReader;
import financetrackerapp.domain.DaoService;
import financetrackerapp.domain.Finance;
import financetrackerapp.domain.User;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        
public class FinanceUi extends Application {
    
    private DaoService daoService;
    
    @Override
    public void init() {
        try {
            Properties prop=new Properties();
            prop.load(new FileInputStream("config.properties"));
            String userFile= prop.getProperty("userFile");
            String financeFile = prop.getProperty("financeFile");
            UserDao userDao = new UserDaoReader(userFile);
            FinanceDao financeDao = new FinanceDaoReader(financeFile);
            this.daoService = new DaoService(userDao, financeDao);
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Files missing");
        } catch (IOException e) {    
            System.out.println("Error: Failed accessing file");
        } catch (Exception e) {    
            System.out.println("Error: Something unexpected happened" + e.getMessage());
        }
    }
    
    @Override
    public void start(Stage finance) {
        daoService.login("Pegi");
        String username = daoService.loggedIn().getUsername();
        User userStatus = daoService.loggedIn();
        finance.setTitle("Finance Tracker");
        BorderPane overlay = new BorderPane();
        BorderPane page = new BorderPane();
        LoginUi loginUi = new LoginUi(daoService, page);
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
        overlay.setCenter(loginUi.getLoginScreen());
        });
        
        System.out.println(userStatus);
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
        TextField dateField = new TextField();
        createForm.add(dateText, 0, 2);
        createForm.add(dateField, 1, 2);
        
        Button createButton = new Button("Create");
        formButtons.getChildren().add(createButton);
        EventHandler<ActionEvent> createEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            Finance financeObject = new Finance(userStatus.getUsername(),Integer.valueOf(priceField.getText()), eventField.getText(), dateField.getText());
            daoService.createFinance(financeObject);
            }
        };
        createButton.setOnAction(createEvent);
        
        Button clearButton = new Button("Clear");
        formButtons.getChildren().add(clearButton);
        EventHandler<ActionEvent> clearEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            priceField.clear();
            eventField.clear();
            dateField.clear();
        }
        };
        clearButton.setOnAction(clearEvent);
        formButtons.setSpacing(55.0);
        
        topBar.setBottom(createForm);
        // display finance stats
        BorderPane financeStats = new BorderPane();
        page.setCenter(financeStats);
        
        Label balance = new Label("Balance: 1500");
        balance.setFont( new Font("Arial", 20));

        TableView tableView = new TableView();
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
        daoService.createUser(new User("Pegi", "pegii"));
        
        Finance test = new Finance(username,100, "Bought nice tea", "22.10.2019");
        daoService.createFinance(test);
        Finance test2 = new Finance(username,9001, "Bought energy drink", "23.10.2019");
        daoService.createFinance(test2);
        Finance test3 = new Finance(username,1337, "Bought water", "24.10.2019");
        daoService.createFinance(test3);

        
        for(Finance f: daoService.getAll()) {
            tableView.getItems().add(new Finance(f.getUsername(),f.getPrice(),f.getEvent(), f.getDate()));
        }
        
        financeStats.setCenter(tableView);
        financeStats.setTop(balance);
        financeStats.setMargin(balance,new Insets(10));
        
        overlay.setCenter(page);
        Scene financeLayout = new Scene(overlay);
        finance.setScene(financeLayout);
        finance.setWidth(1600);
        finance.setHeight(800);
        finance.show();
    }
}
