package com.oreilly.security.acl.entities;

import java.math.BigInteger;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
@Data
@Entity
//@Table(name="ACL_SID")
public class AclSid {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(scale=100)
	BigInteger id;
	@Column(unique=true)
	boolean principal;
	@Column(unique=true)
	String sid;

}
