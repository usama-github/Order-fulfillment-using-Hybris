package com.capg.techchallenge.order.dao;

import java.util.List;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;

public interface TechchallengeOrderDao {

	public List<StockLevelModel> getAllStockLevelsForProduct(ProductModel product);
}
