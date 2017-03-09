package com.fish.webcrawler;


public interface LinkFilter {
	public boolean accept(String url);
}