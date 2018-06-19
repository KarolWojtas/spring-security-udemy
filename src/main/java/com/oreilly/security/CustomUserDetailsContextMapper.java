package com.oreilly.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.repositories.AutoUserRepository;
@Component
public class CustomUserDetailsContextMapper implements UserDetailsContextMapper{
	private AutoUserRepository autoUserRepository;
	
	@Autowired
	public CustomUserDetailsContextMapper(AutoUserRepository autoUserRepository) {
		super();
		this.autoUserRepository = autoUserRepository;
	}

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
			Collection<? extends GrantedAuthority> authorities) {
		AutoUser user = new AutoUser();
		user.setFirstName(ctx.getStringAttribute("givenName"));
		user.setEmail(ctx.getStringAttribute("mail"));
		user.setLastName(ctx.getStringAttribute("sn"));
		user.setUsername(username);
		user.setAuthorities(authorities);
		return user;
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		AutoUser autoUser = (AutoUser) user;
		ctx.setAttributeValue("givenName", autoUser.getFirstName());
		ctx.setAttributeValue("sn", autoUser.getLastName());
		ctx.setAttributeValue("uid", autoUser.getUsername());
		ctx.setAttributeValue("password", autoUser.getPassword());
		ctx.setAttributeValue("mail", autoUser.getEmail());
		
	}

}
