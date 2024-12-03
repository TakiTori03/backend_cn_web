package com.hust.Ecommerce.controllers.statistic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.dtos.statistic.StatisticResponse;
import com.hust.Ecommerce.services.statistic.StatisticService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
public class StatisticController {

    private StatisticService statisticService;

    @GetMapping
    public ResponseEntity<StatisticResponse> getStatistic() {

        return ResponseEntity.ok(statisticService.getStatistic());
    }
}
