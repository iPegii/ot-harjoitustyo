
package financetrackerapp;

import financetrackerapp.dao.FinanceDaoReader;
import financetrackerapp.domain.Finance;
import java.util.List;
import javafx.application.Application;
import financetrackerapp.ui.FinanceUi;


public class Main {
    public static void main(String[] args) {
        Application.launch(FinanceUi.class);
    }
}
