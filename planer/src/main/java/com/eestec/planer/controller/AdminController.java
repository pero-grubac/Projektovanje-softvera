package com.eestec.planer.controller;

import com.eestec.planer.controller.util.KorisnikRequest;
import com.eestec.planer.controller.util.LoginForm;
import com.eestec.planer.dto.AdminDTO;
import com.eestec.planer.dto.LogDTO;
import com.eestec.planer.dto.LogDTOMessage;
import com.eestec.planer.dto.PorukaLoga;

import com.eestec.planer.service.LogService;
import com.eestec.planer.service.implementations.AdminServiceImpl;
import com.eestec.planer.service.implementations.EmailServiceImpl;
import com.eestec.planer.service.implementations.JwtService;
import com.eestec.planer.service.implementations.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class AdminController {
    private final AdminServiceImpl adminService; // Use AdminServiceImpl

    @Autowired
    private final LogService logService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    public AdminController(AdminServiceImpl adminService, LogServiceImpl logService) {
        this.adminService = adminService;
        this.logService = logService;
    }


//    @GetMapping("/getall")
//    //  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
//        List<AdminDTO> admins = adminService.getAllAdmins();
//        return ResponseEntity.ok(admins);
//    }


//    @PostMapping("/new")
//    //  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<Void> createAdmin(@RequestBody LoginForm loginForm) {
//        logger.info(loginForm.getUsername());
//        AdminDTO admin = adminService.createAdmin(loginForm);
//        if (admin != null)
//            return ResponseEntity.ok().build();
//        else return ResponseEntity.notFound().build();
//    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> updateAdmin(@RequestBody KorisnikRequest admin) {
        AdminDTO updatedAdmin = adminService.updateAdmin(admin);
        if (updatedAdmin != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/email")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> hello() {
        emailService.email("","pero", "eplaner", "test");
        return ResponseEntity.ok().build();
    }
    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getLozinka()));            if (authentication.isAuthenticated()) {
                // User authenticated successfully
                logService.create(PorukaLoga.USPJESNA_PRIJAVA.getValue(),loginForm.getUsername());
                return ResponseEntity.ok(jwtService.generateToken(loginForm.getUsername()));
            } else {
                // User not authenticated
                logService.create(PorukaLoga.NEUSPJESNA_PRIJAVA.getValue(),loginForm.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user " + loginForm.getUsername());
            }
        } catch (AuthenticationException e) {
            // Handle authentication exception (e.g., user not found)
            logService.create(PorukaLoga.NEUSPJESNA_PRIJAVA.getValue(),loginForm.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed for user " + loginForm.getUsername());
        }
    }

    @GetMapping("/logs")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<LogDTOMessage>> getLogs() {
        return new ResponseEntity<>(logService.getLogsForAdmin(), HttpStatus.OK);
    }

    @GetMapping("/logs/{subject}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<LogDTOMessage>> getLogsBySubject(@PathVariable String subject) {
        return new ResponseEntity<>(logService.getLogsForAdminBySubject(subject), HttpStatus.OK);
    }


//    @DeleteMapping("/delete/{id}")
//    //  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
//        boolean isOk = adminService.deleteAdmin(id);
//        if (isOk)
//            return ResponseEntity.noContent().build();
//        else return ResponseEntity.notFound().build();
//    }

//    @PostMapping("/authenticate")
//    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getLozinka(), loginForm.getUsername()));
//        if (authentication.isAuthenticated())
//            return jwtService.generateToken(loginForm.getUsername());
//        else
//            throw new UsernameNotFoundException("invalid user request !");
//
//    }

}
