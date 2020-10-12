package kz.iitu.adminservice.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Users {
    private List<User> users;

//    public Users() {
//        users = new ArrayList<>();
//    }

    public List<User> getUsers() {
        return users;
    }
}
