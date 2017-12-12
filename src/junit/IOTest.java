package junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import network.IO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.AfterClass;
import org.junit.Test;

import application.FileAndPathNames;

public class IOTest {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testFilefromJar() throws DocumentException, IOException {
		BufferedReader reader = IO.filefromJar(FileAndPathNames.HIDDEN_CONFIG_FILE_NAME.toString(), "UTF-8");
		assertTrue(reader.ready());
		SAXReader saxParser = new SAXReader();
		saxParser.read(reader);
		reader.close();
	}

	@Test
	public void canReadConfigTest() throws DocumentException, IOException {
		File xml = null;
		XMLWriter xmlWriter =null;
		try {
			xml = new File(FileAndPathNames.CONFIG_FILE_PATH.toString());

			assertFalse(xml.exists());

			IO.copyTextFile(FileAndPathNames.CONFIG_FILE_PATH.toString(), FileAndPathNames.CONFIG_FILE_NAME.toString(), "UTF8");

			assertTrue(xml.exists());
			
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding("UTF-8");
			Document doc = saxReader.read(xml);
			assertEquals( doc.getXMLEncoding(), "UTF-8");

			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(FileAndPathNames.CONFIG_FILE_PATH.toString()), "UTF8"), format);
			xmlWriter.write(doc);
			xmlWriter.flush();
			xmlWriter.close();
			
			doc = saxReader.read(xml);
			assertEquals( doc.getXMLEncoding(), "UTF-8");
		} finally {			
			if(xml != null){
				xml.delete();
			}
			if(xmlWriter!= null)
				try{
				xmlWriter.close();
				}catch(Exception ex){}
		}

	}

	//@Test
	public void testReadConfig() throws IOException {
		//if the file exists compy temp file
		File tempConfig = new File(FileAndPathNames.TEMP_CONFIG_FILE_PATH.toString());
		assertFalse(tempConfig.exists());

		//copy the config file to temp location
		IO.copyTextFile(FileAndPathNames.TEMP_CONFIG_FILE_PATH.toString(), FileAndPathNames.CONFIG_FILE_NAME.toString(), "UTF8");
		assertTrue(tempConfig.exists());

		long tmpModTime = tempConfig.lastModified();
		tempConfig.delete();
		assertFalse(tempConfig.exists());

		File config = new File(FileAndPathNames.CONFIG_FILE_PATH.toString());
		if (!config.exists()) {
			IO.copyTextFile(FileAndPathNames.CONFIG_FILE_PATH.toString(), FileAndPathNames.CONFIG_FILE_NAME.toString(), "UTF8");
		}

		assertTrue(config.exists());
		long confModTime = config.lastModified();

		System.out.println(new Date(tmpModTime));
		System.out.println(new Date(confModTime));

	}

}
