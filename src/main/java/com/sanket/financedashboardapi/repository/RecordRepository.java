package com.sanket.financedashboardapi.repository;

import com.sanket.financedashboardapi.enums.TransactionType;
import com.sanket.financedashboardapi.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RecordRepository extends MongoRepository<Record, String> {

    Page<Record> findByIsDeletedFalse(Pageable pageable);

    Page<Record> findByIsDeletedFalseAndType(
            TransactionType type, Pageable pageable);

    Page<Record> findByIsDeletedFalseAndCategory(
            String category, Pageable pageable);

    Page<Record> findByIsDeletedFalseAndTypeAndCategory(
            TransactionType type, String category, Pageable pageable);

    Page<Record> findByIsDeletedFalseAndDateBetween(
            LocalDate from, LocalDate to, Pageable pageable);

    Page<Record> findByIsDeletedFalseAndTypeAndDateBetween(
            TransactionType type, LocalDate from, LocalDate to, Pageable pageable);

    Page<Record> findByIsDeletedFalseAndCategoryAndDateBetween(
            String category, LocalDate from, LocalDate to, Pageable pageable);

    Page<Record> findByIsDeletedFalseAndTypeAndCategoryAndDateBetween(
            TransactionType type, String category, LocalDate from, LocalDate to, Pageable pageable);

    Optional<Record> findByIdAndIsDeletedFalse(String id);
}