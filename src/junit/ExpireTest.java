package junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import util.ExpireProcess;

public class ExpireTest {

	@Test
	public void test() {
		String expireDateString = "29/09/2015";
		assertFalse(ExpireProcess.expired(expireDateString));
		expireDateString = "14/10/2015";
		assertFalse(ExpireProcess.expired(expireDateString));
		expireDateString = "13/10/2014";
		assertTrue(ExpireProcess.expired(expireDateString));
	}

}
