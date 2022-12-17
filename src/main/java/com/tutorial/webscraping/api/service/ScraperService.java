package com.tutorial.webscraping.api.service;

import com.tutorial.webscraping.api.model.ResponseDTO;

//github.com/somych1
public interface ScraperService {
    ResponseDTO getStatus(String caseId);
}
