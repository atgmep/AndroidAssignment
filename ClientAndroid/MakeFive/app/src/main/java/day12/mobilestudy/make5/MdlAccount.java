package day12.mobilestudy.make5;

import com.google.gson.annotations.Expose;

public class MdlAccount {

    @Expose
    private String username;
    @Expose
    private Long point;

    public MdlAccount() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }


}
