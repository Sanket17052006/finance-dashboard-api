package com.sanket.financedashboardapi.service;

import com.sanket.financedashboardapi.dto.request.CreateRecordRequest;
import com.sanket.financedashboardapi.dto.request.UpdateRecordRequest;
import com.sanket.financedashboardapi.dto.response.RecordResponse;
import com.sanket.financedashboardapi.enums.TransactionType;
import com.sanket.financedashboardapi.exception.ResourceNotFoundException;
import com.sanket.financedashboardapi.model.Record;
import com.sanket.financedashboardapi.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public RecordResponse create(CreateRecordRequest request, String createdBy) {
        Record record = Record.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory().toLowerCase().trim())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(createdBy)
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(recordRepository.save(record));
    }

    public Page<RecordResponse> getAll(
            TransactionType type,
            String category,
            LocalDate from,
            LocalDate to,
            Pageable pageable) {

        boolean hasType = type != null;
        boolean hasCategory = category != null && !category.isBlank();

        // Fix 1: handle from-only and to-only cases
        // Fix 2: add one day to 'to' so upper bound is inclusive
        LocalDate effectiveFrom = from != null ? from : LocalDate.of(2000, 1, 1);
        LocalDate effectiveTo = to != null ? to.plusDays(1) : LocalDate.now().plusDays(1);
        boolean hasDateFilter = from != null || to != null;

        if (hasType && hasCategory && hasDateFilter) {
            return recordRepository
                    .findByIsDeletedFalseAndTypeAndCategoryAndDateBetween(
                            type, category.toLowerCase().trim(),
                            effectiveFrom, effectiveTo, pageable)
                    .map(this::toResponse);
        }
        if (hasType && hasDateFilter) {
            return recordRepository
                    .findByIsDeletedFalseAndTypeAndDateBetween(
                            type, effectiveFrom, effectiveTo, pageable)
                    .map(this::toResponse);
        }
        if (hasCategory && hasDateFilter) {
            return recordRepository
                    .findByIsDeletedFalseAndCategoryAndDateBetween(
                            category.toLowerCase().trim(),
                            effectiveFrom, effectiveTo, pageable)
                    .map(this::toResponse);
        }
        if (hasDateFilter) {
            return recordRepository
                    .findByIsDeletedFalseAndDateBetween(
                            effectiveFrom, effectiveTo, pageable)
                    .map(this::toResponse);
        }
        if (hasType && hasCategory) {
            return recordRepository
                    .findByIsDeletedFalseAndTypeAndCategory(
                            type, category.toLowerCase().trim(), pageable)
                    .map(this::toResponse);
        }
        if (hasType) {
            return recordRepository
                    .findByIsDeletedFalseAndType(type, pageable)
                    .map(this::toResponse);
        }
        if (hasCategory) {
            return recordRepository
                    .findByIsDeletedFalseAndCategory(
                            category.toLowerCase().trim(), pageable)
                    .map(this::toResponse);
        }

        return recordRepository
                .findByIsDeletedFalse(pageable)
                .map(this::toResponse);
    }

    public RecordResponse getById(String id) {
        return toResponse(findActiveById(id));
    }

    public RecordResponse update(String id, UpdateRecordRequest request) {
        Record record = findActiveById(id);

        if (request.getAmount() != null) record.setAmount(request.getAmount());
        if (request.getType() != null) record.setType(request.getType());
        if (request.getCategory() != null && !request.getCategory().isBlank())
            record.setCategory(request.getCategory().toLowerCase().trim());
        if (request.getDate() != null) record.setDate(request.getDate());
        if (request.getNotes() != null) record.setNotes(request.getNotes());

        record.setUpdatedAt(LocalDateTime.now());
        return toResponse(recordRepository.save(record));
    }

    public void delete(String id) {
        Record record = findActiveById(id);
        record.setIsDeleted(true);
        record.setUpdatedAt(LocalDateTime.now());
        recordRepository.save(record);
    }

    private Record findActiveById(String id) {
        return recordRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + id));
    }

    private RecordResponse toResponse(Record record) {
        return RecordResponse.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .notes(record.getNotes())
                .createdBy(record.getCreatedBy())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}