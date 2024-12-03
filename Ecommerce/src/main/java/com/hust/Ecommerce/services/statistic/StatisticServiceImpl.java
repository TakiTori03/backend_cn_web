package com.hust.Ecommerce.services.statistic;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.Ecommerce.dtos.statistic.StatisticOrderResource;
import com.hust.Ecommerce.dtos.statistic.StatisticResponse;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.repositories.order.OrderProjectionRepository;
import com.hust.Ecommerce.repositories.order.OrderRepository;
import com.hust.Ecommerce.repositories.product.BrandRepository;
import com.hust.Ecommerce.repositories.product.ProductRepository;
import com.hust.Ecommerce.repositories.review.ReviewRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private BrandRepository brandRepository;
    private OrderProjectionRepository orderProjectionRepository;

    @Override
    public StatisticResponse getStatistic() {
        StatisticResponse statisticResponse = new StatisticResponse();

        int totalProduct = productRepository.countByProductId();
        int totalOrder = orderRepository.countByOrderId();
        int totalReview = reviewRepository.countByReviewId();
        int totalUser = userRepository.countAccountActivated("USER");
        int totalBrand = brandRepository.countByBrandId();

        List<StatisticOrderResource> statisticOrder = orderProjectionRepository.getOrderCountByCreateDate();

        statisticResponse.setTotalProduct(totalProduct);
        statisticResponse.setTotalOrder(totalOrder);
        statisticResponse.setTotalReview(totalReview);
        statisticResponse.setTotalCustomer(totalUser);
        statisticResponse.setTotalBrand(totalBrand);

        statisticResponse.setStatisticOrder(statisticOrder);
        return statisticResponse;
    }

}
