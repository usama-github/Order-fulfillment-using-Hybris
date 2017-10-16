package com.capg.techchallenge.order.service;

import java.util.List;

import de.hybris.platform.commerceservices.service.data.OrderFulFillmentData;
import de.hybris.platform.core.model.order.OrderModel;

public interface TechchallengeOrderService {

	public List<OrderFulFillmentData> orderFulfillmentProcess(List<OrderModel> orders);
}
