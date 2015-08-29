package com.webservice.newsapp.jaxb.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.webservice.newsapp.database.manager.DBconnection;
import com.webservice.newsapp.jaxb.model.Feed;
import com.webservice.newsapp.jaxb.model.NewsPaper;
import com.webservice.newsapp.jaxb.model.RSSFeedParser;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.tartarus.snowball.stemmer.SnowballStemmer;

import sun.util.logging.resources.logging;

@Path("/feedspublish")
public class XMLResource {
	// This can be used to test the integration with the browser
	private final static Logger log = Logger.getLogger(XMLResource.class
			.getName());
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;

	@GET
	@Produces({ MediaType.TEXT_XML })
	public String getFeeds() throws IOException, InstantiationException,
			IllegalAccessException, InterruptedException, SQLException {

		fileTxt = new FileHandler(
				"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\servicio.log");
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		log.addHandler(fileTxt);
		
		String archivo = null;
		String archivo_prueba = null;
		File file = new File(
				"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\articles.rss");
		if (!file.exists()) {
			file.createNewFile();
		}

		DBconnection conexionDB = new DBconnection();
		conexionDB.test_connection();
		conexionDB.getNewsPapers();
		List<NewsPaper> newspaperUrls = conexionDB.getNewsPapers();

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		archivo = "<?xml version=\"1.0\"?><feeds>";
		for (int i = 0; i < newspaperUrls.size(); i++) {

			RSSFeedParser parser = new RSSFeedParser(newspaperUrls.get(i)
					.getUrl());
			Feed feed = parser.readFeed();

			for (int i1 = 0; i1 < feed.getMessages().size(); i1++) {

				if (feed.getMessages().get(i1).getPubDate() != null) {
					System.out.println("La fecha no es nula");
					conexionDB.insert_feed(feed.getMessages().get(i1)
							.getDescription(), feed.getMessages().get(i1)
							.getTitle(),
							feed.getMessages().get(i1).getAuthor(), feed
									.getMessages().get(i1).getPubDate(),
							newspaperUrls.get(i).getId());
					archivo += "<item><title>"
							+ feed.getMessages().get(i1).getTitle()
							+ "</title>";
					archivo += "<link>" + feed.getMessages().get(i1).getLink()
							+ "</link>";
					archivo += "<description>"
							+ feed.getMessages().get(i1).getDescription()
							+ "</description>";
					archivo += "<content:encoded>"
							+ feed.getMessages().get(i1).getGuid()
							+ "</content:encoded>";
					archivo += "<pubDate>"
							+ feed.getMessages().get(i1).getPubDate()
							+ "</pubDate></item>";

				}

			}

		}
		archivo += "</feeds>";
		// Escribimos en el archivo
		bw.write(archivo_prueba);
		bw.close();

		System.out.println("Done");

		/*
		 * Class stemClass = null; try { stemClass = Class
		 * .forName("org.tartarus.snowball.stemmer.ext.spanishStemmer"); } catch
		 * (ClassNotFoundException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } SnowballStemmer stemmer = (SnowballStemmer)
		 * stemClass.newInstance();
		 * 
		 * Reader reader; String a =
		 * "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\articles.rss"
		 * ; reader = new BufferedReader(new FileReader(a));
		 * 
		 * StringBuffer input = new StringBuffer();
		 * 
		 * OutputStream outstream = null;
		 * 
		 * try { outstream = new FileOutputStream(
		 * "C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\documents\\salida.txt"
		 * ); } catch (FileNotFoundException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 * 
		 * Writer output = new OutputStreamWriter(outstream); output = new
		 * BufferedWriter(output);
		 * 
		 * int repeat = 1; // if (args.length > 4) { // repeat =
		 * Integer.parseInt(args[4]); // }
		 * 
		 * Object[] emptyArgs = new Object[0]; int character; while ((character
		 * = reader.read()) != -1) { char ch = (char) character; if
		 * (Character.isWhitespace((char) ch)) { if (input.length() > 0) {
		 * stemmer.setCurrent(input.toString()); for (int i = repeat; i != 0;
		 * i--) { stemmer.stem(); } output.write(stemmer.getCurrent());
		 * output.write('\n'); input.delete(0, input.length()); } } else {
		 * input.append(Character.toLowerCase(ch)); } } output.flush(); Logger
		 * log = null; log.info("Output:" + output);
		 */
		return archivo;
	}
}
