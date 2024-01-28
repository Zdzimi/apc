package com.zdzimi.apc.controller;

import com.zdzimi.apc.service.ContractorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class ContractorNotFoundAdvice {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ContractorNotFoundException.class)
  public String contractorNotFoundHandler(ContractorNotFoundException e, Model model) {
    model.addAttribute("exception", e.getMessage());
    return "contractor-not-found";
  }

}
