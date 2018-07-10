package entity;

/**
 * Created by 14414 on 2017/4/27.
 */
public class Admin {
    private String adminName;
    private String password;

    public Admin() {
    }

    public Admin(String adminName, String password) {
        this.adminName = adminName;
        this.password = password;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    @Override
    public String toString() {
        return "admin  adminName="+adminName+"   password="+password;
    }
}
