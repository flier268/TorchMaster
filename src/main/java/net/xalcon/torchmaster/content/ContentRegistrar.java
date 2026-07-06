package net.xalcon.torchmaster.content;

import net.xalcon.torchmaster.platform.Services;

public interface ContentRegistrar
{
    ContentRegistrar INSTANCE = Services.load(ContentRegistrar.class);

    ContentRegistration register();
}
