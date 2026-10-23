package com.oasisinfobyte.library.repository;

import com.oasisinfobyte.library.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReturnedFalse();
    List<Loan> findByMemberId(Long memberId);
}
