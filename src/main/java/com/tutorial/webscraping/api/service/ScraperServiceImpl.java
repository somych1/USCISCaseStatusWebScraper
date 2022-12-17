package com.tutorial.webscraping.api.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.tutorial.webscraping.api.model.ResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//github.com/somych1
@Service
public class ScraperServiceImpl implements ScraperService {

    //Reading data from property file to a list
    @Value("#{'${website.urls}'}")
    String url;

    @Override
    public ResponseDTO getStatus(String caseId) {
        ResponseDTO responseDTO = new ResponseDTO();

        //browser setup
        WebClient webClient = browserSetup();

        String serviceCenter = caseId.substring(0, 3);
        String caseNumber = caseId.substring(3);
        System.out.println(serviceCenter);
        System.out.println(caseNumber);

        try {
            // loading the HTML to a Document Object
            HtmlPage page = webClient.getPage(url);

            // case lookup
            HtmlInput input = page.getHtmlElementById("receipt_number");
            input.setValueAttribute(caseId);
            HtmlInput button = page.getElementByName("initCaseSearch");
            HtmlPage pageAfterClick = button.click();

            // new page after click
            HtmlHeading1 h1 = pageAfterClick.getFirstByXPath("//div/h1");
            HtmlParagraph paragraph = pageAfterClick.getFirstByXPath("//div/p");

            //setting responseDTO
            responseDTO = responseDTOBuilder(caseId, h1.getTextContent(), paragraph.getTextContent());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return responseDTO;
    }

    @Override
    public List<ResponseDTO> getListOfCaseStatuses(String caseId, Integer range) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<ResponseDTO> responseDTOList = new ArrayList<>();
        //browser setup
        WebClient webClient = browserSetup();

        // string split service center + 0
        String serviceCenter = caseId.substring(0, 4);

        // string split case number
        String caseNumber = caseId.substring(4);
        Integer startPosition = new Integer(caseNumber);

        for (int i = 0; i < range; i++) {

            try {
                // Increased case number
                String newCaseId = serviceCenter + (startPosition + i);

                // loading the HTML to a Document Object
                HtmlPage page = webClient.getPage(url);
                System.out.println(newCaseId);
                // case lookup
                HtmlInput input = page.getHtmlElementById("receipt_number");
                input.setValueAttribute(newCaseId);
                HtmlInput button = page.getElementByName("initCaseSearch");
                HtmlPage pageAfterClick = button.click();

                // new page after click
                HtmlHeading1 h1 = pageAfterClick.getFirstByXPath("//div/h1");
                HtmlParagraph paragraph = pageAfterClick.getFirstByXPath("//div/p");

                //setting responseDTO
                responseDTO = responseDTOBuilder(newCaseId, h1.getTextContent(), paragraph.getTextContent());
                responseDTOList.add(responseDTO);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return responseDTOList;
    }

    //browser setup
    private WebClient browserSetup() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(false);
        webClient.getOptions().setTimeout(60000);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setGeolocationEnabled(false);
        webClient.getOptions().setAppletEnabled(false);
        return webClient;
    }

    // response object builder
    private ResponseDTO responseDTOBuilder(String caseId, String status, String description) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCaseId(caseId);
        responseDTO.setStatus(status);
        responseDTO.setDescription(description);
        return responseDTO;
    }
}
