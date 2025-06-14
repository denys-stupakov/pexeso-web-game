package sk.tuke.kp.kpi.pexeso.service;

import sk.tuke.kp.kpi.pexeso.entity.User;

public interface UserService {
    void addUser(User user);
    String getPassword(String username);
    int checkLogin(String username);
    void reset();
}