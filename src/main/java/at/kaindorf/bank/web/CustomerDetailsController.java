package at.kaindorf.bank.web;

import at.kaindorf.bank.database.GiroAccountRepository;
import at.kaindorf.bank.database.SavingsAccountRepository;
import at.kaindorf.bank.pojos.Account;
import at.kaindorf.bank.pojos.Customer;
import at.kaindorf.bank.pojos.GiroAccount;
import at.kaindorf.bank.pojos.SavingsAccount;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Exa_103_SpringBoot_JPA_BankDB<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 29. Dezember 2022<br>
 * <b>Time:</b> 16:08<br>
 */

@Controller
@Slf4j
@RequestMapping("/detail")
@SessionAttributes(
        {"filterLastname", "customer"}
)
public class CustomerDetailsController {

    private final GiroAccountRepository giroAccountRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    public CustomerDetailsController(GiroAccountRepository giroAccountRepository, SavingsAccountRepository savingsAccountRepository) {
        this.giroAccountRepository = giroAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
    }

    @ModelAttribute("customer")
    public Customer customer() {
        return new Customer();
    }

    @ModelAttribute("amount")
    public int amount() {
        return 0;
    }

    @GetMapping
    public ModelAndView showDetailCustomer(Model model, @RequestParam("radCustomer") Customer customer) {
        log.debug("GET request to /detail");
        model.addAttribute("customer", customer);
        return new ModelAndView("customerDetailsView");
    }

    @PostMapping(value = "/depositWithdraw/{id}", params = "action=-")
    public ModelAndView deposit(@RequestParam int amount,
                                @PathVariable("id") int accountId,
                                @SessionAttribute("customer") Customer customer) {
        log.debug("POST request to /detail/depositWithdraw");

        Account account = getAccountFromCustomer(customer, accountId);

        account.setBalance(account.getBalance() - amount);

        if (account instanceof GiroAccount) {
            giroAccountRepository.updateBalance(account.getBalance(), account.getAccountId());
        } else if (account instanceof SavingsAccount) {
            savingsAccountRepository.updateBalance(account.getBalance(), account.getAccountId());
        }

        return new ModelAndView("customerDetailsView");
    }

    @PostMapping(value = "/depositWithdraw/{id}", params = "action=+")
    public ModelAndView withdraw(@RequestParam int amount,
                                 @PathVariable("id") int accountId,
                                 @SessionAttribute("customer") Customer customer) {
        log.debug("POST request to /detail/depositWithdraw");

        Account account = getAccountFromCustomer(customer, accountId);

        account.setBalance(account.getBalance() + amount);

        if (account instanceof GiroAccount) {
            giroAccountRepository.updateBalance(account.getBalance(), account.getAccountId());
        } else if (account instanceof SavingsAccount) {
            savingsAccountRepository.updateBalance(account.getBalance(), account.getAccountId());
        }

        return new ModelAndView("customerDetailsView");
    }

    private Account getAccountFromCustomer (Customer customer, int id) {
        return customer.getAccounts().stream().filter(account -> account.getAccountId() == id).findFirst().get();
    }
}
