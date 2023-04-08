package com.action;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Address;

import org.apache.commons.net.DatabaseHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.helper.CSSModel;
import com.helper.DBUtils;
import com.helper.HttpsURLReader;
import com.helper.PhishMailGuard;
import com.helper.SpamModel;
import com.helper.StringHelper;
import com.helper.WebsiteCssTemplate;
import com.helper.WebsiteModel;

/**
 * Example program to list links from a URL.
 */
public class CrawlWebsite extends DatabaseHelper {
	public static ArrayList<WebsiteCssTemplate> websites=new ArrayList<WebsiteCssTemplate>();
	static{
		websites=(ArrayList<WebsiteCssTemplate>) DBUtils.getBeanList(WebsiteCssTemplate.class, "SELECT * FROM websitecsstemplates");
	}
	public static void main(String[] args) throws IOException {
//	boolean s=	checkIfPhishing("http://www.icicibank.com/");
//	System.out.println("Site is Phishing "+s);

//InetAddress addr = Address.getByName("www.dnsjava.org");
  
		 //store all the information in db
		
		WebsiteModel siteToBeChecked=createCSSTemplate("https://infinity.icicibank.com/corp/AuthenticationController?FORMSGROUP_ID__=AuthenticationFG&__START_TRAN_FLAG__=Y&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=ICI&ITM=nli_primer_login_btn_desk&_ga=2.167614205.217047310.1559732228-1997447441.1557489983", false);
		
		
	/*	WebsiteModel siteToBeChecked=createCSSTemplate("https://infinity.icicibank.com/corp/AuthenticationController?FORMSGROUP_ID__=AuthenticationFG&__START_TRAN_FLAG__=Y&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=ICI&ITM=nli_primer_login_btn_desk&_ga=2.167614205.217047310.1559732228-1997447441.1557489983", false);
		System.out.println("*********************");
        System.out.println("css containt "+siteToBeChecked);
		System.out.println("*********************");*/
		
	    //	fetchCssImports(siteToBeChecked);
		String url="http://akpro.tk/";
		url="http://localhost/icici/Log%20in%20to%20Internet%20Banking.htm";
//		url="https://infinity.icicibank.com/corp/AuthenticationController?FORMSGROUP_ID__=AuthenticationFG&__START_TRAN_FLAG__=Y&FG_BUTTONS__=LOAD&ACTION.LOAD=Y&AuthenticationFG.LOGIN_FLAG=1&BANK_ID=ICI&ITM=nli_primer_login_btn_desk&_ga=2.95739615.217047310.1559732228-1997447441.1557489983";
		
		SpamModel sm= PhishMailGuard.getAllInformationFromUrl(url);
		System.out.println(sm.isSpam());
		String result=checkIfPhishing(url);   
		 System.out.println("url :: "+result);
//		createCSSTemplate(url, false);
//		boolean result=checkIfPhishing("https://www.icicibank.com/nri-banking/nri-login.page");   
//		 System.out.println("result "+result);
//		 createCSSTemplate("http://akpro.tk/", true);
//		/*createCSSTemplate("http://www.maharastra.co.in/", false);*/
		/*createCSSTemplate("https://www.icicibank.com/nri-banking/interim/interim.page?", false);*/
		//createCSSTemplate("https://www.samsung.com/us/support/account/",false);
     	 /* boolean result=	checkIfPhishing("http://192.168.0.101/icici/");*/
//		 boolean result=	checkIfPhishing("https://www.innovation.ch/java/HTTPClient/advanced_info.html");
//     	 System.out.println("url :: "+result);
		  
	}
	
	  public static String checkIfPhishing(String url) {   
		URL u;
		String returnMessage="";
		boolean siteIsPhishing=false;
		ArrayList<WebsiteCssTemplate> matchedModelList=new ArrayList<WebsiteCssTemplate>();
		try
		{
			int DIFF=5;
			WebsiteModel siteToBeChecked=createCSSTemplate(url, true);	// compare=true means do not add to database
			ArrayList<CSSModel> arr= siteToBeChecked.getCssList();
			// CHeck for matching site from database START
			for (int i = 0; i < arr.size(); i++){
				CSSModel css=arr.get(i);
				int tagSelectorCount=css.getTagSelectorCount();
				int classSelectorCount=css.getClassSelectorCount();
				int idSelectorCount=css.getIdSelectorCount();
				int otherSelectorCount=css.getOtherSelectorCount();
				for (int j = 0; j < websites.size(); j++) 
				{
					WebsiteCssTemplate website=websites.get(j);
					int diff=-1;
					int comp=0;
					String q="Select * from (SELECT *,CAST(if(idCount>0,idCount-"+idSelectorCount+",0) as signed)+CAST(if(classCount>0,classCount-"+classSelectorCount+",0) as signed)+cast(if(htmlCount>0 ,htmlCount-"+tagSelectorCount+",0) as signed)+cast(if(otherCount>0 ,otherCount-"+otherSelectorCount+",0) as signed) as diff FROM websitecsstemplates) A where A.diff>=0 AND A.diff<="+DIFF+" AND (A.idCount+A.classCount+A.htmlCount+A.otherCount)>0 order by A.diff asc";
					List l =DBUtils.getBeanList(WebsiteCssTemplate.class, q);
					if(l.size()>0)
					{
						WebsiteCssTemplate website1=(WebsiteCssTemplate) l.get(0);
						System.out.println("websitename "+website1.getWebsitename());
						matchedModelList.add(website1);
					}
				}
			}		
			// CHeck for matching site from database END
			HashMap hosts=new HashMap();
			ArrayList data=new ArrayList();
			for (int i = 0; i < matchedModelList.size(); i++) {
				 WebsiteCssTemplate wct= matchedModelList.get(i);
				 if(hosts.get(wct.getWebsitename())==null)
				 data.add(new String[]{wct.getWebsitename(),wct.getWebsiteHost()});
			}
		
			for (int i = 0; i < data.size(); i++) {
				String[] websiteHost=(String[]) data.get(i);
				if(websiteHost[1].equalsIgnoreCase(siteToBeChecked.getWebsiteHost())){
					siteIsPhishing=false;
					returnMessage=siteIsPhishing+"#";
					break;
				}else{
					siteIsPhishing=true;
					System.out.println("Original Host "+websiteHost[1]+"Current Host "+siteToBeChecked.getWebsiteHost());
					System.out.println("Found Phishing Site ==> "+websiteHost[0]);
					returnMessage=siteIsPhishing+"#"+websiteHost[1]+"#"+siteToBeChecked.getWebsiteHost();
				}
			}
			
			
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return returnMessage;
	}
	public static WebsiteModel createCSSTemplate(String url,boolean compare) {
		CSSParser cpd = new CSSParser();
		WebsiteModel model = new WebsiteModel();
		model.setWebsiteUrl(url);
		ArrayList<String> cssFiles;
		try {
			cssFiles = fetchCssImports(model);
			model.setCssURLList(cssFiles);
			ArrayList<CSSModel> cssList = new ArrayList<CSSModel>();
			for (int i = 0; i < cssFiles.size(); i++) {
				String cssUrl = cssFiles.get(i);
				CSSModel csd = cpd.selectTagTypes(cssUrl);
				cssList.add(csd);
			}   
			model.setCssList(cssList);
			System.out.println("model ::"+model);
			if(!compare)
				updateCssTemplateDB(model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	public static void updateCssTemplateDB(WebsiteModel model){
		String websiteURL=model.getWebsiteUrl();
		int websiteDivCount=StringHelper.n2i(model.getWebsiteDivCount());
		ArrayList<CSSModel> cssList=model.getCssList();
		for (int i = 0; i < cssList.size(); i++) {
			CSSModel css = cssList.get(i);
			String query="insert into websitecsstemplates (websitename, styleSheet, idTags, classTags, htmlTags, otherTags, idCount, classCount, htmlCount, otherCount, divCount,cssUrl,websitetitle,websiteHost,copyright)";
			query+=" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			DBUtils.executeUpdate(query, new Object[]{
					websiteURL,
					css.getCssName(),
					css.getIdSelector(),
					css.getClassSelector(),
					css.getTagSelector(),
					css.getOtherSelector(),
					css.getIdSelectorCount(),
					css.getClassSelectorCount(),
					css.getTagSelectorCount(),
					css.getOtherSelectorCount(),
					websiteDivCount,css.getCssUrl(),model.getWebsiteTitle(),model.getWebsiteHost(),model.getCopyRight()
			});			
		}
	}
	public static String step1_RemoveHTML(String str){
//		System.out.print("Before  "+str);
		str = str.replaceAll("\\<.*?\\>", "");
		str = str.replaceAll("&.*?;","");
		
//		System.out.println("After "+str);
		return str;
	}

    public static  ArrayList<String> fetchCssImports(WebsiteModel model) throws IOException {
    	 String titleString="";        
        ArrayList<String> cssFiles=new ArrayList<String>();
        String url = model.getWebsiteUrl();
        print("Fetching %s...", url);
        URL u=new URL(url);
        try{
        InetAddress address = InetAddress.getByName(u.getHost());
        
        model.setWebsiteHost(StringHelper.getIpAddress(address.toString()) );
        }catch (Exception e) {
			// TODO: handle exception
		}
        Document doc =null;
        if(url.toLowerCase().startsWith("http:")){
        //doc = Jsoup.connect(url).get();
        
        doc = Jsoup.connect(url).get();
     
        }
        else if(url.toLowerCase().startsWith("https:")){
        	doc = Jsoup.parse(HttpsURLReader.readHttpsURL(url).toString());
            
        
        }else{
        	doc = Jsoup.connect(url).get(); 
        }
        Elements title = doc.select("title");
        
        try{
        	titleString=step1_RemoveHTML(title.get(0).toString());
        	model.setWebsiteTitle(titleString);
        }catch (Exception e) {
        	e.printStackTrace();
		}
        String copyRight="";
//        Elements copyright= doc.getElementsContainingText("Copyright");
//        try{
//        	if(copyright==null||copyright.size()==0){
//        		copyright= doc.getElementsContainingText("©");
//        	}
//        	copyRight=step1_RemoveHTML(copyright.get(0).toString());
//        	copyRight=copyRight.toLowerCase();
//        	copyRight=copyRight.replaceAll("copyright", "");
//        	copyRight=copyRight.replaceAll("all rights reserved", "");
////        	copyRight=copyRight.replaceAll(".", "");
//        	copyRight=copyRight.replaceAll("©", "");
//        	model.setCopyRight(copyRight);
//        }catch (Exception e) {
//        	e.printStackTrace();
//		}
//        
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

//        print("\nMedia: (%d)", media.size());
//        for (Element src : media) {
//            if (src.tagName().equals("img"))
//                print(" * %s: <%s> %sx%s (%s)",
//                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//                        trim(src.attr("alt"), 20));
//            else
//                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//        }

//        print("\nImports: (%d)", imports.size());
//        for (Element link : imports) {
//            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
//        }

//        print("\nLinks: (%d)", links.size());
      
        for (Element link : imports) {
        	String l=link.attr("abs:href");
        	if(l.indexOf(".css")!=-1)
        	{
        	String links1[]=l.split("&");
        	String httpLink="";
        		for (int i = 0; i < links1.length; i++) {
        			String lnk=links1[i];
//					System.out.println("links1[i] "+links1[i]);
					if(links1[i].startsWith("http")){
						httpLink=links1[i];
						cssFiles.add(httpLink); 
					}
					else
					{
//						if(lnk.contains("/"))
//						{
//							System.out.println("Link String at CrawlWebsite : "+lnk); 
							String part=lnk.substring(0,lnk.indexOf("/"));
							String subpart=httpLink.substring(0,httpLink.indexOf(part+"/"))+lnk;
							System.out.println("Sub-Part : "+subpart);
							System.out.println("Part : "+subpart);
							cssFiles.add(subpart);
//						}           
					}           
				}
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        	}
        }
        for (Element link : imports) {
        	String l=link.attr("href");
        	if(l.indexOf(".css")!=-1){
        		String mainURL=url.substring(0,url.lastIndexOf("/")+1);
        		cssFiles.add(mainURL+l);
				}
            print(" * a: <%s>  (%s)", link.attr("abs:href"),trim(link.text(), 35));
        	}
        System.out.println(cssFiles.size());
        return cssFiles;
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}