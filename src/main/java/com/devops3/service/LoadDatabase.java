package com.devops3.service;


import com.devops3.model.ERole;
import com.devops3.model.Role;
import com.devops3.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private List<User> initialUsers = new ArrayList<>();

    private Map<String, Role> roleMap = new HashMap<>();

    public void initializeRoles() {


        roleMap.put("admin", new Role(ERole.ROLE_ADMIN));
        roleMap.put("moderator", new Role(ERole.ROLE_MODERATOR));
        roleMap.put("user", new Role(ERole.ROLE_USER));
    }

    public void initializeUsers() {

        if (roleMap.isEmpty()) {
            initializeRoles();
        }

        User patrick = new User("patrick", "plresquillo@convergeict.com", "1234");
        patrick.addRoles(roleMap.get("user"));

        User yong = new User("yong", "iyslee@convergeict.com", "1234");
        yong.addRoles(roleMap.get("moderator"));

        User lyn = new User("lyn", "srbreamon@convergeict.com", "1234");
        lyn.addRoles(roleMap.get("admin"));

        User devops3 = new User("devops3", "devops3@convergeict.com", "Yeswecan123!");
        devops3.setRoles(new HashSet<>(roleMap.values()));


        initialUsers.add(patrick);
        initialUsers.add(yong);
        initialUsers.add(lyn);
        initialUsers.add(devops3);

    }

    @Bean
    CommandLineRunner initRoleDatabase(RoleRepository repository) {
        initializeRoles();
        return args -> {
            for (Map.Entry<String, Role> entry : roleMap.entrySet()) {
                log.info("Preloading " + repository.save(entry.getValue()));
            }
        };
    }

    @Bean
    CommandLineRunner initUserDatabase(UserRepository repository) {
        initializeUsers();
        return args -> {
            for (User u : initialUsers) {
                log.info("Preloading " + repository.save(u));
            }
        };
    }


}
