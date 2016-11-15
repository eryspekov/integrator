package kg.infocom.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created by Admin on 11/10/2016.
 */
@Entity
@Table(name = "consumer_arg")
public class ConsumerArguments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "cs_id", nullable = false)
    @JsonBackReference
    private ProducerService consumerService;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "arg_id", nullable = false)
    private Argument argument;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Argument getArgument() {
        return argument;
    }

    public void setArgument(Argument argument) {
        this.argument = argument;
    }

    public ProducerService getConsumerService() {
        return consumerService;
    }

    public void setConsumerService(ProducerService consumerService) {
        this.consumerService = consumerService;
    }
}
