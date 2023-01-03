package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.GiroAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface GiroAccountRepository extends JpaRepository<GiroAccount, Long> {
    @Transactional
    @Modifying
    @Query("update GiroAccount g set g.balance = ?1 where g.accountId = ?2")
    void updateBalance(Double balance, Long accountId);
}