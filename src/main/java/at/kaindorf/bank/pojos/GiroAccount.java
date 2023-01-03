package at.kaindorf.bank.pojos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@DiscriminatorValue("GIRO")
public class GiroAccount extends Account {
    private Double overdraft;
    private Float debitInterest;
    private Float creditInterest;
}