package kg.infocom.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by kbakytbekov on 13.03.2017.
 */
@Entity
@Table(name="service_log")
public class ServiceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username",nullable = false)
    private String user;
    @Column(name = "request")
    private String request;
    @Column(name = "response")
    private String response;
    @Column(name = "logdate")
    private Date logdate;
    @Column(name = "ipaddress")
    private String ipaddress;
    @Column(name = "method")
    private String method;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getLogdate() {
        return logdate;
    }

    public void setLogdate(Date logdate) {
        this.logdate = logdate;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    @Override
    public String toString() {
        return "ServiceLog{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                ", logdate=" + logdate +
                ", ipaddress='" + ipaddress + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

}
