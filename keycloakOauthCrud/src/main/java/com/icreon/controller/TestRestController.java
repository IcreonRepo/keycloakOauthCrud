package com.icreon.controller;


import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icreon.configuration.SecurityContextUtils;

@RestController
@RequestMapping("api/v1/test")
public class TestRestController {

    //private static final Logger LOG = LoggerFactory.getLogger(TestRestController.class);

    @GetMapping(path = "/username")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<String> getAuthorizedUserName() {
        return ResponseEntity.ok(SecurityContextUtils.getUserName());
    }

    @GetMapping(path = "/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Set<String>> getAuthorizedUserRoles() {
        return ResponseEntity.ok(SecurityContextUtils.getUserRoles());
    }

}
