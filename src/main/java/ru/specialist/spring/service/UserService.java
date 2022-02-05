package ru.specialist.spring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.specialist.spring.entity.User;
import ru.specialist.spring.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getPosts().size();


        return user;
    }

}
