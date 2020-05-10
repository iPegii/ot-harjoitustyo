/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.ui;

import financetrackerapp.domain.Finance;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author iPegii
 */
public class FinanceCellObject {
        
    private final SimpleStringProperty id;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty formattedPrice;
    private final SimpleStringProperty date;
    private final SimpleObjectProperty event;
    private final SimpleStringProperty user;
    private final SimpleObjectProperty<ModifyButton> modifyButton;
    private final SimpleObjectProperty<RemoveButton> removeButton;
    private final FinanceTableUi financeTableUi;

    public FinanceCellObject(FinanceTableUi financeTableUi, Finance finance) {
        this.financeTableUi = financeTableUi;
        this.id = new SimpleStringProperty(finance.getId());
        this.formattedPrice = new SimpleStringProperty(finance.getFormattedPrice());
        this.price = new SimpleDoubleProperty(finance.getPrice());
        this.event = new SimpleObjectProperty(new EventText(finance.getEvent()).getText());
        this.date = new SimpleStringProperty(finance.getDate());
        this.modifyButton = new SimpleObjectProperty(new ModifyButton(finance));
        this.removeButton = new SimpleObjectProperty(new RemoveButton(finance));
        this.user = new SimpleStringProperty(finance.getUser());
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

    public SimpleStringProperty getId() {
        return id;
    }

    public SimpleDoubleProperty getPrice() {
        return price;
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
        
    public ObjectProperty removeButtonProperty() {
        return removeButton;
    }

    @Override
    public String toString() {
        return "FinanceCellObject{" + "id=" + id + ", price=" + price + ", formattedPrice=" + formattedPrice + ", date=" + date + ", event=" + event + ", user=" + user + ", modifyButton=" + modifyButton + ", removeButton=" + removeButton + '}';
    }
        
    class ModifyButton extends Button {

        public ModifyButton(Finance finance) {
            super("Modify");
            setOnAction((ActionEvent action) -> {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Finance modification");
                dialog.setHeaderText("You're modifying: ");
                VBox vbox = new VBox();
                GridPane grid = new GridPane();
            
                Text financeToModify = new Text("\"" + formattedPrice.getValue() + " - " + event.getValue() + " - " + date.getValue() + "\"");
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
                
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
                Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.setDisable(true);
                
                BooleanBinding priceFieldBoolean = Bindings.createBooleanBinding(() -> {
                    if (priceField.textProperty().getValue() != null || priceField.textProperty().getValue().trim().length() > 0) {
                        try {
                            Double priceValidation = Double.parseDouble(priceField.textProperty().getValue());
                        } catch (NumberFormatException e) {
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                }, priceField.textProperty());
                
                BooleanBinding eventFieldBoolean = Bindings.createBooleanBinding(() -> {
                    if (eventField.textProperty().getValue() != null && eventField.textProperty().getValue().trim().length() > 2) {
                        System.out.println(eventField.textProperty().getValue().trim().length());
                        return true;
                    } else {
                        return false;
                    }
                }, eventField.textProperty());
                
                BooleanBinding dateFieldBoolean = Bindings.createBooleanBinding(() -> {
                    if (dateField.valueProperty().getValue() == null || dateField.valueProperty().getValue().toString().length() != 10) {
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
                    if (dialogButton == confirmButtonType) {
                        Double newPrice = Double.parseDouble(priceField.getText());
                        String newEvent = eventField.getText();
                        String newDate = dateField.getValue().toString();
                        Finance nf = new Finance(finance.getId(), newPrice, newEvent, newDate, finance.getUser());
                        if (financeTableUi.getDaoService().updateFinance(nf) == true) {
                            List<FinanceCellObject> newList = new ArrayList<>();
                            for (FinanceCellObject f: financeTableUi.getFinanceTable()) {
                                if (f.getId().getValue().equals(id.getValue())) {
                                    Finance newFinance = new Finance(id.getValue(), newPrice, newEvent, newDate, user.getValue());
                                    newList.add(new FinanceCellObject(financeTableUi, newFinance));
                                } else {
                                    newList.add(f);
                                }
                            }
                            financeTableUi.setFinanceTable(FXCollections.observableArrayList(newList));
                            financeTableUi.getTableView().setItems(financeTableUi.getFinanceTable());
                            financeTableUi.setFinancesList(financeTableUi.getDaoService().getAllFinances());
                            financeTableUi.updateBalance();
                            financeTableUi.setNotification("Event modified succesfully", 5, "INFO");
                        }
                    }
                    return null;
                });
                dialog.showAndWait();
            });
        }
    }
    
    class RemoveButton extends Button {

        public RemoveButton(Finance finance) {
            super("Remove");
            setOnAction((action) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("You're removing: ");
                Text financeToRemove = new Text(" \"" + formattedPrice.getValue() + " - " + event.getValue().toString() + " - " + date.getValue() + "\"");
                financeToRemove.setWrappingWidth(200);
                alert.getDialogPane().setContent(financeToRemove);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    financeTableUi.getDaoService().deleteFinance(id.getValue());
                    FinanceCellObject savedObject = null;
                    for (FinanceCellObject f: financeTableUi.getFinanceTable()) {
                        if (f.getId().getValue().equals(id.getValue())) {
                            savedObject = f;
                        }
                    }
                    ObservableList<FinanceCellObject> list = financeTableUi.getFinanceTable();
                    list.remove(savedObject);
                    financeTableUi.setFinanceTable(list);
                    financeTableUi.setFinancesList(financeTableUi.getDaoService().getAllFinances());
                    financeTableUi.updateBalance();
                    financeTableUi.setNotification("Event removed succesfully!", 5, "INFO");
                }
            });
        }
    }
    
    public class EventText extends Text {
        public  EventText(String text) {
            super(text);
            wrappingWidthProperty().bind(financeTableUi.getTableView().widthProperty().multiply(0.635));
        }
    }
}
