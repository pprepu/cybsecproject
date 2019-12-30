package sec.project.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
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
import sec.project.domain.EventFood;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.EventFoodRepository;
import sec.project.service.AccountService;
import sec.project.service.SignupService;

@Controller
public class DefaultController {
    
    private final int maxSize = 7;
    
    private boolean isNotFull = true;
    
    @Autowired
    HttpSession session;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private SignupService signupService;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private EventFoodRepository eventFoodRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        
        List<Signup> currentlySignedUp = signupService.listSignups();
        if (currentlySignedUp.size() >= maxSize) {
            model.addAttribute("eventFull", "The event is unfortunately full, Better luck next time!");
            isNotFull = false;
        } else {
            model.addAttribute("eventFull", "There's still room!");
        }
        
        model.addAttribute("isNotFull", isNotFull);
        
        return "form";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address){
        
        /* 1.1, the code below should be added, so that direct POST-requests couldn't 
        be used to add additional signups to the page after max size has been reached .
        List<Signup> currentlySignedUp = signupService.listSignups();
        if (currentlySignedUp.size() >= maxSize) {
            return "redirect:/form";
        }
        */
        signupService.addSignup(name, address);
        session.setAttribute("signedUp", "done");
        return "redirect:/done";
   
    }
    
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public String createNewAccount(@Valid @ModelAttribute Account account, BindingResult bindingResult) {
        
        /* 1.1, code below should be added to prevent using direct POST-requests to add new users 
        when the person hasn't signed up.
        if (session.getAttribute("signedUp") == null) {
            return "error";
        }
        */
        if( bindingResult.hasErrors()) {
            return "done";
        }
        
        if (accountRepository.findByUsername(account.getUsername()) != null) {
            return "redirect:/done";
        }
        
        /* 1.4, first part to fixing unencrypted passwords
        These two lines below should be added to this method (use bcrypt for encypting passwords)
        String password = account.getPassword();
        account.setPassword(passwordEncoder.encode(password));
        */
        accountRepository.save(account);
        return "redirect:/login";
    }
    
    @RequestMapping(value = "/done", method = RequestMethod.GET)
    public String loadDone(Model model, @ModelAttribute Account account) {

        
        /* 1.1 One way to check authorization for the signups page - cookies and http sessions
        if (session.getAttribute("signedUp") == null) {
            return "error";
        }
        */
        model.addAttribute("list", signupService.listSignups());
        return "done";
    }
    
    @RequestMapping(value = "/secret", method = RequestMethod.GET)
    public String loadSecret(Model model) {
        model.addAttribute("currentUser", "Logged in as: " + accountService.loggedInUser());
        model.addAttribute("list", eventFoodRepository.findAll());
        return "secret";
    }
    
    @RequestMapping(value = "/secret", method = RequestMethod.POST)
    public String createFoodItem(@RequestParam String name) {

        eventFoodRepository.save(new EventFood(name));
        return "redirect:/secret";
    }
    
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }
    
}
