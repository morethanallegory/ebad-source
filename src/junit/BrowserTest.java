package junit;

import static org.junit.Assert.fail;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

import application.FileAndPathNames;


public class BrowserTest {

	@Test
	public void test() {
		String html = FileAndPathNames.RESULTS_PAGE_PATH.toString();
		File file = new File(html);
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				fail(e.getMessage());
			}
		} else {
			fail("Desktop is not supported");
		}
	}

	@Test
	public void displayHelpTest() {
		String html = FileAndPathNames.RESULTS_PAGE_PATH.toString();
		File file = new File(html);
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e) {
				fail(e.getMessage());
			}
		} else {
			fail("Desktop is not supported");
		}
	}

}
