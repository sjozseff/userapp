package solomonj.msg.userapp.jpa.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("book")
public class Book extends Publication {

	@Transient
	private static final long serialVersionUID = 6162720738158711916L;
	
	@ManyToMany
	@JoinTable(name = "publications_authors", joinColumns = @JoinColumn(name = "publication_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
	private List<Author> bAuthors;
	
	public Book() {
		
		bAuthors = new ArrayList<>();
	}

	public List<Author> getbAuthors() {
		return bAuthors;
	}

	public void setbAuthors(List<Author> bAuthors) {
		this.bAuthors = bAuthors;
	}
	
}
