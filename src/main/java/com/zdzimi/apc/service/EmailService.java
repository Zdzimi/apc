package com.zdzimi.apc.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.zdzimi.apc.controller.MainController;
import com.zdzimi.apc.data.entity.Contractor;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendHref(Contractor contractor) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom("astra@noreply.pl");
    mailMessage.setSubject(contractor.getCardCode().equals("0") ? "Admin panel." : "Zgłoś brakujący produkt.");
    mailMessage.setTo(contractor.getEmail());
    mailMessage.setText(String.format(
        contractor.getCardCode().equals("0") ?
            "Link do panelu administracyjnego: %s, ważny do %s." :
            "Zgłoś brakujący produkt pod adresem: %s. Link będzie aktywny do %s.",
        linkTo(MainController.class)
            .slash(
                contractor.getCardCode().equals("0") ? "admin" : "propose"
            )
            .slash(contractor.getCode().getCode())
            .withRel("proposal")
            .getHref(),
        contractor.getCode().getCreationDateTime().plusMinutes(10).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    ));
    mailSender.send(mailMessage);
  }

}
