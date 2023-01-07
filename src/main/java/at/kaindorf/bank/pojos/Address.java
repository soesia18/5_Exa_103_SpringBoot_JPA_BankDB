package at.kaindorf.bank.pojos;

import jakarta.persistence.*;
import lombok.*;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Exa_103_SpringBoot_JPA_BankDB<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 29. Dezember 2022<br>
 * <b>Time:</b> 16:08<br>
 */

@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @Column(length = 100)
    private String streetname;
    @Column(name = "street_number", length = 10)
    private String streetNumber;
    @Column(name = "zip_code", length = 10)
    private String zipCode;
    private String city;

    public String getAddress () {
        return streetname + " " + streetNumber + ", " + zipCode + " " + city;
    }
}