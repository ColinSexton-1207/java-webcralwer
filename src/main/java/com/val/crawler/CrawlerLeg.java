package com.val.crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerLeg {
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:92.0) Gecko/20100101 Firefox/92.0";
    private List<String> links = new LinkedList<String>(); // List of URLs
    private Document htmlDoc; // Targeted webpage(document)
	
	/* --- URL Crawl --- */
	/*
	 * Creates an HTTP request, checks HTTP response, and gathers all links on the page
	 *
	 * Will perform the searchFor operation once the crawl is completed.
	 */
	public boolean crawl(String url) {
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT); // Connects to the url w/ the USER_Agent
			Document htmlDoc = connection.get(); // Gets the connection for the current document
			this.htmlDoc = htmlDoc;
			
			// Checks if visited webpage is an HTML document
			if(!connection.response().contentType().contains("text/html")) {
				System.out.println("HTML not found, unable to process request!");
				return false;
			}
			
			Elements pageLinks = htmlDoc.select("a[href]"); // Targets URL's on currentPage (HTML uses <a href="xxx" for a link)
			
			// Search through all links on the page and add them to list of links
			for(Element link : pageLinks) { this.links.add(link.absUrl("href")); }
			
			return true;
		}catch(IOException e) { return false; } // Error getting HTTP request
	}
	
	/* --- URL Search --- */
	/*
	 * Searches the HTML body element after a successful crawl occurs
	 */
	public boolean searchFor(String phrase) {
		// Ensure that the crawl() method is called prior to running the search
		if(this.htmlDoc == null) {
			System.out.println("Error: Must have crawl the webpage before proceeding.");
			return false;
		}
		
		System.out.println("Searching for phrase: " + phrase + "...");
		String bodyText = this.htmlDoc.body().text();
		
		return bodyText.toLowerCase().contains(phrase.toLowerCase()); // Returns the searched for phrase
	}
	
	// Return links from the compiled list
	public List<String> getLinks() { return this.links; }
}