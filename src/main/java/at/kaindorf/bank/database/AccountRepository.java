package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.Account;
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

    @Query("SELECT SUM (a.balance) FROM Account a WHERE a.accountId IN ?1")
    int totalSum (List<Long> accountIds);
}