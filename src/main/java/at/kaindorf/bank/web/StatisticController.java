package at.kaindorf.bank.web;

import at.kaindorf.bank.database.AccountRepository;
import at.kaindorf.bank.database.CustomerRepository;
import at.kaindorf.bank.pojos.Account;
import at.kaindorf.bank.pojos.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Exa_103_SpringBoot_JPA_BankDB<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 29. Dezember 2022<br>
 * <b>Time:</b> 16:09<br>
 */

@Controller
@Slf4j
@RequestMapping("/statistic")
@SessionAttributes({
        "avgBalanceOfNegativeBalanceAccounts",
        "highestBalanceCustomer",
        "lowestBalanceCustomer",
        "percentageOfMaleCustomer",
        "percentageOfFemaleCustomer"
})
public class StatisticController {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public StatisticController(CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }


    @ModelAttribute("customerByCity")
    public List<Customer> customerByCity () {
        return new ArrayList<>();
    }

    @ModelAttribute("avgBalanceOfNegativeBalanceAccounts")
    public String avgBalanceOfNegativeBalanceAccounts() {
        return Account.numberFormat.format(0);
    }

    @ModelAttribute("highestBalanceCustomer")
    public Customer highestBalanceCustomer() {
        return new Customer();
    }

    @ModelAttribute("lowestBalanceCustomer")
    public Customer lowestBalanceCustomer() {
        return new Customer();
    }

    @ModelAttribute("percentageOfMaleCustomer")
    public double percentageOfMaleCustomer() {
        return 0.0;
    }

    @ModelAttribute("percentageOfFemaleCustomer")
    public double percentageOfFemaleCustomer() {
        return 0.0;
    }

    @GetMapping
    public ModelAndView showStatisticView (Model model) {
        log.debug("GET request to /statistic");

        Customer highestBalanceCustomer = customerRepository.findCustomerByHighestBalance();
        Customer lowestBalanceCustomer = customerRepository.findCustomerByLowestBalance();

        model.addAttribute("highestBalanceCustomer", highestBalanceCustomer);
        model.addAttribute("lowestBalanceCustomer", lowestBalanceCustomer);

        double percentageOfMaleCustomer = customerRepository.getPercentageOfMaleCustomer();
        double percentageOfFemaleCustomer = customerRepository.getPercentageOfFemaleCustomer();

        model.addAttribute("percentageOfMaleCustomer", Customer.percentFormatter.format(percentageOfMaleCustomer));
        model.addAttribute("percentageOfFemaleCustomer", Customer.percentFormatter.format(percentageOfFemaleCustomer));


        Double avgBalanceOfNegativeBalanceAccounts = accountRepository.getAverageBalanceOfNegativeBalanceAccounts();

        if (avgBalanceOfNegativeBalanceAccounts != null) {
            model.addAttribute("avgBalanceOfNegativeBalanceAccounts", Account.numberFormat.format(avgBalanceOfNegativeBalanceAccounts));
        } else {
            model.addAttribute("avgBalanceOfNegativeBalanceAccounts", "There are no existing negative balance Accounts!");
        }



        return new ModelAndView("statisticView");
    }

    @PostMapping("/city")
    public ModelAndView showCityCustomer (Model model, @RequestParam("city") String city) {
        log.debug("POST request to /statistic/city");


        model.addAttribute("customerByCity", customerRepository.findCustomersByCity(city));


        return new ModelAndView("statisticView");
    }
}
