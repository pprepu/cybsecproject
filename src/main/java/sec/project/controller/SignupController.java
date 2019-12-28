package sec.project.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sec.project.domain.Account;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;
import sec.project.service.AccountService;
import sec.project.service.SignupService;

@Controller
public class SignupController {
    
    //ArrayList<String> testi = new ArrayList<>();
    
    private int maxSize = 4;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private SignupService signupService;
/*
    @Autowired
    private SignupRepository signupRepository;
    */
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        /*
        List<Signup> currentlySignedUp = signupRepository.findAll();
        if (currentlySignedUp.size() >= maxSize) {
            model.addAttribute("eventFull", "Event is unfortunately full, Better luck next time!");
        } else {
            model.addAttribute("eventFull", "There's still room!");
        }
        */
        return "form";
    }
/*
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        List<Signup> currentlySignedUp = signupRepository.findAll();
        if (currentlySignedUp.size() >= maxSize) {
            return "redirect:/form";
        }
        signupRepository.save(new Signup(name, address));
        return "redirect:/done";
    }
    */
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address) throws SQLException {
        /*
        List<Signup> currentlySignedUp = signupRepository.findAll();
        if (currentlySignedUp.size() >= maxSize) {
            return "redirect:/form";
        }
    */
        signupService.addSignup(name, address);
        return "redirect:/done";
    }
    
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public String createNewAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "done";
        }
        
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return "redirect:/done";
        }
        
        // currently not doing this :( 
        //String password = account.getPassword();
        //account.setPassword(passwordEncoder.encode(password));
        
        accountRepository.save(account);
        return "redirect:/secret";
    }
    
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String loadDone(Model model, @ModelAttribute Account account) throws Exception {
        //model.addAttribute("list", signupRepository.findAll());
        model.addAttribute("list", signupService.listAll());
        return "done";
    }
    
    @RequestMapping(value = "/secret", method = RequestMethod.GET)
    public String loadSecret(Model model) {
        model.addAttribute("currentUser", "Logged in as: " + accountService.loggedInUser());
        return "secret";
    }
    
    @RequestMapping("/")
    public String defaultMapping() {
        return "redirect:/form";
    }
    
    

}
