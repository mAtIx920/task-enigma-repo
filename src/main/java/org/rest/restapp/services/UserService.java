package org.rest.restapp.services;

import org.rest.restapp.models.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsers(UserDTO userFilter, Integer page, Integer size);

    UserDTO getUser(Integer id);

    void addUser(UserDTO user);

    void removeUser(Integer id);

    void removeUsers();

    boolean usersNotExist(List<Integer> keys);

}
