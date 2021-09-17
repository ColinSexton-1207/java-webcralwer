package com.val.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public  class Crawler {
	private static final int SEARCH_LIMIT = 100; // Max # of URL's that can be searched through
	private Set<String> docsSearched = new HashSet<String>(); // Unique URL entries that have been searched
	private List<String> docsToSearch = new LinkedList<String>(); // List of URL's that might be searched through (in order)
	
	/* --- URL Traversal --- */
	/*
	 * Returns the next URL that will be searched through. This is done in the order the URL's are found.
	 *
	 * Ensures that the next URL is not one that has been searched through already.
	 */
	private String nextPage() {
		String nextPage; // Placeholder for the next URL to visit
		
		// Loops through the list finding the next unvisited URL
		do {
			nextPage = this.docsToSearch.remove(0); // Removes the most recent page visited from the list
		}while(this.docsSearched.contains(nextPage));
		
		this.docsSearched.add(nextPage); // Adds the next unique URL to the Set so the page can be searched through
		
		return nextPage; // Returns the next URL that will be searched through
	}
	
	/* --- URL Search --- */
	/*
	 * Creates a new crawler leg that will make an HTTP request and then parse the document to find the search keyword
	 */
	public void search(String url, String phrase) {
		// Loops through parse-able pages as long as the search limit has not been reached
		while(this.docsSearched.size() < SEARCH_LIMIT) {
			String currentPage; // Placeholder for the current URL visited
			
			CrawlerLeg leg = new CrawlerLeg(); // Initializes a new leg for the current document to be searched for
			
			// If the next element doesn't exist, add a current URL to the visited Set
			if(this.docsToSearch.isEmpty()) {
				currentPage = url;
				this.docsSearched.add(url);
			} else currentPage = this.nextPage(); // Traverse from current URL to the next URL in the list
			
			leg.crawl(currentPage);
			
			boolean success = leg.searchFor(phrase);
			
			// Print out success message and end the program if the phrase is found
			if(success) {
				System.out.println(String.format("Success! The item, %s, was found at %s!", phrase, currentPage));
				break;
			}
			
			this.docsToSearch.addAll(leg.getLinks());
		}
		
		// Prints out the number of URLs searched
		System.out.println(String.format("This search went through %s page(s)!", this.docsSearched.size()));
	}
}