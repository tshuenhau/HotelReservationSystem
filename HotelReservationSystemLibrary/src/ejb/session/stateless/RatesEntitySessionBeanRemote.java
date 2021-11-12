/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Rates;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author tshuenhau
 */
@Remote
public interface RatesEntitySessionBeanRemote {
    
    public List<Rates> retrieveAllRates();

    public Rates createNewRate(Rates rate);
}
