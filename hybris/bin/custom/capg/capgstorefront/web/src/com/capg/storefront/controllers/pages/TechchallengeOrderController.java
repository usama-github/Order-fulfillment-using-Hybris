package com.capg.storefront.controllers.pages;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.capg.techchallenge.order.service.TechchallengeOrderService;
import com.rexel.storefront.annotations.RequireHardLogIn;
import com.rexel.storefront.constants.RexelAnalyticsPageTypeConstants;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commerceservices.service.data.OrderFulFillmentData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.user.UserService;

@Controller
@Scope("tenant")
@RequestMapping("/my-account")
public class TechchallengeOrderController extends AbstractPageController
{
	private static final String ORDER_FULFILLMENT_PROCESS = "orderfulFillmentProcess";
	private static final String OrderFulfillmentProcessPage = "pages/account/orderFulfillmentProcessPage"; 	//your path to jsp page
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "techchallengeOrderService")
	private TechchallengeOrderService techchallengeOrderService;

	@RequestMapping(value = "/orderfulFillmentProcess",method = RequestMethod.GET)
	@RequireHardLogIn
	public String orderFulFillmentProcess(final Model model) throws CMSItemNotFoundException
	{
		
		final B2BCustomerModel customer = (B2BCustomerModel) userService.getCurrentUser();
		final List<OrderModel> orderModels = (List<OrderModel>) customer.getOrders();
        List<OrderFulFillmentData> orderFulFillmentDataFinalList = techchallengeOrderService.orderFulfillmentProcess(orderModels);
        List<String> orderCode = new ArrayList<String>();
        for(OrderFulFillmentData orderFulfillmentData : orderFulFillmentDataFinalList)
        {
        	System.out.println("****************************************************");
        	System.out.println(orderFulfillmentData.getSource());
        	System.out.println(orderFulfillmentData.getDestination());
        	System.out.println(orderFulfillmentData.getSku());
        	System.out.println(orderFulfillmentData.getAmount());
        	System.out.println("****************************************************");
        }
        
        model.addAttribute("orderCode", orderCode);
        model.addAttribute("orderFulFillmentDataFinalList", orderFulFillmentDataFinalList);
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_FULFILLMENT_PROCESS));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_FULFILLMENT_PROCESS));
		model.addAttribute(RexelAnalyticsPageTypeConstants.KEY_PAGETYPE, RexelAnalyticsPageTypeConstants.ACCOUNT);
		return OrderFulfillmentProcessPage;
	}
}
