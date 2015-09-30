package edu.ntu.vison.smallfarmer01.model;

/**
 * Created by Vison on 2015/9/30.
 */
public class UserData {

    class User {
        String last_name;
        String first_name;
    }
    String user_avatar;
    User user;

    public UserData() {
        this.user = new User();
    }



    // Getter
    public String getUserName() {
        return user.last_name+user.first_name;
    }

    public String getUserImgUrl() {
        return user_avatar;
    }

}
