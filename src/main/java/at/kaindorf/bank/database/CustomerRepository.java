package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.Account;
import at.kaindorf.bank.pojos.Customer;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.address.city = ?1")
    List<Customer> findCustomersByCity (String city);

    @Query("SELECT c from Customer c JOIN c.accounts a GROUP BY c ORDER BY SUM(a.balance) DESC LIMIT 1")
    Customer findCustomerByHighestBalance ();

    @Query("SELECT c from Customer c JOIN c.accounts a GROUP BY c ORDER BY SUM(a.balance) ASC LIMIT 1")
    Customer findCustomerByLowestBalance ();

    @Query("SELECT (COUNT(c) * 1.0) / ((SELECT COUNT(ca) FROM Customer ca)) FROM Customer c WHERE c.gender = 'm'")
    double getPercentageOfMaleCustomer ();

    @Query("SELECT (COUNT(c) * 1.0) / ((SELECT COUNT(ca) FROM Customer ca)) FROM Customer c WHERE c.gender = 'w'")
    double getPercentageOfFemaleCustomer ();
}