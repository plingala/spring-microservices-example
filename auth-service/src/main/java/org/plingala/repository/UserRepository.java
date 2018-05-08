package org.plingala.repository;

import java.util.List;

import org.plingala.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository { //extends CrudRepository<User, String> {

	public default User findByUserName(String username) {
		final List<String> roles = new java.util.ArrayList<>(1);
		roles.add("ROLES_USER");
		final User user = new User("test", "test", "Fname Lname", roles);
		return user;
	}
}
