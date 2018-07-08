package hsim.checkpoint.setting.session;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
public class ValidationSessionInfo {

    private String token;
    private String ipAddress;
    private Date createDate;

    public ValidationSessionInfo(HttpServletRequest req, String t) {
        this.token = t;
        this.createDate = new Date();
        this.ipAddress = req.getRemoteAddr();
    }

}
