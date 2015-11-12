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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.webservice.newsapp.database.manager.DBconnection;
import com.webservice.newsapp.database.manager.ReadService;
import com.webservice.newsapp.jaxb.model.Feed;
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
	ArrayList<String> conjuntoVacias = null;

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

		ReadService read = new ReadService();
		read.readBdInsert();
		System.out.println("Leidos desde BBDD");

		DBconnection conexionDB = new DBconnection();
		try {
			conexionDB.test_connection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		archivo = "<?xml version=\"1.0\" encoding=\"utf-8\"?><feeds>";
/*
		// Diario de avisos
		List<Feed> listNews2 = conexionDB.getNews(2);
		for (int j = 0; j < listNews2.size(); j++) {
			archivo += "<item><title>" + listNews2.get(j).getTitle()
					+ "</title>";
			archivo += "<link>" + listNews2.get(j).getLink() + "</link>";
			archivo += "<description>" + listNews2.get(j).getDescription()
					+ "</description>";
			archivo += "<encoded>" + listNews2.get(j).getLanguage()
					+ "</encoded>";
			archivo += "<pubDate>" + listNews2.get(j).getPubDate()
					+ "</pubDate></item>";
		}*/
		archivo += "</feeds>";

		// Escribimos en el archivo

		System.out.println("Done");

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(archivo);
		bw.close();

		System.out.println("Done File");
		conjuntoVacias = new ArrayList<String>();
		conjuntoVacias = leerConjuntoVacias();
		System.out.println("Leido el fichero de vacías");
		// ElDia
		List<Feed> listNews = conexionDB.getNews(1);
		for (int i = 0; i < listNews.size(); i++) {
			 String texto1 = limpiarTextoVacias(listNews.get(i).getDescription());
			 System.out.println("Texto sin vacias" + texto1);		
		}
		
		
		return archivo;
	}

	
	
	
	private String limpiarTextoVacias(String description) {
		 System.out.println("Limpieza de " + description);
		for (int i = 0; i < conjuntoVacias.size(); i++){
			description = description.replaceAll("\\b" + conjuntoVacias.get(i) + "\\b", "");
		}
		
		return description;
				
	}

	private ArrayList<String> leerConjuntoVacias() {

		BufferedReader br = null;
		String sCurrentLine;
		ArrayList<String> vacias = new ArrayList<String>();

		try {
			br = new BufferedReader(
					new FileReader(
							"C:\\Users\\ASUS\\Webservice\\com.webservice.newsapp\\resources\\vacias.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				vacias.add(sCurrentLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vacias;
	}

}
