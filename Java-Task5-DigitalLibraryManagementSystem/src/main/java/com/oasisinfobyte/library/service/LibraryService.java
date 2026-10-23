package com.oasisinfobyte.library.service;

import com.oasisinfobyte.library.exception.ResourceNotFoundException;
import com.oasisinfobyte.library.model.Book;
import com.oasisinfobyte.library.model.Loan;
import com.oasisinfobyte.library.model.Member;
import com.oasisinfobyte.library.repository.BookRepository;
import com.oasisinfobyte.library.repository.LoanRepository;
import com.oasisinfobyte.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LibraryService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    public LibraryService(BookRepository bookRepository, MemberRepository memberRepository,
                          LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    public List<Book> getBooks() { return bookRepository.findAll(); }

    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.isBlank()) return getBooks();
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Book addBook(Book book) {
        if (bookRepository.existsByIsbnIgnoreCase(book.getIsbn())) {
            throw new IllegalArgumentException("A book with this ISBN already exists.");
        }
        if (book.getAvailableCopies() == null) book.setAvailableCopies(book.getTotalCopies());
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updated) {
        Book current = getBook(id);
        int alreadyIssued = current.getTotalCopies() - current.getAvailableCopies();
        if (updated.getTotalCopies() < alreadyIssued) {
            throw new IllegalArgumentException("Total copies cannot be less than the number of currently issued copies.");
        }
        current.setTitle(updated.getTitle());
        current.setAuthor(updated.getAuthor());
        current.setIsbn(updated.getIsbn());
        current.setTotalCopies(updated.getTotalCopies());
        current.setAvailableCopies(updated.getTotalCopies() - alreadyIssued);
        return bookRepository.save(current);
    }

    public void deleteBook(Long id) {
        Book book = getBook(id);
        if (loanRepository.findAll().stream().anyMatch(loan ->
                loan.getBook().getId().equals(id) && !loan.isReturned())) {
            throw new IllegalArgumentException("Cannot delete a book while it is currently issued.");
        }
        bookRepository.delete(book);
    }

    public List<Member> getMembers() { return memberRepository.findAll(); }

    public Member addMember(Member member) {
        if (memberRepository.existsByEmailIgnoreCase(member.getEmail())) {
            throw new IllegalArgumentException("A member with this email already exists.");
        }
        return memberRepository.save(member);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + id));
    }

    @Transactional
    public Loan issueBook(Long bookId, Long memberId) {
        Book book = getBook(bookId);
        Member member = getMember(memberId);
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalArgumentException("No copies of this book are currently available.");
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        return loanRepository.save(new Loan(book, member));
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
        if (loan.isReturned()) {
            throw new IllegalArgumentException("This loan has already been returned.");
        }
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        Book book = loan.getBook();
        book.setAvailableCopies(Math.min(book.getTotalCopies(), book.getAvailableCopies() + 1));
        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    public List<Loan> getLoans() { return loanRepository.findAll(); }
    public List<Loan> getActiveLoans() { return loanRepository.findByReturnedFalse(); }
    public List<Loan> getMemberLoans(Long memberId) {
        getMember(memberId);
        return loanRepository.findByMemberId(memberId);
    }
}
