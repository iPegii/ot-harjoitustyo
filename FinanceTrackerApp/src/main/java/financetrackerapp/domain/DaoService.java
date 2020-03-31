/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package financetrackerapp.domain;

import financetrackerapp.dao.FinanceDao;
import financetrackerapp.dao.FinanceDaoReader;
import java.util.List;

/**
 *
 * @author iPegii
 */
public class DaoService {
    
    private FinanceDaoReader financeDao;
    
    public DaoService() {
        financeDao = new FinanceDaoReader("finances.txt");
    }
    
    
    public List<Finance> getAll() {
        return financeDao.getAll();
    }
    
}
