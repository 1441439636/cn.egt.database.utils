package dao;

import entity.User;

/**
 * Created by 14414 on 2017/4/19.
 */
public interface UserDao {
    void add(User user);
    User getByCode(String codeVal);

    void update(User user);

    User getByUser(User userVal);
}
