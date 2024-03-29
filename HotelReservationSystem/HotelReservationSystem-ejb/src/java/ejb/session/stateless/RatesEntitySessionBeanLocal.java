/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Rates;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteRateException;
import util.exception.RateAlreadyExistException;
import util.exception.RatesNotFoundException;

/**
 *
 * @author tshuenhau
 */
@Local
public interface RatesEntitySessionBeanLocal {

    public List<Rates> retrieveAllRates();

    public Rates createNewRate(Rates rate) throws RateAlreadyExistException;

    public Rates retrievesRatesByRateID(Long rateID) throws RatesNotFoundException;

    public void deleteRate(Long rateID) throws RatesNotFoundException, DeleteRateException;
    
}
