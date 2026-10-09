package com.Nova.Merch.Store.Service;

import com.Nova.Merch.Store.Entity.StoreConfiguration;
import com.Nova.Merch.Store.Entity.Store;
import com.Nova.Merch.Store.Repository.StoreConfigurationRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsContructor
@Transactional(readOnly = true)
public class StoreConfigurationService{

  private final StoreConfigurationRepository storeConfigurationRepository;

  public StoreConfiguration getConfiguration(UUID storeId) {
    return storeConfigurationRepository
      .findByStore_Id(storeId)
      .orElseThrow(() ->
                  new IllegalArgumentException(
                    "Store configuration not found: " + storeId
                  )
                );
  }

  public boolean configurationExists(UUID storeId) {
    return storeConfigurationRepository
      .existsByStore_id(storeId);
  }
  
  @Transactional
  public StoreConfiguration createConfiguration(Store store) {

    if(store == null || store.getId() == null) {
      throw new IllegalArgumentException(
        "A persisted store is required"
      );
    }

    UUID storeId = store.getId();

    if(configurationExists(storeId)) {
      throw new IllegalStateException (
        "Configuration already exists for store: " + storeId
      );
    }

    StoreConfiguration configuration = new StoreConfiguration(store);
    
    return storeConfigurationRepository.save(configuration);
  }

@Transactional
public StoreConfiguration completeConfiguration(UUID storeId) {
  
  StoreConfiguration configuration = getConfiguration(storeId);

  if(!configuration.isConfigurationCompleted()) {
    configuration.markConfigurationCompleted();
  }
  return configuration;
}

@Transactional
public StoreConfiguration reopenConfiguration(UUID storeId) {
  
  StoreConfiguration configuration =
                getConfiguration(storeId);

  if (configuration.isConfigurationCompleted()) {
        configuration.markConfigurationIncomplete();
    }

  return configuration;
}


@Transactional
    public StoreConfiguration publishConfiguration(UUID storeId) {

        StoreConfiguration configuration =
                getConfiguration(storeId);

        configuration.markAsPublished();

        return configuration;
    }
}
