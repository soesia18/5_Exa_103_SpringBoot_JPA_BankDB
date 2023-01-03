package at.kaindorf.bank.pojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    private String streetname;
    @Column(name = "street_number")
    private String streetNumber;
    @Column(name = "zip_code")
    private String zipCode;
    private String city;

    public String getAddress () {
        return streetname + " " + streetNumber + ", " + zipCode + " " + city;
    }
}