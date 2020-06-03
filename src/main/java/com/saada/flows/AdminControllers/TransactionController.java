package com.saada.flows.AdminControllers;

import java.util.Optional;

// import com.saada.flows.models.SessionHistory;
import com.saada.flows.services.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/v1/admin/session/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {
    @Autowired
    private SessionService sessionService;

    private final boolean isAdmin = false;
    @GetMapping(path = "sessions")
    public String getSessions(@RequestParam Optional<Integer> page){
        return sessionService.listSessions(isAdmin,page).toString();
    }
}