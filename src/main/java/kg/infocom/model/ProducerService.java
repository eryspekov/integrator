package kg.infocom.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by eryspekov on 17.08.16.
 */
@Entity
@Table(name = "producer_service")
public class ProducerService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "with_param", nullable = false)
    private Boolean with_param;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "ws_id", nullable = false)
    private WebServiceType webServiceType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "producerService")
    @JsonManagedReference
    private Set<ProducerArguments> arguments;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "producer_element",
            joinColumns = @JoinColumn(name = "ps_id"),
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

    public Boolean getWith_param() {
        return with_param;
    }

    public void setWith_param(Boolean with_param) {
        this.with_param = with_param;
    }

    public Set<ProducerArguments> getArguments() {
        return arguments;
    }

    public void setArguments(Set<ProducerArguments> arguments) {
        this.arguments = arguments;
    }
}
