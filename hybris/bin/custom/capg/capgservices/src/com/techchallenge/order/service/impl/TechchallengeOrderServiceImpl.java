package com.capg.techchallenge.order.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.capg.techchallenge.order.dao.TechchallengeOrderDao;
import com.capg.techchallenge.order.dao.impl.TechchallengeOrderDaoImpl;
import com.capg.techchallenge.order.service.TechchallengeOrderService;

import de.hybris.platform.commerceservices.service.data.OrderFulFillmentData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.model.ModelService;

public class TechchallengeOrderServiceImpl implements TechchallengeOrderService{
	
	private static final Logger LOG = Logger.getLogger(TechchallengeOrderServiceImpl.class);
	private TechchallengeOrderDao techchallengeOrderDao;
	private ModelService modelService;

	 public List<OrderFulFillmentData> orderFulfillmentProcess(List<OrderModel> orders){
		 	List<OrderFulFillmentData> orderFulFillmentDataFinalList =new ArrayList<OrderFulFillmentData>();
		 	orderFulFillmentDataFinalList = iterateOrders(orders);
	    	return orderFulFillmentDataFinalList;
	    }
	    
	    public List<OrderFulFillmentData> iterateOrders(List<OrderModel> orders){
	    	List<OrderFulFillmentData> orderFulFillmentDataListOfAllOrders =new ArrayList<OrderFulFillmentData>();
	    	for(OrderModel order : orders){
	    		orderFulFillmentDataListOfAllOrders.addAll(iterateOrderEntries(order));
	    	}
	    	return orderFulFillmentDataListOfAllOrders;
	    }
	    
	    public List<OrderFulFillmentData> iterateOrderEntries(OrderModel order){
	    	List<OrderFulFillmentData> orderFulFillmentDataListOfOrder = new ArrayList<OrderFulFillmentData>();
	    	for(AbstractOrderEntryModel orderEntry : order.getEntries()){
	    		List<StockLevelModel> stockLevelListOfOrderEntryProduct = getStockLevelForProduct(orderEntry.getProduct());
	    		if(CollectionUtils.isNotEmpty(stockLevelListOfOrderEntryProduct)){
	    			orderFulFillmentDataListOfOrder.addAll(splitOrderByAvailableStock(stockLevelListOfOrderEntryProduct,orderEntry));
	    		}
	    	}
	    	return orderFulFillmentDataListOfOrder;
	    }
	    
	    public List<StockLevelModel> getStockLevelForProduct(ProductModel product){
	    	List<StockLevelModel> stockLevelListOfProduct = getTechchallengeOrderDao().getAllStockLevelsForProduct(product);
			return stockLevelListOfProduct;
	    }
	    
	    public List<OrderFulFillmentData> splitOrderByAvailableStock(List<StockLevelModel> stockLevelListOfOrderEntryProduct, AbstractOrderEntryModel orderEntry){
	    	long orderedAmount = orderEntry.getQuantity();
	    	long achievedAmount = 0l;
	    	List<OrderFulFillmentData> orderFulFillmentDataList = new ArrayList<OrderFulFillmentData>();
	    	try{
	    		for(StockLevelModel stockLevel : stockLevelListOfOrderEntryProduct){
	    			long availableAmountOfStock = stockLevel.getAvailable();
	    			if(achievedAmount == orderedAmount || availableAmountOfStock == 0)
	    			{
	    				break;
	    			}
	    			long remainingAmountToAcheiveTarget = orderedAmount - achievedAmount;
	    			achievedAmount = calculateStock(achievedAmount, stockLevel, availableAmountOfStock, remainingAmountToAcheiveTarget);
	    			OrderFulFillmentData orderFulFillmentData = prepareOrderFulFillmentDataList(orderEntry, availableAmountOfStock,
						stockLevel,remainingAmountToAcheiveTarget);
	    			orderFulFillmentDataList.add(orderFulFillmentData);
	    			getModelService().save(stockLevel);
	    		}
	    	}
	    	catch(Exception e){
	    		LOG.error("Something went wrong while splitting the order by available stock",e);
	    	}
	    	return orderFulFillmentDataList;
	    }

		private OrderFulFillmentData prepareOrderFulFillmentDataList(AbstractOrderEntryModel orderEntry,
				long availableAmountOfStock, StockLevelModel stockLevel,long remainingAmountToAcheiveTarget) {
			OrderFulFillmentData orderFulFillmentData = new OrderFulFillmentData();
			if(null!=stockLevel.getWarehouse()){
				orderFulFillmentData.setSource(stockLevel.getWarehouse().getName());
			}
			if(null!=orderEntry.getDeliveryAddress()){
				orderFulFillmentData.setDestination(orderEntry.getDeliveryAddress().getTown());
			}
			orderFulFillmentData.setSku(orderEntry.getProduct().getCode());
			if(remainingAmountToAcheiveTarget >= availableAmountOfStock)
			{
				orderFulFillmentData.setAmount((int)availableAmountOfStock);
			}
			else
			{
				orderFulFillmentData.setAmount((int)remainingAmountToAcheiveTarget);
			}
			return orderFulFillmentData;
		}

		private long calculateStock(long achievedAmount, StockLevelModel stockLevel, long availableAmountOfStock,
				long remainingAmountToAceiveTarget) {
			if( availableAmountOfStock >= remainingAmountToAceiveTarget){
				stockLevel.setAvailable((int) (availableAmountOfStock - remainingAmountToAceiveTarget));
				achievedAmount = achievedAmount + remainingAmountToAceiveTarget;
			}
			else
			{
				achievedAmount = achievedAmount + availableAmountOfStock;
				stockLevel.setAvailable(0);
			}
			return achievedAmount;
		}

		public TechchallengeOrderDao getTechchallengeOrderDao() {
			return techchallengeOrderDao;
		}

		public void setTechchallengeOrderDao(TechchallengeOrderDao techchallengeOrderDao) {
			this.techchallengeOrderDao = techchallengeOrderDao;
		}

		public ModelService getModelService() {
			return modelService;
		}

		public void setModelService(ModelService modelService) {
			this.modelService = modelService;
		}
}
