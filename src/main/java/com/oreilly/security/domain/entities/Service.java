package com.oreilly.security.domain.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

@Entity
public class Service {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY,generator="my_generator")
	@Column(name="SERVICE_ID")
	@TableGenerator(pkColumnName="id",
					name="my_generator",
					initialValue=100
			)
	private Long id;
	private String description;
	public Service() {
		// TODO Auto-generated constructor stub
	}
	public Service(String description) {
		super();
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	
}
