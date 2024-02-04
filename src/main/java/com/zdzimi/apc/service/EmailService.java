package com.zdzimi.apc.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.zdzimi.apc.controller.MainController;
import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.data.entity.Proposal;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public String sendHref(Contractor contractor) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setSubject(contractor.getCardCode().equals("0") ? "Admin panel." : "Sprawdź dostępność produktu.");
    mailMessage.setTo(contractor.getEmail());
    mailMessage.setText(String.format(
        contractor.getCardCode().equals("0") ?
            "Link do panelu administracyjnego: %s, ważny do %s." :
            "Sprawdź dostępność produktu pod adresem: %s. Link będzie aktywny do %s.",
        linkTo(MainController.class)
            .slash(
                contractor.getCardCode().equals("0") ? "admin" : "propose"
            )
            .slash(contractor.getCode().getCode())
            .withRel("proposal")
            .getHref(),
        contractor.getCode().getExpirationDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    ));
    mailSender.send(mailMessage);
    return contractor.getEmail();
  }

  public void sendMailsToAdmin(Contractor admin, Collection<Proposal> proposals) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setSubject("Wyszukiwania z ostatnich 10 min.");
    message.setTo(admin.getEmail());
    message.setText(String.format(
        "Link do panelu administracyjnego: %s, ważny do %s. Ostatnio wyszukiwane produkty: %s",
        linkTo(MainController.class)
            .slash("admin")
            .slash(admin.getCode().getCode())
            .withRel("admin")
            .getHref(),
        admin.getCode().getExpirationDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
        createListOBarcodes(proposals)
    ));
    mailSender.send(message);
  }

  private String createListOBarcodes(Collection<Proposal> proposals) {
    StringBuilder sb = new StringBuilder();
    proposals.forEach(p -> sb.append("\n").append(p.getBarcode()));
    return sb.toString();
  }

  public void sendAlreadyDeliveredMail(Proposal proposal) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(proposal.getContractor().getEmail());
    message.setSubject("Twój produkt został dostarczony.");
    message.setText(
        String.format(
            "Twój produkt o kodzie kreskowym: %s, wyszukiwany %s został dostarczony.",
            proposal.getBarcode(),
            proposal.getDateTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        )
    );
    mailSender.send(message);
  }

}
