package at.kaindorf.bank.pojos;

import jakarta.persistence.*;
import lombok.*;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Exa_103_SpringBoot_JPA_BankDB<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 29. Dezember 2022<br>
 * <b>Time:</b> 16:08<br>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@NamedQuery(name = "Account.totalBalance",query = "SELECT SUM (a.balance) FROM Account a WHERE a.accountId IN ?1")
public abstract class Account {

    public static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    @Column(name = "account_number")
    private Long accountNumber;
    private Double balance;

    @ToString.Exclude
    @ManyToMany(mappedBy = "accounts")
    private List<Customer> customerList = new ArrayList<>();

    public String getFormattedBalance () {
        return numberFormat.format(balance);
    }
}
