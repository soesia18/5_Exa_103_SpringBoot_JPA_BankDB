package at.kaindorf.bank.pojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Exa_103_SpringBoot_JPA_BankDB<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 29. Dezember 2022<br>
 * <b>Time:</b> 16:12<br>
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue
    @Column(name = "customer_id")
    private Long customerId;

    private String firstname;
    private String lastname;
    private Long customerNumber;

    private LocalDate birthdate;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToMany
    @JoinTable(name = "customer_account",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private List<Account> accounts = new ArrayList<>();

    public String getName () {
        return lastname + ", " + firstname;
    }

    public String getCustomer () {
        return getName() + " (" + gender + ") " + birthdate;
    }

    public List<GiroAccount> getGiroAccounts () {
        return accounts
                .stream()
                .filter(account -> account instanceof GiroAccount)
                .map(account -> (GiroAccount) account)
                .collect(Collectors.toList());
    }

    public List<SavingsAccount> getSavingsAccounts () {
        return accounts
                .stream()
                .filter(account -> account instanceof SavingsAccount)
                .map(account -> (SavingsAccount) account)
                .collect(Collectors.toList());
    }

    public double getTotalBalance () {
        return accounts
                .stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }
}
