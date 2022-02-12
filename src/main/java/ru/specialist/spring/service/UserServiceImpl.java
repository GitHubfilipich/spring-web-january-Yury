package ru.specialist.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.specialist.spring.entity.Role;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.RoleRepository;
import ru.specialist.spring.repository.UserRepository;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getPosts().size();
        return user;
    }

    @Override
    public void create(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()){
            throw new EntityExistsException();
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(List.of(roleRepository.findByName(Role.USER).orElseThrow()));
        user.setDtCreated(LocalDateTime.now());
        user.setIsActive(true);

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getRoles().size();
        return user;
    }

//    15:42
}
