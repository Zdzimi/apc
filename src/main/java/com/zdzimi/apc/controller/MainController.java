package com.zdzimi.apc.controller;

import com.zdzimi.apc.data.CommodityProposal;
import com.zdzimi.apc.data.entity.Commodity;
import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.service.CodeInactiveException;
import com.zdzimi.apc.service.CommodityService;
import com.zdzimi.apc.service.ContractorsService;
import com.zdzimi.apc.service.EmailService;
import com.zdzimi.apc.service.ProposalService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/astra")
@RequiredArgsConstructor
public class MainController {

  private final EmailService emailService;
  private final ContractorsService contractorsService;
  private final CommodityService commodityService;
  private final ProposalService proposalService;

  @GetMapping
  public String showPassingCardIdForm() {
    return "index";
  }

  @GetMapping("/code-sent")
  public String showClientPanel(@RequestParam String cardId) {
    emailService.sendHref(contractorsService.getContractorByCardCode(cardId));
    return "code-sent";
  }

  @GetMapping("/propose/{code}")
  public String getProposalPanel(@PathVariable String code, Model model) {
    Contractor contractor = contractorsService.getContractorByTenMinuteCode(code);
    model.addAttribute("code", code);
    model.addAttribute("proposal", createCommodityProposal(contractor.getId()));
    model.addAttribute("proposals", contractor.getProposals());
    return "proposal";
  }

  @GetMapping("/admin/{code}")
  public String getAdminPanel(@PathVariable String code, @RequestParam(required = false) Integer days, Model model) {
    Contractor contractor = contractorsService.getContractorByTenMinuteCode(code);
    if (!contractor.getCardCode().equals("0")) {
      throw new CodeInactiveException(code);
    }
    model.addAttribute(code);
    model.addAttribute("info", days == null ? "Wszystkie zapytania:" : String.format("Zapytania z ostatnich %d dni:", days));
    model.addAttribute("summary", proposalService.getLastSummary(days));
    model.addAttribute("lastMonthProposals", proposalService.getLastMonthProposals());
    return "admin";
  }

  @PostMapping("/propose/{code}")
  public String propose(@PathVariable String code, @ModelAttribute CommodityProposal cp, Model model) {
    Optional<Commodity> commodity = commodityService
        .checkProductAvailability(cp.getBarcode(), cp.getNeededAmount());
    if (commodity.isPresent()) {
      model.addAttribute("info", String.format(
          "Mamy %s %d szt.", commodity.get().getName(), (int) commodity.get().getAmount().getAmount()
      ));
    } else {
      proposalService.saveProposal(cp);
      model.addAttribute("info", String.format(
          "Dodaliśmy Twój towar do listy do zamówienia%s.",
          cp.isConsentToNotification() ? ", jeżeli towar zostanie zamówiony poinformujemy cię o tym" : ""
      ));
    }
    Contractor contractor = contractorsService.getContractorByTenMinuteCode(code);
    model.addAttribute("code", code);
    model.addAttribute("proposal", createCommodityProposal(contractor.getId()));
    model.addAttribute("proposals", contractor.getProposals());
    return "proposal";
  }

  @PostMapping("delete-propose")
  public String deletePropose(@RequestParam long id, @RequestParam String code) {
    proposalService.delete(id);
    return String.format("redirect:/astra/propose/%s", code);
  }

  private CommodityProposal createCommodityProposal(Long contractorId) {
    CommodityProposal proposal = new CommodityProposal();
    proposal.setContractorId(contractorId);
    proposal.setNeededAmount(1);
    proposal.setConsentToNotification(true);
    return proposal;
  }

}
