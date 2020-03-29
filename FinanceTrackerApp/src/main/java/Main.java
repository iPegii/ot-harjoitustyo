
import dao.FinanceDaoService;
import domain.Finance;
import java.util.List;
import javafx.application.Application;
import ui.FinanceUi;


public class Main {
    public static void main(String[] args) {
        /*
        FinanceDaoService financeService = new FinanceDaoService("finances.txt");
        Finance finance = new Finance(50, "test day", "29.3.2020");
        financeService.create(finance);
        List<Finance> wow = financeService.getAll();
        for(Finance f: wow) {
            System.out.println(f);
        }
        */
        Application.launch(FinanceUi.class);
    }
}
