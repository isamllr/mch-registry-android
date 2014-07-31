package com.mch.registry.css.test;

import android.test.AndroidTestCase;

import com.mch.registry.ccs.data.PregnancyDataHandler;

/**
 * Created by Isa on 31.07.2014.
 */
public class PregnancyDataHandlerTest  extends AndroidTestCase {

	public void setUp(){
		//RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
	}

	public void testAddEntry(){
		String testText = "Testtest";
		String testDate = "2014-01-01";
		int testDay = 29;

		PregnancyDataHandler rdh = new PregnancyDataHandler(getContext(), "test", null,1);
		rdh.incrementLoadingProgress();

		assertEquals(0, rdh.incrementLoadingProgress());
		assertEquals(1, rdh.incrementLoadingProgress());
		assertEquals(2, rdh.incrementLoadingProgress());
		assertEquals(3, rdh.incrementLoadingProgress());
		assertEquals(4, rdh.incrementLoadingProgress());
	}

	public void tearDown() throws Exception{
		super.tearDown();
	}
}
