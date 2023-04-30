package edu.trucktrack.service;

import edu.trucktrack.entity.EmployeeEntity;
import edu.trucktrack.repository.jpa.EmployeeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {

	private final EmployeeJpaRepository employeeJpaRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		EmployeeEntity employee = employeeJpaRepository.getByEmail(email);
		return new User(employee.getEmail(), employee.getPassword(), employee.getRoles().stream().map(item -> new SimpleGrantedAuthority(item.getRole())).collect(
				Collectors.toList()));
	}
}
