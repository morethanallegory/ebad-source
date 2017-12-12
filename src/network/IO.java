package network;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

public class IO {
	/*
	 * public static String copyFilesToUserDir(String subFolder, String
	 * fileToCopy) throws SecurityException, IOException {
	 * 
	 * File dataFolder = new File(System.getProperty("user.home") +
	 * System.getProperty("file.separator") + subFolder); // create the
	 * directory is not already there if (!dataFolder.exists()) {
	 * dataFolder.mkdir(); } String copiedFilePath = dataFolder.getPath() +
	 * System.getProperty("file.separator") + fileToCopy; File copy = new
	 * File(copiedFilePath); if (!copy.exists()) { // now get the name of file
	 * to be copied
	 * 
	 * IO.class.getClassLoader().getResourceAsStream(fileToCopy); String
	 * filename = IO.class.getClassLoader()
	 * .getResource("/files"+fileToCopy).getFile(); File toCopy = new
	 * File(filename); Files.copy(toCopy.toPath(), copy.toPath()); }
	 * 
	 * return copiedFilePath; }
	 */

	public static boolean createDir(String dir) {
		File dataFolder = new File(dir);
		if (!dataFolder.exists()) {
			return dataFolder.mkdir();
		}
		return false;
	}

	public static void copyTextFile(String fileTo, String fileFrom, String encoding) throws IOException {
		File copy = new File(fileTo);
		if (!copy.exists()) {
			InputStream ins = ClassLoader.getSystemClassLoader().getResourceAsStream(fileFrom);
			BufferedReader in = new BufferedReader(new InputStreamReader(ins, encoding));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTo, true), encoding));
			try {

				String line = null;
				while ((line = in.readLine()) != null) {
					out.write(line);
					out.newLine(); // Write system dependent end of
									// line.
				}

			} finally {
				in.close();
				out.close();
			}
		}
	}

	public static BufferedReader filefromJar(String fileName, String encoding) throws UnsupportedEncodingException {
		InputStream ins = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
		BufferedReader in = new BufferedReader(new InputStreamReader(ins, encoding));
		return in;
	}

	public static void copyImageFile(String fileTo, String fileFrom, String fileExt) throws IOException {
		File copy = new File(fileTo);
		if (!copy.exists()) {
			BufferedImage image = loadImage(fileFrom);
			ImageIO.write(image, fileExt, copy);
		}
	}

	/*
	public static void copyFile(String fileTo, String fileFrom, String encoding) throws SecurityException, IOException {
		File copy = new File(fileTo);
		if (!copy.exists()) {
			 
			JarFile jfile = new JarFile(ClassLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath());

			for (Enumeration<JarEntry> e = jfile.entries(); e.hasMoreElements();) {
				JarEntry entry = e.nextElement();
				if (entry.getName().equals(fileFrom)) {
					if (encoding != null) {
						java.io.InputStream is = jfile.getInputStream(entry);
						BufferedReader in = new BufferedReader(new InputStreamReader(is, encoding));
						// And where to copy it to
						BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTo, true), encoding));

						String line = null;
						while ((line = in.readLine()) != null) {
							out.write(line);
							out.newLine(); // Write system dependent end of
											// line.
						}

						// close both streams
						in.close();
						out.close();
					} else {
						java.io.File f = new java.io.File(fileTo);
						java.io.InputStream is = jfile.getInputStream(entry);
						java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
						while (is.available() > 0) {
							fos.write(is.read());
						}
						fos.close();
						is.close();
					}
					break;
				}
			}

			jfile.close();
		}
	}
	*/
	public static File getFile(String file) throws IOException {
		return new File(file);
	}

	public static boolean writeFile(String fileName, String text) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(text);
			return true;
		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}
		return false;
	}

	public static String readText(String fileName, String encoding) {
		InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input, encoding));

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (input != null)
					input.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	public static BufferedImage loadImage(String fileName) throws IOException {
		InputStream input = null;
		BufferedImage image;
		try {
			input = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
			image = ImageIO.read(input);
		} finally {
			if (input != null)
				input.close();
		}
		return image;
	}

	public static String readFile(String fileName) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			return sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

}
