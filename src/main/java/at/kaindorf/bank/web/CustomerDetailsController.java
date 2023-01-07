package at.kaindorf.bank.web;

import at.kaindorf.bank.database.AccountRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.NumberFormat;
import java.util.Optional;

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
    private final AccountRepository accountRepository;

    public CustomerDetailsController(GiroAccountRepository giroAccountRepository, SavingsAccountRepository savingsAccountRepository, AccountRepository accountRepository) {
        this.giroAccountRepository = giroAccountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.accountRepository = accountRepository;
    }

    @ModelAttribute("customer")
    public Customer customer() {
        return new Customer();
    }

    @ModelAttribute("amount")
    public int amount() {
        return 0;
    }

    @ModelAttribute("totalAmount")
    public String totalAmount() {
        return Account.numberFormat.format(0);
    }

    @GetMapping
    public ModelAndView showDetailCustomer(Model model, @RequestParam("radCustomer") Customer customer) {
        log.debug("GET request to /detail");
        model.addAttribute("customer", customer);
        updateTotalBalance(model, customer);
        return new ModelAndView("customerDetailsView");
    }

    @PostMapping(value = "/depositWithdraw/{id}", params = "action=+")
    public ModelAndView deposit(Model model,
                                @RequestParam int amount,
                                @PathVariable("id") int accountId,
                                @SessionAttribute("customer") Customer customer) {
        log.debug("POST request to /detail/depositWithdraw");

        Account account = getAccountFromCustomer(customer, accountId);
        account.setBalance(account.getBalance() + amount);
        accountRepository.updateBalance(account.getBalance(), account.getAccountId());


        updateTotalBalance(model, customer);

        /*if (account instanceof GiroAccount) {
            giroAccountRepository.updateBalance(account.getBalance(), account.getAccountId());
        } else if (account instanceof SavingsAccount) {
            savingsAccountRepository.updateBalance(account.getBalance(), account.getAccountId());
        }*/

        return new ModelAndView("customerDetailsView");
    }

    @PostMapping(value = "/depositWithdraw/{id}", params = "action=-")
    public ModelAndView withdraw(Model model,
                                 @RequestParam int amount,
                                 @PathVariable("id") int accountId,
                                 @SessionAttribute("customer") Customer customer) {
        log.debug("POST request to /detail/depositWithdraw");

        ModelAndView modelAndView = new ModelAndView("customerDetailsView");

        Account account = getAccountFromCustomer(customer, accountId);

        if (account instanceof GiroAccount && account.getBalance() - amount < (-((GiroAccount) account).getOverdraft())) {
            modelAndView.addObject("GiroAccountError", "Too low balance to withdraw: Overdraft-limit: " + ((GiroAccount) account).getOverdraft());
        } else if (account instanceof SavingsAccount && account.getBalance() - amount < 0) {
            modelAndView.addObject("SavingsAccountError", "Too low balance to withdraw " +
                    Account.numberFormat.format((account.getBalance() - amount)));
        } else {
            account.setBalance(account.getBalance() - amount);
            accountRepository.updateBalance(account.getBalance(), account.getAccountId());
        }


        updateTotalBalance(model, customer);


        return modelAndView;
    }

    private void updateTotalBalance(Model model, Customer customer) {
        model
                .addAttribute("totalAmount",
                        Account.numberFormat
                                .format(accountRepository
                                        .totalBalance(customer
                                                .getAccounts()
                                                .stream()
                                                .map(Account::getAccountId)
                                                .toList())));
    }

    private Account getAccountFromCustomer(Customer customer, int id) {
        return customer.getAccounts().stream().filter(account -> account.getAccountId() == id).findFirst().get();
    }
}
