package dao;

import domain.Finance;
import java.util.List;

public interface FinanceDao {
    public void create(Finance finance);
    public List<Finance> getAll();
}
