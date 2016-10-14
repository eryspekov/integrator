package kg.infocom.model;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by eryspekov on 17.08.16.
 */
@Entity
@Table(name = "producer_service")
@Transactional
public class ProducerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "ws_id", nullable = false)
    private WebServiceType webServiceType;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "producer_arg",
            joinColumns = @JoinColumn(name = "ps_id"),
            inverseJoinColumns = @JoinColumn(name = "arg_id"))
    private Set<Argument> arguments;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "producer_element",
            joinColumns = @JoinColumn(name = "ps_id"),
            inverseJoinColumns = @JoinColumn(name = "element_id"))
    //@LazyCollection(LazyCollectionOption.TRUE)
    private List<Element> elements;


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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebServiceType getWebServiceType() {
        return webServiceType;
    }

    public void setWebServiceType(WebServiceType webServiceType) {
        this.webServiceType = webServiceType;
    }

    public Set<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(Set<Argument> arguments) {
        this.arguments = arguments;
    }

}
