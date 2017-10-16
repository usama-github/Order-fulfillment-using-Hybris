package com.capg.techchallenge.order.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.capg.techchallenge.order.dao.TechchallengeOrderDao;
import com.rexel.region.dao.impl.DefaultRexelRegionDao;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

public class TechchallengeOrderDaoImpl implements TechchallengeOrderDao{

	private static final Logger LOG = Logger.getLogger(TechchallengeOrderDaoImpl.class);
	private FlexibleSearchService flexibleSearchService;
	
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}

	@Override
	public List<StockLevelModel> getAllStockLevelsForProduct(ProductModel product) {
		String productCode = product.getCode();
		checkProductCode(productCode);
		FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {StockLevel} WHERE {productCode} = ?productCode ORDER BY {available} DESC" );
		fQuery.addQueryParameter("productCode", productCode);
		SearchResult<StockLevelModel> result = null;
		try
		{
			result = getFlexibleSearchService().search(fQuery);
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve the stock details for product "+productCode+"",e);
			return null;
		}
		return result.getResult();
	}

	private void checkProductCode(String productCode)
	{
	    if (productCode == null)
	     {
	      throw new IllegalArgumentException("product code cannot be null.");
	     }
    }
}
