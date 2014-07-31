package com.mch.registry.css.server.test;

/**
 * Created by Isa on 31.07.2014.
 */

import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.MySqlHandler;
import com.mch.registry.ccs.server.com.mch.registry.ccs.server.data.Pregnancy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)

public class MySqlHandlerTest {

	private int multiplier;

	public MySqlHandlerTest(int testParameter) {
		this.multiplier = testParameter;
	}


	// creates the test data

	@Parameters

	public static Collection<Object[]> data() {
		Object[][] data = new Object[][]{{1}, {5}, {121}};
		return Arrays.asList(data);
	}


	@Test

	public void getPregnancyInfoByGcmRegIdTest() {

		MySqlHandler mysql = new MySqlHandler();
		Pregnancy preg = mysql.getPregnancyInfoByGcmRegId("APA91bFnWV6Y2Ty4hsNtOEmvLrwgVMXLFkmT4Fd2xIfTSV8zNe8cVLVjGB0I09JeXjiBO52n23WY48Luprqvq9k2vxPrdSKN0QuAh11H76MsgVlHtwdsHYMYZjLQ_gAqfiGfk8J4SNnc1p4buI4LebWOjmKM9yeeOHKrWSEd2r5ORGCjvXXzBs0");
		assertEquals("5s55emmm69uunf0bkvjihl9rnb", preg.getActivationCode());

	}


}

