/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateful;

import classes.Allocation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author tshuenhau
 */
@Local
public interface AllocationSessionBeanLocal {


    public List<Allocation> generateReport(Date date);

    public void allocateRooms(Date date);
    
}
