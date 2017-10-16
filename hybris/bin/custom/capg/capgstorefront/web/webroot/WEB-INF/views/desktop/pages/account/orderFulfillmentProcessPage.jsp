<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/desktop/template"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/desktop/order"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/desktop/common"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="deliveryAddress" tagdir="/WEB-INF/tags/desktop/user"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
	
<template:page pageTitle="${pageTitle}">

<table  cellspacing="0" cellpadding="0">
                           <thead id="orderSplit">
                              <tr>
                                 <th class="dark-text" scope="col">
                                    LOCATION
                                 </th>
                                 <th class="com-col-1 dark-text" scope="col">
                                   DESTINATION
                                 </th>
                                 <th class="com-col-2 dark-text" scope="col">
                                    SKU
                                 </th>
                                  <th class="com-col-2 dark-text" scope="col">
                                    AMOUNT
                                 </th>
                              </tr>
                           </thead>
                           
							 <tbody id="relnDocsTB">
							<c:forEach items="${orderFulFillmentDataFinalList}" var="orderFulFillmentData"
											varStatus="loop">	
                              <tr>
                                 <td scope="row">
                                    		<span class="sourcedata">${orderFulFillmentData.source}</span>
                                 </td>
                                 <td class="com-col-2"><span class="destdata">${orderFulFillmentData.destination}</span></td>
                                 <td class="com-col-3"><span class="skudata">${orderFulFillmentData.sku}</span></td>
                                 <td class="com-col-3"><span class="amountdata">${orderFulFillmentData.amount}</span></td>
                              </tr>
                              
                              </c:forEach>
                               </tbody>
                         </table>	

</template:page>
