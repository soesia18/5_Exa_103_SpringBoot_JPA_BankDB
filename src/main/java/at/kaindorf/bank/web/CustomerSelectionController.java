package at.kaindorf.bank.web;

import at.kaindorf.bank.database.CustomerRepository;
import at.kaindorf.bank.pojos.Customer;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <h3>Created by IntelliJ IDEA.</h3><br>
 * <b>Project:</b> Exa_103_SpringBoot_JPA_BankDB<br>
 * <b>User:</b> Simon Schoeggler<br>
 * <b>Date:</b> 29. Dezember 2022<br>
 * <b>Time:</b> 16:08<br>
 */

@Controller
@Slf4j
@RequestMapping("/")
@SessionAttributes(
        {"filterLastname"}
)
public class CustomerSelectionController {

    private final CustomerRepository customerRepository;
    private List<Customer> customerList;

    public CustomerSelectionController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void initDb () {
        customerList = customerRepository.findAll();
        customerList.sort(Comparator.comparing(Customer::getLastname).thenComparing(Customer::getFirstname));
    }

    @ModelAttribute("customerAmount")
    public int customerAmount () {
        return 0;
    }

    @ModelAttribute("filterLastname")
    public String filterLastname () {
        return "";
    }

    @ModelAttribute("customerList")
    public List<Customer> customerList () {
        return new ArrayList<>();
    }
    @GetMapping
    public ModelAndView showCustomerSelectPage (Model model) {
        log.debug("Get request to /");

        model.addAttribute("filterLastname", "");
        model.addAttribute("customerAmount", customerList.size());
        model.addAttribute("customerList", customerList);

        return new ModelAndView("customerSelectionView");
    }

    @GetMapping(value = "/filter")
    public ModelAndView showFilteredList (@RequestParam(required = false) String filterLastname,
                                    Model model,
                                          @SessionAttribute("filterLastname") String sessionFilterLastname) {
        log.debug("Get request to /filter");

        if (filterLastname == null) {
           filterLastname = sessionFilterLastname;
        }

        if (filterLastname.isBlank()) {
            return new ModelAndView("redirect:/");
        }

        model.addAttribute("filterLastname", filterLastname);

        String finalFilterLastname = filterLastname;
        List<Customer> filteredCustomer = customerList
                .stream()
                .filter(customer -> customer.getLastname().toLowerCase().contains(finalFilterLastname.toLowerCase()))
                .toList();

        model.addAttribute("customerAmount", filteredCustomer.size());
        model.addAttribute("customerList",filteredCustomer);

        return new ModelAndView("customerSelectionView");
    }
}
