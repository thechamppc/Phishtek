package com.action;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.constant.ServerConstants;
import com.helper.PhishMailGuard;
import com.helper.SpamModel;
import com.helper.StringHelper;

/**
 * s* Example program to list links from a URL.
 */
public class HTMLParserTest {
	public static void main(String[] args) throws IOException {

		// HTMLParsing("http://www.tickld.com/ajax/login.php");
		// HTMLParsing("http://www.tickld.com/ajax/login.php");
		String url="https://www.flipkart.com/?gclid=EAIaIQobChMIqpTdyLuG4gIVQRyPCh1FXwAEEAAYASAAEgLimvD_BwE&ef_id=EAIaIQobChMIqpTdyLuG4gIVQRyPCh1FXwAEEAAYASAAEgLimvD_BwE:G:s&s_kwcid=AL!739!3!260704327909!e!!g!!flipkart&semcmpid=sem_8024046704_brand_eta_goog";
		StringBuffer storeHtmlparseData = 	HTMLParsing(url);
		SpamModel sm=(SpamModel)PhishMailGuard.getAllInformationFromUrl(url);
		System.out.println(" Host "+sm.getHost());
		String filename=sm.getHost();
		StringHelper.writeStringBufferToFile(storeHtmlparseData,ServerConstants.PARSING_FILE+filename+".html.parse.txt");
		
	}
	public static StringBuffer HTMLParsing(String url) throws IOException {

		
		StringBuffer storeHtmlparseData = new StringBuffer();
		print("Fetching %s...", url);

		Document doc = Jsoup.connect(url).get();
		
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		print("\nMedia: (%d)", media.size());
		for (Element src : media) {
			if (src.tagName().equals("img")) {
				String s = print(" * %s: <%s> %sx%s (%s)", src.tagName(),
						src.attr("abs:src"), src.attr("width"),
						src.attr("height"), trim(src.attr("alt"), 20));
				storeHtmlparseData.append(s + "\n");
			
				/*
				 * System.err.println("-----------------------------");
				 * System.out.println("tag name ::" +src.tagName());
				 * System.out.println("attr ::" +src.attr("abs:src"));
				 * System.out.println("attr ::" +src.attr("width"));
				 * System.err.println("-----------------------------");
				 */
			}

			else {
				String s = print(" * %s: <%s>", src.tagName(),
						src.attr("abs:src"));
				storeHtmlparseData.append(s + "\n");
			}
		}

		print("\nImports: (%d)", imports.size());
		for (Element link : imports) {
			String s = print(" * %s <%s> (%s)", link.tagName(), link.attr("abs:href"),
					link.attr("rel"));
			storeHtmlparseData.append(s + "\n");
		}

		print("\nLinks: (%d)", links.size());
		for (Element link : links) {
			String s = 	print(" * a: <%s>  (%s)", link.attr("abs:href"),
					trim(link.text(), 35));
			storeHtmlparseData.append(s + "\n");
		}
		return storeHtmlparseData;
	}

	private static String print(String msg, Object... args) {

		String s = String.format(msg, args);
		System.out.println(s);
		return s;
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}
}