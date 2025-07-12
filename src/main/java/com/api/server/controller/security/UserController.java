package com.api.server.controller.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.api.server.dto.security.RegisteredUser;
import com.api.server.dto.security.SaveUser;
import com.api.server.dto.security.ChangePassword;
import com.api.server.service.auth.AuthenticationService;
import com.api.server.service.security.UserService;
import com.api.server.util.UserPdfExporter;
import com.api.server.util.UserExcelExporter;
import com.api.server.util.UserCsvExporter;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired 
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<RegisteredUser>> listAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        try {
            Page<RegisteredUser> users = userService.findAll(role, status, pageable);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPDF(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        try {
            Page<RegisteredUser> usersPage = userService.findAll(role, status, Pageable.unpaged());
            List<RegisteredUser> users = usersPage.getContent();
            
            UserPdfExporter exporter = new UserPdfExporter();
            byte[] pdfBytes = exporter.export(users);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "usuarios.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        try {
            Page<RegisteredUser> usersPage = userService.findAll(role, status, Pageable.unpaged());
            List<RegisteredUser> users = usersPage.getContent();
            
            UserExcelExporter exporter = new UserExcelExporter();
            byte[] excelBytes = exporter.export(users);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("filename", "usuarios.xlsx");
            
            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportToCSV(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String status) {
        try {
            Page<RegisteredUser> usersPage = userService.findAll(role, status, Pageable.unpaged());
            List<RegisteredUser> users = usersPage.getContent();
            
            UserCsvExporter exporter = new UserCsvExporter();
            byte[] csvBytes = exporter.export(users);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("filename", "usuarios.csv");
            
            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser) {
        RegisteredUser registeredUser = authenticationService.registerOneUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisteredUser> getUser(@PathVariable Long id) {
        RegisteredUser user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("/change-password")
    public ResponseEntity<RegisteredUser> changeOwnPassword(@RequestBody @Valid ChangePassword user) {
        RegisteredUser updatedUser = userService.changeOwnPassword(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<RegisteredUser> changePassword(@PathVariable Long id, @RequestBody @Valid ChangePassword user) {
        RegisteredUser updatedUser = userService.changePassword(id, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<RegisteredUser> disableUser(@PathVariable Long id) {
        RegisteredUser updatedUser = userService.disableUser(id);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisteredUser> updateUser(@PathVariable Long id, @RequestBody @Valid SaveUser user) {
        RegisteredUser updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
    }
}
