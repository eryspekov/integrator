package kg.infocom.model;

import javax.persistence.*;

/**
 * Created by eryspekov on 16.08.16.
 */
@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "org_id", nullable = false)
    private Long org_Id;

    @Column(name = "org_name")
    private String org_name;



}
