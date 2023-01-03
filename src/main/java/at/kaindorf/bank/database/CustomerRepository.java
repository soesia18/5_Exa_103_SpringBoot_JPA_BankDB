package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}