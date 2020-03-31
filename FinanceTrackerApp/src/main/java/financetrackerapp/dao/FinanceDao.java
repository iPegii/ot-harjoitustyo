package financetrackerapp.dao;

import financetrackerapp.domain.Finance;
import java.util.List;

public interface FinanceDao {
    public Finance create(Finance finance);
    public List<Finance> getAll();
}
