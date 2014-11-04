/*
 * The MIT License (MIT)

 * Copyright (c) 2014 Michael D. Vinci

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package WebCrawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static WebCrawler.Crawler.model;
import static WebCrawler.Crawler.model2;

/**
 *
 * @author michaeldvinci
 */
public class SearchMe {
    
    private String linkSM;
    private String linkSMPrev;
    private ArrayList<String> alS;
 
    /**
     * Searches html for a specific string
     * 
     * @param link          link to be searched
     * @param search        search string
     * @throws IOException
     */
    public void findString(String link, String search) throws IOException {
        try {
            Document docParse = Jsoup.connect(link).userAgent("Chrome").ignoreHttpErrors(true).followRedirects(true).timeout(0).get();

            if (docParse.text().contains(search)) {
                Object[] row = {link};
                model.addRow(row);
            }
        }
        catch(NullPointerException | MalformedURLException | IllegalArgumentException e) { }
    }
    
    /**
     * Adds links based on the <a> tag
     *
     * @param qu            queue of links
     * @param link          link to be searched
     * @throws IOException
     */
    public void addALinks(Queue qu, String link) throws IOException {
        Document docParse = Jsoup.connect(link).ignoreHttpErrors(true).followRedirects(true).userAgent("Chrome").timeout(0).get();
        
        String linkSPrev = "";
        Elements linkE = docParse.select("a");
            
        for (Element linkE1 : linkE) {
            String linkS = linkE1.attr("abs:href");
            if ((linkS != null) && (!linkS.contains("#")) && (linkS.equals("") != true) && (linkS.contains("@") != true) && (linkS.equals(linkSPrev) != true)) {
                if (!qu.contains(linkS)) {
                    qu.offer(linkS);
                    Object[] row2 = {linkS};
                    model2.addRow(row2);
                    linkSPrev = linkS;
                }
            }
        }
    }
    
    /**
     * tests string to see if it's a valid url
     * 
     * @param url2          url string
     * @return              boolean return
     */
    public boolean url2Test(String url2) {
         if ((url2.contains(",") != true)) {
            if (url2.contains(".com") || url2.contains(".html") || url2.contains(".edu")) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * tests link for validity
     * 
     * @param linkT         link to be validated
     * @return              boolean value
     */
    public boolean linkTest(String linkT) {
        return linkT.contains(".com") || linkT.contains(".edu") || linkT.contains(".net") || linkT.contains(".org") || linkT.contains(".gov") || linkT.contains(".uk");
    }
    
    /**
     * adds links with <meta> tag to queue
     *
     * @param qu            link queue
     * @param link          link to be searched
     * @param domain        domain string for concatenation
     * @throws IOException
     */
    public void addMLinks(Queue qu, String link, String domain) throws IOException {
        Document docParse = Jsoup.connect(link).ignoreHttpErrors(true).followRedirects(true).userAgent("Chrome").timeout(0).get();
        Elements linkM = docParse.select("meta");
        linkSMPrev = "";

        try {
            for (Element linkM1 : linkM) {
                String url = linkM1.attr("content");

                if ((url.contains(";") == true)) {
                    String urlRedirect = url.split(";")[1];

                    if (urlRedirect.contains(" ")) {
                        linkSM = urlRedirect.split("= ")[1].replace("'","");
                        concatLinkSM(link);
                    }
                    else {
                        linkSM = urlRedirect.split("=")[1].replace("'","");
                        concatLinkSM(link);
                    }
                }
            
                if((linkSM.contains("#") != true) && (linkSM.equals("") != true) && (linkSM.contains("@") != true)) {
                    if (!qu.contains(linkSM)) {
                        qu.offer(linkSM);
                        Object[] row2 = {linkSM};
                        model2.addRow(row2);
                        linkSMPrev = linkSM;    
                    }
                }
            }
        }
            
         catch(Exception e) { }
    }
    
    /**
     * make viable links from concatenation
     * 
     * @param domain        domain string to concatenate
     */
    public void concatLinkSM(String domain) {
        if ((linkSM.contains(domain) != true) && (linkSM.contains(".com") != true) && (linkSM.contains("UTF-8") != true)) {
            linkSM = domain + "/" + linkSM;
        }
        
    }
    
}
