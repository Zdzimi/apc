package com.zdzimi.apc.controller;

import com.zdzimi.apc.data.CommodityProposal;
import com.zdzimi.apc.data.entity.Commodity;
import com.zdzimi.apc.data.entity.Contractor;
import com.zdzimi.apc.service.CodeInactiveException;
import com.zdzimi.apc.service.CommodityService;
import com.zdzimi.apc.service.ContractorsService;
import com.zdzimi.apc.service.EmailService;
import com.zdzimi.apc.service.ProposalService;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
  public String showClientPanel(@RequestParam String cardId, Model model) {
    model.addAttribute("email", emailService.sendHref(contractorsService.getContractorByCardCode(cardId)));
    return "code-sent";
  }

  @GetMapping("/propose/{code}")
  public String getProposalPanel(@PathVariable String code, Model model) {
    prepareModel(code, model);
    return "proposal";
  }

  @GetMapping("/admin/{code}")
  public String getAdminPanel(@PathVariable String code, @RequestParam(required = false) Integer days, Model model) {
    Contractor contractor = contractorsService.getContractorByCode(code);
    if (!contractor.getCardCode().equals("0")) {
      throw new CodeInactiveException(code);
    }
    contractorsService.updateAdminCode(contractor);
    model.addAttribute(code);
    model.addAttribute("info", days == null ? "Wszystkie zapytania:" : String.format("Zapytania z ostatnich %d dni:", days));
    model.addAttribute("summary", proposalService.getLastSummary(days));
    model.addAttribute("lastMonthProposals", proposalService.getLastMonthProposals());
    return "admin";
  }

  @GetMapping("/admin/product/{code}")
  public String getProposalsOfOneBarcode(@PathVariable String code, @RequestParam String barcode, Model model) {
    Contractor contractor = contractorsService.getContractorByCode(code);
    if (!contractor.getCardCode().equals("0")) {
      throw new CodeInactiveException(code);
    }
    contractorsService.updateAdminCode(contractor);
    model.addAttribute("proposals", proposalService.getAll(barcode));
    model.addAttribute("barcode", barcode);
    return "product-proposals";
  }

  @PostMapping("/propose/{code}")
  public String propose(@Valid @ModelAttribute CommodityProposal cp, BindingResult result, @PathVariable String code, Model model) {
    if (result.hasErrors()) {
      prepareModel(code, model);
      return "proposal";
    }
    Optional<Commodity> commodity = commodityService.checkProductAvailability(cp.getBarcode());
    if (commodity.isPresent()) {
      model.addAttribute("info", String.format(
          "Mamy %s %d szt.", commodity.get().getName(), (int) commodity.get().getAmount().getAmount()
      ));
    } else {
      proposalService.saveProposal(cp);
      model.addAttribute("info", String.format(
          "Nie mamy Twojego towaru, dodajemy go do listy zamówienia%s.",
          cp.isConsentToNotification() ? ", kiedy towar zostanie zamówiony poinformujemy Cię o tym" : ""
      ));
    }
    prepareModel(code, model);
    return "proposal";
  }

  private void prepareModel(@PathVariable String code,
      Model model) {
    Contractor contractor = contractorsService.getContractorByCode(code);
    model.addAttribute("code", code);
    model.addAttribute("proposal", createCommodityProposal(contractor.getId()));
    model.addAttribute("proposals", contractor.getProposals());
  }

  @PostMapping("delete-propose")
  public String deletePropose(@RequestParam long id, @RequestParam String code) {
    proposalService.delete(id);
    return String.format("redirect:/astra/propose/%s", code);
  }

  @PostMapping("delete-all")
  public String deleteAll(@RequestParam long contractorId, @RequestParam String code) {
    proposalService.deleteAll(contractorId);
    return String.format("redirect:/astra/propose/%s", code);
  }

  private CommodityProposal createCommodityProposal(Long contractorId) {
    CommodityProposal proposal = new CommodityProposal();
    proposal.setContractorId(contractorId);
    proposal.setConsentToNotification(true);
    return proposal;
  }

}
