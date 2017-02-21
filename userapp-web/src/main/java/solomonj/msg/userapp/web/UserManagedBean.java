package solomonj.msg.userapp.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.primefaces.event.RowEditEvent;

import solomonj.msg.appuser.common.exception.ServiceException;
import solomonj.msg.appuser.common.service.IUserService;
import solomonj.msg.userapp.ejb.service.util.SendEmail;
import solomonj.msg.userapp.ejb.service.util.ShowTime;
import solomonj.msg.userapp.jpa.model.Role;
import solomonj.msg.userapp.jpa.model.User;

/**
 * Managed bean for users.
 * 
 * @author Solomon Jozsef.
 *
 */
@Named("usermanagedbean")
@SessionScoped
public class UserManagedBean implements Serializable {

	private static final long serialVersionUID = -16296420798818231L;
	private IUserService userBean = null;
	private User user = new User();
	private boolean edit;
	private List<String> selectedRoles = new ArrayList<>();
	private List<User> allUsers = null;
	private List<User> allBadUsers = null;
	private String searchName = "";

	private IUserService getUserBean() {
		if (userBean == null) {
			try {
				InitialContext jndi = new InitialContext();
				userBean = (IUserService) jndi.lookup(IUserService.jndiNAME);
			} catch (NamingException e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						LoginManagedBean.getResourceBundleString("user.naming"), null));
			}
		}
		return userBean;
	}

	public List<String> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<String> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public void editUser(User user) {
		selectedRoles.clear();
		List<Role> roles = user.getRoles();
		for (Role r : roles) {
			selectedRoles.add(new Integer(r.getId()).toString());
		}

		this.user = user;
		edit = true;
	}

	public void cancelEdit() {

		this.user = new User();
		selectedRoles = new ArrayList<>();
		edit = false;
	}

	public void add() {

		user.setRoles(rolesToInt());
		insertUser(user);
		user = new User();
		selectedRoles = new ArrayList<>();

	}

	public void resetAdd() {
		System.out.println(user.getUsername());
		user = new User();
		selectedRoles = new ArrayList<>();
	}

	public void saveEdit() {

		user.setRoles(rolesToInt());
		updateUser(user);
		this.user = new User();
		selectedRoles = new ArrayList<>();
		edit = false;

	}

	public List<User> getAllUsers() {
		try {
			if(allUsers == null) {
				allUsers = getUserBean().searchUserByName(searchName);	
			}
			

		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
		}
		if (allUsers == null) {
			return new ArrayList<>();
		}
		return allUsers;
	}

	public List<User> getAllBadUsers() {
		try {
			allBadUsers = getUserBean().getAllBadUsers();
			return allBadUsers;
		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
		}
		if (allBadUsers == null) {
			return new ArrayList<>();
		}
		return allBadUsers;
	}

	public void insertUser(User user) {
		try {
			getUserBean().insertUser(user);
		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
		}
	}

	public void deleteUserById(User user) {
		try {
			getUserBean().deleteUserById(user);
		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
		}

	}

	public void updateUser(User user) {
		try {
			getUserBean().updateUser(user);
		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
		}

	}

	public User getUser() {
		return user;
	}

	public boolean isEdit() {
		return edit;
	}

	public void delete(User user) {

		if (edit) {
			cancelEdit();
		}
		deleteUserById(user);

	}

	private List<Role> rolesToInt() {
		List<Role> roles = new ArrayList<>();
		for (String i : selectedRoles) {
			roles.add(new Role(Integer.parseInt(i)));
		}
		return roles;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public void clearFilter() {
		searchName = "";
	}

	public User login(String n, String p) {
		try {
			return getUserBean().login(n, p);
		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
			return new User();
		}
	}

	public void sendEmail(User u) {
		try {			
//			ShowTime time = new ShowTime();
//			time.checkBorrowing();
			SendEmail.sendEmail(u.getEmail(), "szocscsillamaria@gmail.com");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					LoginManagedBean.getResourceBundleString("web.user.emailsent"), null));
		} catch (ServiceException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					LoginManagedBean.getResourceBundleString(e.getMessage()), null));
		}
	}
	
	public void onEdit(RowEditEvent event) {
		
		updateUser((User) event.getObject());
	

		FacesMessage msg = new FacesMessage("User Edited", ((User) event.getObject()).getUsername());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edit Cancelled", ((User) event.getObject()).getUsername());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
