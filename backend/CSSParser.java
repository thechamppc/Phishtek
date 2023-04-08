package com.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.helper.CSSModel;
import com.helper.HttpsURLReader;
import com.steadystate.css.parser.CSSOMParser;

public class CSSParser {
	public static void main(String[] args) {
		/*
		 * selectTagTypes(
		 * "D:\\work\\project\\TaxiRecommender\\TFinderWeb\\WebContent\\theme\\css\\style.css"
		 * );
		 */
		/*
		 * loadCSSStyle(
		 * "https://cdn2.f-cdn.com/build/css/flux/base.css?	=bd758ccaf182bd34a6b6f8b4a64e67d5"
		 * );
		 */

		/* loadCSSStyle("C:\\Users\\technowings\\Desktop\\css\\test.css"); */
		// loadCSSStyle("http://192.168.0.101/icici");
		/* showCSSStyle("http://192.168.0.101/icici"); */
		// loadCSSStyle("https://www.yesbank.in/template.css");
		// selectTagTypes("https://www.bankofbaroda.co.in/1.css");
		// selectTagTypes("https://www.icicibank.com/?utm_source=google&utm_medium=map&utm_content=A9773&utm_campaign=ICICI-Bank-ATM");
		// selectTagTypes("https://www.icicibank.com/online-safe-banking/phishing.page");
		// showCSSStyle("https://www.bankofbaroda.co.in/1.css");
 	   //    selectTagTypes("https://www.icicibank.com/nri-banking/interim/interim.page?");
 	       selectTagTypes("https://www.icicibank.com/assets/css/nli-index-custom.min.css");
		// showCSSStyle("https://www.icicibank.com/nri-banking/interim/interim.page?print.css"); 
		 //selectTagTypes("http://www.hdfcbank.com/homepage.css"); 

	}

	public static CSSModel selectTagTypes(String cssFilePath) {
		CSSModel css = new CSSModel();
		ArrayList<String> styleList = new ArrayList<String>();
		css.setCssUrl(cssFilePath);
		styleList = loadCSSStyle(cssFilePath);
		String cssName = "";
		if (cssFilePath.indexOf("\\") != -1) {
			cssName = cssFilePath.substring(cssFilePath.lastIndexOf("\\") + 1);
		} else if (cssFilePath.indexOf("/") != -1) {
			cssName = cssFilePath.substring(cssFilePath.lastIndexOf("/") + 1);
		}
		System.out.println("csss :: file name " + cssName);
		css.setCssName(cssName);
		ArrayList<String> tagSelector = new ArrayList<String>();
		ArrayList<String> classSelector = new ArrayList<String>();
		ArrayList<String> idSelector = new ArrayList<String>();
		ArrayList<String> otherSelector = new ArrayList<String>();

		for (Iterator iterator = styleList.iterator(); iterator.hasNext();) {
			String styleSelector1 = (String) iterator.next();
			String[] selectorComponents = styleSelector1.split("\\s*,\\s*");
			for (int i = 0; i < selectorComponents.length; i++) {
				String styleSelector = selectorComponents[i];
				if (styleSelector.indexOf("#") != -1) { // id selector
					idSelector.add(styleSelector);
				} else if (styleSelector.indexOf(".") != -1) { // class selector
					classSelector.add(styleSelector);
				} else if ((styleSelector.indexOf(":") != -1)
						|| (styleSelector.indexOf("=") != -1)
						|| (styleSelector.indexOf(">") != -1)
						|| (styleSelector.indexOf(">") != -1)
						|| (styleSelector.indexOf("~") != -1)) { // other
																	// selector
					otherSelector.add(styleSelector);
				} else { // tag selector
					tagSelector.add(styleSelector);
				}
			}

		}

		String tagsCSV = tagSelector.toString()
				.substring(1, tagSelector.toString().length() - 1)
				.replace(", ", ",");
		String classesCSV = classSelector.toString()
				.substring(1, classSelector.toString().length() - 1)
				.replace(", ", ",");
		String idsCSV = idSelector.toString()
				.substring(1, idSelector.toString().length() - 1)
				.replace(", ", ",");
		String othersCSV = otherSelector.toString()
				.substring(1, otherSelector.toString().length() - 1)
				.replace(", ", ",");

		css.setClassSelector(classesCSV);
		css.setClassSelectorCount(classSelector.size());

		css.setTagSelector(tagsCSV);
		css.setTagSelectorCount(tagSelector.size());

		css.setIdSelector(idsCSV);
		css.setIdSelectorCount(idSelector.size());

		css.setOtherSelector(othersCSV);
		css.setOtherSelectorCount(otherSelector.size());

		System.out.println("tagSelector " + tagSelector.size());
		System.out.println("classSelector " + classSelector.size());
		System.out.println("idSelector " + idSelector.size());
		System.out.println("otherSelector " + otherSelector.size());
		return css;
	}

	public static ArrayList<String> loadCSSStyle(String filePath) {
		ArrayList<String> styleList = new ArrayList<String>();
		try {

			InputStream stream = null;
			System.out.println("LOading CSS " + filePath);
			if (filePath.startsWith("http:")) {
				/* stream = new URL(filePath).openStream(); */
				// stream = new FileInputStream(new File(filePath));
				stream = HttpClient.getHTTPContentStream(filePath);
				// wrap as an InputSource

			} else if (filePath.startsWith("https:")) {
                
				stream = HttpClient.getHTTPSStream(filePath);

			} else {
				stream = new FileInputStream(new File(filePath));
			}
			// wrap as an InputSource
			InputSource source = new InputSource(new InputStreamReader(stream));

			// instantiate a parser
			CSSOMParser parser = new CSSOMParser();
			CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null,
					null);
			CSSRuleList ruleList = stylesheet.getCssRules();
			for (int i = 0; i < ruleList.getLength(); i++) {
				CSSRule rule = ruleList.item(i);
				if (rule instanceof CSSStyleRule) {
					CSSStyleRule styleRule = (CSSStyleRule) rule;
					// System.out.println("selector: "+
					// styleRule.getSelectorText());
					String[] selectorComponents = styleRule.getSelectorText()
							.split("\\s*,\\s*");
					// for (int j = 0; j < selectorComponents.length; j++) {
					// System.out.println("====> "+selectorComponents[j]);
					// }
					styleList.add(styleRule.getSelectorText());

					CSSStyleDeclaration styleDeclaration = styleRule.getStyle();

					// for (int j = 0; j < styleDeclaration.getLength();
					// j++) {
					// String property = styleDeclaration.item(j);
					// System.out.println("property: " + property);
					// System.out.println("value: "+
					// styleDeclaration.getPropertyCSSValue(property).getCssText());
					// }

				}
			}
		} catch (Exception t) {
			t.printStackTrace();
		}
		return styleList;
	}

	public static void showCSSStyle(String filePath) {
		try {
			InputStream stream = null;

			if (!filePath.startsWith("http:")) {
				// stream = new FileInputStream(new File(filePath));
				stream = HttpClient.getHTTPContentStream(filePath);
				// wrap as an InputSource
			} else {
				// stream = new URL(filePath).openStream();
				stream = HttpClient.getHTTPSStream(filePath);
			}
			InputSource source = new InputSource(new InputStreamReader(stream));

			// instantiate a parser
			CSSOMParser parser = new CSSOMParser();
			CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null,
					null);

			CSSRuleList ruleList = stylesheet.getCssRules();
			for (int i = 0; i < ruleList.getLength(); i++) {
				CSSRule rule = ruleList.item(i);

				if (rule instanceof CSSStyleRule) {

					CSSStyleRule styleRule = (CSSStyleRule) rule;

					System.out.println("selector: "
							+ styleRule.getSelectorText());

					CSSStyleDeclaration styleDeclaration = styleRule.getStyle();

					for (int j = 0; j < styleDeclaration.getLength(); j++) {

						String property = styleDeclaration.item(j);

						System.out.println("property: " + property);

						System.out.println("value: "
								+ styleDeclaration
										.getPropertyCSSValue(property)
										.getCssText());

					}
				}
			}
		} catch (Exception t) {
			t.printStackTrace();
		}
	}
}
