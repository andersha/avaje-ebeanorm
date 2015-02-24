package com.avaje.tests.basic;

import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.tests.model.basic.Order;
import com.avaje.tests.model.basic.OrderDetail;
import com.avaje.tests.model.basic.Product;
import com.avaje.tests.model.basic.ResetBasicData;

public class TestSharedInstancePropagation extends BaseTestCase {

	
	/**
	 * Test that the sharedInstance status is propagated on lazy loading.
	 */
  @Ignore("Doesn't work due to a feature not being available")
  @Test
	public void testSharedListNavigate() {

		ResetBasicData.reset();
		
		Order order = Ebean.find(Order.class)
			.setAutofetch(false)
			.setUseCache(true)
			.setReadOnly(true)
			.setId(1)
			.findUnique();
			
		
		Assert.assertNotNull(order);
		Assert.assertTrue(Ebean.getBeanState(order).isReadOnly());
		
		List<OrderDetail> details = order.getDetails();
		BeanCollection<?> bc = (BeanCollection<?>)details;
		Assert.assertTrue(bc.isReadOnly());
		Assert.assertFalse(bc.isPopulated());
		
		// lazy load
		bc.size();
		
		Assert.assertTrue(bc.isPopulated());
		Assert.assertTrue(bc.size() > 0);
		OrderDetail detail = details.get(0);
		
		Assert.assertTrue(Ebean.getBeanState(detail).isReadOnly());
		Assert.assertFalse(Ebean.getBeanState(detail).isReference());
		
		Product product = detail.getProduct();

		Assert.assertTrue(Ebean.getBeanState(product).isReadOnly());
		
		// lazy load
		product.getName();
		Assert.assertFalse(Ebean.getBeanState(product).isReference());
		
	}
}
