package com.zdzimi.apc.controller;

import com.zdzimi.apc.service.CodeInactiveException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class CodeInactiveAdvice {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(CodeInactiveException.class)
  public String codeInactiveHandler(CodeInactiveException e, Model model) {
    model.addAttribute("exception", e.getMessage());
    return "code-inactive";
  }

}
