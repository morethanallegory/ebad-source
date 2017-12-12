package junit;

import static org.junit.Assert.fail;

import org.junit.Test;

import application.Application;

public class ApplicationTest {

	@Test
	public void test() {
		try {
			Application.initialize();
			// IO.copyFilesToUserSubDir( Config.userDataDirName,
			// Config.configFileName);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
