package com.hust.Ecommerce.mappers.cashbook;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.hust.Ecommerce.dtos.cashbook.ClientPaymentMethodResponse;
import com.hust.Ecommerce.dtos.cashbook.PaymentMethodRequest;
import com.hust.Ecommerce.dtos.cashbook.PaymentMethodResponse;
import com.hust.Ecommerce.entities.cashbook.PaymentMethod;
import com.hust.Ecommerce.mappers.GenericMapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMethodMapper extends GenericMapper<PaymentMethod, PaymentMethodRequest, PaymentMethodResponse> {

    @Mapping(source = "id", target = "paymentMethodId")
    @Mapping(source = "name", target = "paymentMethodName")
    @Mapping(source = "code", target = "paymentMethodCode")
    ClientPaymentMethodResponse entityToClientResponse(PaymentMethod entity);

    List<ClientPaymentMethodResponse> entityToClientResponse(List<PaymentMethod> entities);

}
