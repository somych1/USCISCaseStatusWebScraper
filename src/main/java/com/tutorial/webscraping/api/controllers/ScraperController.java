package com.tutorial.webscraping.api.controllers;

import com.tutorial.webscraping.api.model.ResponseDTO;
import com.tutorial.webscraping.api.service.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//github.com/somych1
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/caseStatus")
public class ScraperController {

    @Autowired
    ScraperService service;

    @GetMapping(path = "/{caseId}")
    public ResponseDTO getCaseStatus(@PathVariable String caseId){
        return service.getStatus(caseId);
    }

    @GetMapping(path = "/getRange/{caseId}/{range}")
    public List<ResponseDTO> getListOfCases(@PathVariable String caseId, @PathVariable Integer range){
        return service.getListOfCaseStatuses(caseId, range);
    }
}
