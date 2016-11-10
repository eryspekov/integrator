package kg.infocom.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by eryspekov on 10.10.16.
 */
@Entity
@Table(name = "consumer_service")
public class ConsumerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "method", nullable = false)
    private String method;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ws_id", nullable = false)
    private WebServiceType webServiceType;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "consumer_producer",
            joinColumns = @JoinColumn(name = "cs_id"),
            inverseJoinColumns = @JoinColumn(name = "ps_id"))
    private Set<ProducerService> producerServices;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "consumerService")
    private Set<ConsumerArguments> arguments;

    /*@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "consumer_arg",
            joinColumns = @JoinColumn(name = "cs_id"),
            inverseJoinColumns = @JoinColumn(name = "arg_id"))
    private Set<Argument> arguments;*/


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "consumer_element",
            joinColumns = @JoinColumn(name = "cs_id"),
            inverseJoinColumns = @JoinColumn(name = "element_id"))
    private Set<Element> elements;

    public Set<Element> getElements() {
        return elements;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }


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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public WebServiceType getWebServiceType() {
        return webServiceType;
    }

    public void setWebServiceType(WebServiceType webServiceType) {
        this.webServiceType = webServiceType;
    }

    public Set<ProducerService> getProducerServices() {
        return producerServices;
    }

    public void setProducerServices(Set<ProducerService> producerServices) {
        this.producerServices = producerServices;
    }

    public Set<ConsumerArguments> getArguments() {
        return arguments;
    }

    public void setArguments(Set<ConsumerArguments> arguments) {
        this.arguments = arguments;
    }
}
