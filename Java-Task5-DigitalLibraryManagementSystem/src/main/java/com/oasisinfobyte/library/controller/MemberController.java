package com.oasisinfobyte.library.controller;

import com.oasisinfobyte.library.model.Member;
import com.oasisinfobyte.library.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final LibraryService service;
    public MemberController(LibraryService service) { this.service = service; }

    @GetMapping
    public List<Member> getMembers() { return service.getMembers(); }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable Long id) { return service.getMember(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Member addMember(@Valid @RequestBody Member member) { return service.addMember(member); }
}
