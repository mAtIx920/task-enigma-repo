package org.rest.restapp.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.rest.restapp.controllers.exceptions.NotFoundException;
import org.rest.restapp.controllers.exceptions.ResourceAlreadyExistsException;
import org.rest.restapp.models.UserDTO;
import org.rest.restapp.services.listeners.UserRemoveEmitter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ConcurrentHashMap<Integer, UserDTO> users = new ConcurrentHashMap<>();
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<UserDTO> getUsers(UserDTO userFilter, Integer page, Integer size) {
        return users.values().stream()
                .filter(user ->
                        (userFilter.getId() == null || userFilter.getId().equals(user.getId())) &&
                        (userFilter.getName() == null || userFilter.getName().equals(user.getName())) &&
                        (userFilter.getLastName() == null || userFilter.getLastName().equals(user.getLastName())) &&
                        (userFilter.getEmail() == null || userFilter.getEmail().equals(user.getEmail())))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());

    }

    public boolean usersNotExist(List<Integer> keys) {
        for (Integer key : keys) {
            if (!users.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public UserDTO getUser(Integer id) {
        if (!this.users.containsKey(id)) {
            throw new NotFoundException("User with id: " + id + " not found");
        }

        return users.get(id);
    }

    @Override
    public void addUser(UserDTO user) {
        if (checkExists(user.getEmail())) {
            throw new ResourceAlreadyExistsException("User with email: " + user.getEmail() + " already exists");
        }
        users.put(this.users.size() + 1, user);
    }

    @Override
    public void removeUser(Integer id) {
        if (!this.users.containsKey(id)) {
            throw new NotFoundException("User with id: " + id + " not found");
        }
        this.users.remove(id);
        publishRemovedEmitter(List.of(id));
    }

    @Override
    public void removeUsers() {
        List<Integer> userIds = this.users.keySet().stream().toList();
        this.users.clear();
        publishRemovedEmitter(userIds);
    }

    private void publishRemovedEmitter(List<Integer> userIds) {
        applicationEventPublisher.publishEvent(new UserRemoveEmitter(userIds));
    }

    private boolean checkExists(String email) {
        return this.users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @PostConstruct
    private void init() {
        UserDTO user1 = UserDTO.builder()
                .id(0)
                .name("Mateusz")
                .lastName("Rosiński")
                .email("matirosinski02@gmail.com").build();

        UserDTO user2 = UserDTO.builder()
                .id(1)
                .name("Jan")
                .lastName("Kowalski")
                .email("jankowalskigmail.com").build();

        UserDTO user3 = UserDTO.builder()
                .id(2)
                .name("monika")
                .lastName("Nowak")
                .email("monikaNowak@gmail.com").build();

        this.users.put(0, user1);
        this.users.put(1, user2);
        this.users.put(2, user3);
    }
}
