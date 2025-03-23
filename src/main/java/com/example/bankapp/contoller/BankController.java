package com.example.bankapp.contoller;

import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public class BankController {


    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public  String dashboard(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
       model.addAttribute("account",account);
       return "dashboard";
    }

    @GetMapping("/register")
    public  String showRegistrationForm(){

        return "register";
    }

    @GetMapping
    public String registerAccount(@RequestParam String username,@RequestParam String password,Model model ){
        try{
            accountService.registerAccount(username,password);
            return "redirect:/login";
        }catch (RuntimeException e){
            model.addAttribute("error",e.getMessage());
            return "register";
        }

    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        accountService.deposit(account,amount);
        return "redirect:/dashboard";

    }

    @PostMapping("/withdraw")
    public  String withdraw(@RequestParam BigDecimal amount, Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        try {
            accountService.withdrow(account,amount);
        } catch (RuntimeException e) {
            model.addAttribute("error",e.getMessage());
            model.addAttribute("account",account);
            return "dashbord";
        }
        return "redirect:/dashbord";
    }

    @GetMapping("/transaction")
    public String transaction(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("transaction",accountService.getTransactionHistory(account));
        return "transaction";

    }

    @PostMapping("/transfer")
    public  String transferAmount(@RequestParam String toUsername,@RequestParam BigDecimal amount,Model model ){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account fromaccount = accountService.findAccountByUsername(username);
        try {
            accountService.transferAmount(fromaccount,toUsername,amount);
        } catch (RuntimeException e) {
            model.addAttribute("error",e.getMessage());
            model.addAttribute("account",fromaccount);
            return "dashbord";
        }

        return "redirect:/dashbord";
    }







}
