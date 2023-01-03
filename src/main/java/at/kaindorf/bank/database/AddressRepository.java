package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}