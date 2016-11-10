package kg.infocom.model;

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

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "cs_id", nullable = false)
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

    /*public ProducerService getProducerService() {
        return producerService;
    }

    public void setProducerService(ProducerService producerService) {
        this.producerService = producerService;
    }*/

    public Argument getArgument() {
        return argument;
    }

    public void setArgument(Argument argument) {
        this.argument = argument;
    }

}
