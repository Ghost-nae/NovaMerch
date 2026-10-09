package com.Nova.Merch.Store.Service;

import com.Nova.Merch.Store.Entity.Store;
import com.Nova.Merch.Store.Entity.StoreSettings;
import com.Nova.Merch.Store.Repository.StoreSettingsRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreSettingsService {
  private final StoreSettingsRepository storeSettingsRepository;

  public StoreSettings getSettings (UUID storeId) {
    return storeSettingsRepository
      .findByStore_Id(storeId)
      .orElseThrow(() -> 
                   new IllegalArgumentException(
                     "Store settings not found: " + storeId
                     )
                   );                                                 
  }

  public boolean settingsExist(UUID storeId) {
    return storeSettingsRepository.existsByStore_Id(storeId);
  }

  @Transactional
  public StoreSettings createSettings(Store store) {

        if (store == null || store.getId() == null) {
            throw new IllegalArgumentException(
                    "A persisted store is required."
            );
        }

        UUID storeId = store.getId();

        if (settingsExist(storeId)) {
            throw new IllegalStateException(
                    "Settings already exist for store: " + storeId
            );
        }

        StoreSettings settings = new StoreSettings(store);

        return storeSettingsRepository.save(settings);
    }

    @Transactional
    public StoreSettings updateCurrency(
            UUID storeId,
            String currency
    ) {

        if (currency == null) {
            throw new IllegalArgumentException(
                    "Currency is required."
            );
        }

        String normalizedCurrency =
                currency.trim().toUpperCase(Locale.ROOT);

        try {
            Currency.getInstance(normalizedCurrency);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid ISO currency code: " + currency,
                    ex
            );
        }

        StoreSettings settings = getSettings(storeId);

        settings.setDefaultCurrency(normalizedCurrency);

        return settings;
    }

    @Transactional
    public StoreSettings updateLanguage(
            UUID storeId,
            String language
    ) {

        if (language == null || language.isBlank()) {
            throw new IllegalArgumentException(
                    "Language is required."
            );
        }

        String normalizedLanguage = language.trim();

        if (!normalizedLanguage.matches(
                "(?i)[a-z]{2,3}(-[a-z]{2,4})?"
        )) {
            throw new IllegalArgumentException(
                    "Invalid language code: " + language
            );
        }

        StoreSettings settings = getSettings(storeId);

        settings.setDefaultLanguage(normalizedLanguage);

        return settings;
    }

    @Transactional
    public StoreSettings updateTimezone(
            UUID storeId,
            String timezone
    ) {

        if (timezone == null || timezone.isBlank()) {
            throw new IllegalArgumentException(
                    "Timezone is required."
            );
        }

        String normalizedTimezone = timezone.trim();

        try {
            ZoneId.of(normalizedTimezone);
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException(
                    "Invalid timezone: " + timezone,
                    ex
            );
        }

        StoreSettings settings = getSettings(storeId);

        settings.setTimezone(normalizedTimezone);

        return settings;
    }

    @Transactional
    public StoreSettings updateOrderPrefix(
            UUID storeId,
            String orderPrefix
    ) {

        if (orderPrefix == null || orderPrefix.isBlank()) {
            throw new IllegalArgumentException(
                    "Order prefix is required."
            );
        }

        String normalizedPrefix =
                orderPrefix.trim().toUpperCase(Locale.ROOT);

        if (!normalizedPrefix.matches("[A-Z0-9]{1,20}")) {
            throw new IllegalArgumentException(
                    "Order prefix must contain 1-20 letters or numbers."
            );
        }

        StoreSettings settings = getSettings(storeId);

        settings.setOrderPrefix(normalizedPrefix);

        return settings;
    }

    @Transactional
    public StoreSettings updateCustomerRegistration(
            UUID storeId,
            boolean enabled
    ) {

        StoreSettings settings = getSettings(storeId);

        settings.setCustomerRegistrationEnabled(enabled);

        return settings;
    }

    @Transactional
    public StoreSettings updateGuestCheckout(
            UUID storeId,
            boolean enabled
    ) {

        StoreSettings settings = getSettings(storeId);

        settings.setGuestCheckoutEnabled(enabled);

        return settings;
    }
}
