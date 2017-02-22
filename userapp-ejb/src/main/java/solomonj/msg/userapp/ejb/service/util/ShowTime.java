package solomonj.msg.userapp.ejb.service.util;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import solomonj.msg.appuser.common.exception.ServiceException;
import solomonj.msg.userapp.ejb.repository.IBorrowingRepository;
import solomonj.msg.userapp.ejb.repository.IUserRepository;
import solomonj.msg.userapp.ejb.repository.bean.UserRepositoryBean;
import solomonj.msg.userapp.ejb.repository.exception.RepositoryException;
import solomonj.msg.userapp.jpa.model.User;

/**
 * Timer class, for: Email sending;
 * 
 * @author Szocs Csilla
 *
 */
@Singleton
@Startup
public class ShowTime {
	@EJB
	private IUserRepository userRepositoryBean;

	@Schedule(second = "*/86400", minute = "*/1440", hour = "*", persistent = false)
	public void checkBorrowing() throws ServiceException {
		try {
			List<User> asd = userRepositoryBean.getAllBadUsers();
//			System.out.println("check borrowing method");
//			System.out.println("number of users " + asd.size());
	//		SendEmail.sendEmail("szocscsillamaria@gmail.com", "szocscsillamaria@gmail.com");
		
		} catch (RepositoryException e) {

			e.printStackTrace();
		}

	}

	@PostConstruct
	public void startUp() {
		try {
			checkBorrowing();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
