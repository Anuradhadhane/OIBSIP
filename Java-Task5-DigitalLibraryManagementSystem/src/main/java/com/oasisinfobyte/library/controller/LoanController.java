package com.oasisinfobyte.library.controller;

import com.oasisinfobyte.library.model.Loan;
import com.oasisinfobyte.library.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
    private final LibraryService service;
    public LoanController(LibraryService service) { this.service = service; }

    @GetMapping
    public List<Loan> getLoans() { return service.getLoans(); }

    @GetMapping("/active")
    public List<Loan> getActiveLoans() { return service.getActiveLoans(); }

    @GetMapping("/member/{memberId}")
    public List<Loan> getMemberLoans(@PathVariable Long memberId) {
        return service.getMemberLoans(memberId);
    }

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public Loan issueBook(@RequestBody Map<String, Long> request) {
        Long bookId = request.get("bookId");
        Long memberId = request.get("memberId");
        if (bookId == null || memberId == null) {
            throw new IllegalArgumentException("Both bookId and memberId are required.");
        }
        return service.issueBook(bookId, memberId);
    }

    @PutMapping("/{loanId}/return")
    public Loan returnBook(@PathVariable Long loanId) {
        return service.returnBook(loanId);
    }
}
