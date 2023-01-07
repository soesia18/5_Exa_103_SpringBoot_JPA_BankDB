package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.Account;
import at.kaindorf.bank.pojos.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Transactional
    @Modifying
    @Query("update Account a set a.balance = ?1 where a.accountId = ?2")
    void updateBalance(Double balance, Long accountId);

    int totalBalance (List<Long> accountIds);

    @Query("SELECT AVG (a.balance) FROM Account a WHERE a.balance < 0")
    Double getAverageBalanceOfNegativeBalanceAccounts ();
}