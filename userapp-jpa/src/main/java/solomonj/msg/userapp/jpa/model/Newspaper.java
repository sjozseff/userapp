package solomonj.msg.userapp.jpa.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;


@Entity
@DiscriminatorValue("newspaper")
public class Newspaper extends Publication implements Serializable{
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	@OneToMany
	@JoinColumn(name = "publication_id")
	List<Article> articles;
	
	public Newspaper() {	
		articles = new ArrayList<>();
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
	
	
}