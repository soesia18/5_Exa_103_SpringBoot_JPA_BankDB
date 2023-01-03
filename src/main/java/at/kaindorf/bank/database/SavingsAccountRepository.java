package at.kaindorf.bank.database;

import at.kaindorf.bank.pojos.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    @Transactional
    @Modifying
    @Query("update SavingsAccount s set s.balance = ?1 where s.accountId = ?2")
    void updateBalance(Double balance, Long accountId);
}