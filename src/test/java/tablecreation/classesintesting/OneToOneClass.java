package tablecreation.classesintesting;

import annotations.*;

@Entity
@Table(name = "address")
public class OneToOneClass {

    @PrimaryKey
    @Column(name = "address_id")
    private Long address_id;

    @Column(name = "street")
    private String street;

    @OneToOne
    @MapsId
    private Person user;

}
