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
import java.util.Queue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author michaeldvinci
 */
public class Parser {
    
    private SearchMe sm = new SearchMe();
    private Crawler c = new Crawler();
    private String url;
    
    /**
     * Adds links to queue for searching
     * 
     * @param link          search link
     * @param queueMax      total number of sites to search
     * @param urlQueue      queue containing links
     * @param domain        used for concatenating links
     * @param search        search variable
     * 
     */
    public void Parse(String link, int queueMax, Queue urlQueue, String domain, String search) {
        try { 
            Document docParse = Jsoup.connect(link).ignoreHttpErrors(true).followRedirects(true).userAgent("Chrome").timeout(0).get();
            Elements linkM = docParse.select("meta");
            for(Element linkM1 : linkM) {    
                url = linkM.attr("content");

                try { 
                    sm.addMLinks(urlQueue, link, domain);
                }
                catch(NullPointerException | IllegalArgumentException e) { }
            }
            
            sm.addALinks(urlQueue, link);

            sm.findString(link, search);
            
            parse2(queueMax,  link, urlQueue, domain, search);
        }
        catch(Exception e){ } 
    }
    
    /**
     * adds links to queue for searching
     * 
     * @param link          link to be searched
     * @param queueMax      total number of sites to search
     * @param urlQueue      queue containing links
     * @param domain        used for concatenating links
     * @param search        search variable
     * 
     */
    public void parse2(int queueMax, String link, Queue urlQueue, String domain, String search) throws IOException {
        for (int i = 0; i < (queueMax-1); i++) {

            System.out.print((i+2) + " ");
            
            try {
                sm.findString(link, search);
                sm.addALinks(urlQueue, link);
                Document docParse2 = Jsoup.connect(link).ignoreHttpErrors(true).followRedirects(true).userAgent("Chrome").timeout(0).get();
                Elements linkM2 = docParse2.select("meta");
                String url2 = linkM2.attr("content");

                if (sm.url2Test(url2)) {
                    sm.addMLinks(urlQueue, link, domain);
                }
            }
            catch(NullPointerException | IllegalArgumentException e) { }

            link = (String) urlQueue.poll();

            System.out.println(link);
            
            int g = c.queueTable.getRowCount();
        }
    }
    
}
