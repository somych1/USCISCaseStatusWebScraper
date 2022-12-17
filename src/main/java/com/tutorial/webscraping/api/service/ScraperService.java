package com.tutorial.webscraping.api.service;

import com.tutorial.webscraping.api.model.ResponseDTO;

import java.util.List;

//github.com/somych1
public interface ScraperService {
    ResponseDTO getStatus(String caseId);

    List<ResponseDTO> getListOfCaseStatuses(String caseId, Integer range);
}
