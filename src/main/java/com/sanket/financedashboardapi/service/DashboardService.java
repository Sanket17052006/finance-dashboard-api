package com.sanket.financedashboardapi.service;

import com.sanket.financedashboardapi.dto.response.CategoryBreakdownResponse;
import com.sanket.financedashboardapi.dto.response.DashboardSummaryResponse;
import com.sanket.financedashboardapi.dto.response.MonthlyTrendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MongoTemplate mongoTemplate;

    public DashboardSummaryResponse getSummary() {

        MatchOperation matchActive = Aggregation.match(
                Criteria.where("isDeleted").ne(true)
        );

        GroupOperation groupByType = Aggregation.group("type")
                .sum("amount").as("total")
                .count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(matchActive, groupByType);

        List<org.bson.Document> results = mongoTemplate
                .aggregate(aggregation, "records", org.bson.Document.class)
                .getMappedResults();

        double totalIncome = 0.0;
        double totalExpenses = 0.0;
        long totalRecords = 0L;

        for (org.bson.Document doc : results) {
            String type = doc.getString("_id");
            double total = ((Number) doc.get("total")).doubleValue();
            int count = doc.getInteger("count");

            if ("INCOME".equals(type)) {
                totalIncome = total;
                totalRecords += count;
            } else if ("EXPENSE".equals(type)) {
                totalExpenses = total;
                totalRecords += count;
            }
        }

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(totalIncome - totalExpenses)
                .totalRecords(totalRecords)
                .build();
    }

    public List<CategoryBreakdownResponse> getBreakdown() {

        MatchOperation matchActive = Aggregation.match(
                Criteria.where("isDeleted").ne(true)

        );

        GroupOperation groupByCategoryAndType = Aggregation
                .group("category", "type")
                .sum("amount").as("total");

        SortOperation sortByTotal = Aggregation.sort(
                Sort.by(Sort.Direction.DESC, "total")
        );

        Aggregation aggregation = Aggregation.newAggregation(
                matchActive,
                groupByCategoryAndType,
                sortByTotal
        );

        List<org.bson.Document> results = mongoTemplate
                .aggregate(aggregation, "records", org.bson.Document.class)
                .getMappedResults();

        List<CategoryBreakdownResponse> breakdown = new ArrayList<>();

        for (org.bson.Document doc : results) {
            org.bson.Document id = (org.bson.Document) doc.get("_id");
            String category = id.getString("category");
            String type = id.getString("type");
            double total = ((Number) doc.get("total")).doubleValue();
            breakdown.add(new CategoryBreakdownResponse(category, type, total));
        }

        return breakdown;
    }

    public List<MonthlyTrendResponse> getTrends() {

        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6).withDayOfMonth(1);

        MatchOperation matchActive = Aggregation.match(
                Criteria.where("isDeleted").ne(true)
                        .and("date").gte(sixMonthsAgo)
        );

        ProjectionOperation projectFields = Aggregation.project()
                .and(DateOperators.Year.yearOf("date")).as("year")
                .and(DateOperators.Month.monthOf("date")).as("month")
                .andInclude("type", "amount");

        GroupOperation groupByMonthAndType = Aggregation.group(
                Fields.fields("year", "month", "type")
        ).sum("amount").as("total");

        SortOperation sort = Aggregation.sort(
                Sort.by(Sort.Direction.ASC, "_id.year")
                        .and(Sort.by(Sort.Direction.ASC, "_id.month"))
        );

        Aggregation aggregation = Aggregation.newAggregation(
                matchActive,
                projectFields,
                groupByMonthAndType,
                sort
        );

        List<org.bson.Document> results = mongoTemplate
                .aggregate(aggregation, "records", org.bson.Document.class)
                .getMappedResults();

        Map<String, MonthlyTrendResponse> trendMap = new LinkedHashMap<>();

        for (org.bson.Document doc : results) {
            org.bson.Document id = (org.bson.Document) doc.get("_id");
            int year = ((Number) id.get("year")).intValue();
            int month = ((Number) id.get("month")).intValue();
            String type = id.getString("type");
            double total = ((Number) doc.get("total")).doubleValue();

            String key = year + "-" + String.format("%02d", month);

            trendMap.putIfAbsent(key, new MonthlyTrendResponse(year, month, 0.0, 0.0, 0.0));

            MonthlyTrendResponse trend = trendMap.get(key);
            if ("INCOME".equals(type)) {
                trend.setTotalIncome(total);
            } else if ("EXPENSE".equals(type)) {
                trend.setTotalExpenses(total);
            }
            trend.setNet(trend.getTotalIncome() - trend.getTotalExpenses());
        }

        return new ArrayList<>(trendMap.values());
    }
}