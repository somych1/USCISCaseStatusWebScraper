package com.tutorial.webscraping.api.model;

import lombok.Data;

import java.util.Date;

//github.com/somych1
@Data
public class ResponseDTO {
    String caseId;
    String status;
    String description;
    Date date;

}
