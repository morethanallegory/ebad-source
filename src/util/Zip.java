package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

	public static void zip(String fileToZip, String zipFileName) {
		try {
			byte[] buf = new byte[1024];

			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFileName));

			FileInputStream in = new FileInputStream(fileToZip);

			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(fileToZip));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Complete the entry
			out.closeEntry();
			in.close();

			// Complete the ZIP file
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
