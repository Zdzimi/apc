package com.zdzimi.apc.service;

import com.zdzimi.apc.data.entity.Commodity;
import com.zdzimi.apc.data.repository.CommoditiesRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommodityService {

  private final CommoditiesRepository repository;

  public Optional<Commodity> checkProductAvailability(String barcode) {
    return repository.findByBarcodeAtLeastAmount(barcode);
  }

}
