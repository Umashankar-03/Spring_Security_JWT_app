package com.sbss.project.controller;

import com.sbss.project.entity.Customer;
import com.sbss.project.repository.CustomerRepository;
import com.sbss.project.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CustomerRestController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome, This application is developed for Spring Security JWT implementation  ";
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginCheck(@RequestBody Customer c){

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(c.getUname(), c.getPwd());
        try {
            Authentication authentication = authenticationManager.authenticate(token);

            if(authentication.isAuthenticated()){
                String jwtToken = jwtService.generateToken(c.getUname());
                return  new ResponseEntity<>(jwtToken, HttpStatus.OK);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ResponseEntity<>("Invalid Credentials ", HttpStatus.BAD_REQUEST);

    }
    @PostMapping("/register")
    public String registerCustomer(@RequestBody Customer customer){

        String encodedPwd = passwordEncoder.encode(customer.getPwd());
        customer.setPwd(encodedPwd);
        customerRepository.save(customer);
        return "User registered";
}



}
