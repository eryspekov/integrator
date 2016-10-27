package kg.infocom.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by eryspekov on 13.10.16.
 */
@Entity
@Table(name = "argument")
public class Argument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_static")
    private Boolean isStatic;

    @Column(name = "arg_value")
    private String value;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "arguments")
    private Set<ConsumerService> consumerServices;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatic() {
        return isStatic;
    }

    public void setStatic(Boolean aStatic) {
        isStatic = aStatic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<ConsumerService> getConsumerServices() {
        return consumerServices;
    }

    public void setConsumerServices(Set<ConsumerService> consumerServices) {
        this.consumerServices = consumerServices;
    }

}
