package com.Nova.Merch.Access.Bootstrap;

import com.Nova.Merch.Access.Service.PlatformOwnerBootstrapService;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        name = "novamerch.bootstrap.owner.enabled",
        havingValue = "true"
)
public class PlatformOwnerBootstrapRunner implements ApplicationRunner {

    private final PlatformOwnerBootstrapService bootstrapService;

    public PlatformOwnerBootstrapRunner(
            PlatformOwnerBootstrapService bootstrapService
    ) {
        this.bootstrapService = bootstrapService;
    }

    @Override
    public void run(ApplicationArguments args) {
        bootstrapService.bootstrap();
    }
}
