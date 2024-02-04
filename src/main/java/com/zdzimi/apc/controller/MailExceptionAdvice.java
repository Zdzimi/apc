package com.zdzimi.apc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class MailExceptionAdvice {

  @ExceptionHandler(MailException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String mailExceptionHandler(MailException e, Model model) {
    model.addAttribute("exception", e.getMessage());
    return "mail-error";
  }

}
