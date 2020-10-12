package kz.iitu.adminservice.Controllers;

import kz.iitu.adminservice.Models.User;
import kz.iitu.adminservice.Models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/list")
    public User[] getAllUsers() {
        ResponseEntity<User[]> response =
                restTemplate.getForEntity(
                        "http://user-service/api/v1/users/list",
                        User[].class);
        User[] users = response.getBody();

        return users;
    }


    @GetMapping
    public User getUserById(@RequestParam("userId") Long userId) {
        User user = restTemplate.getForObject("http://user-service/api/v1/users/"+userId, User.class);

        return user;
    }
}
