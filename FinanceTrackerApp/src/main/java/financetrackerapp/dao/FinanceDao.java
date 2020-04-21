package financetrackerapp.dao;

import financetrackerapp.domain.Finance;
import java.util.List;

public interface FinanceDao {
    public String create(Finance finance);
    public List<Finance> getAll();
    public String[] getFileName();
}
