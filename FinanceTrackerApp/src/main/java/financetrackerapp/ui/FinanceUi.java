/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;


import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.domain.Finance;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
        
public class FinanceUi extends Application {
    
    public void start(Stage finance) {
        
        finance.setTitle("Finance Tracker");
        BorderPane page = new BorderPane();
        // top bar seup
        BorderPane topBar = new BorderPane();
        page.setTop(topBar);
        Button button = new Button("Press this");
        Label label = new Label("Welcome to Finance Tracker");
        topBar.setRight(button);
        topBar.setPadding(new Insets(10));
        topBar.setLeft(label);
        label.setFont(new Font("Arial", 17));
        
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
        Button clearButton = new Button("Clear");
        formButtons.getChildren().add(clearButton);
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
        
        TableColumn<String, Finance> event = new TableColumn<>("Event");
        event.setCellValueFactory(new PropertyValueFactory<>("event"));
        TableColumn<String, Finance> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        tableView.getColumns().add(price);
        tableView.getColumns().add(event);
        tableView.getColumns().add(date);
        
        FinanceDao financeDao = new FinanceDaoReader("finances.txt");
        Finance test = new Finance(100, "Bought nice tea", "22.10.2019");
        financeDao.create(test);
        Finance test2 = new Finance(9001, "Bought energy drink", "23.10.2019");
        financeDao.create(test2);
        Finance test3 = new Finance(1337, "Bought water", "24.10.2019");
        financeDao.create(test3);
        for(Finance f: financeDao.getAll()) {
            tableView.getItems().add(new Finance(f.getPrice(),f.getEvent(), f.getDate()));
        }
        
        financeStats.setCenter(tableView);
        financeStats.setTop(balance);
        financeStats.setMargin(balance,new Insets(10));
        
 
        Scene layout = new Scene(page);
        finance.setScene(layout);
        finance.setWidth(1600);
        finance.setHeight(800);
        finance.show();
    }
}
