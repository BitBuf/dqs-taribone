package dev.dewy.taribone.utils;

import java.util.Arrays;
import java.util.List;

public final class Config
{
    public Taribone taribone = new Taribone();

    private transient boolean donePostLoad = false;

    public synchronized Config doPostLoad()
    {
        if (this.donePostLoad)
        {
            throw new IllegalStateException("Config post-load is already done!");
        }

        this.donePostLoad = true;

        return this;
    }

    public static final class Taribone
    {
        public String serverIp = "default";

        public String token = "default";

        public String operatorId = "326039530971070474";
        public String subscriberId = "default";
        public String channelId = "default";

        public String tiedIgn = "default";
        public String tiedUuid = "default";

        public boolean focused = true;
        public boolean focusEnabled = false;

        public List<String> accounts = Arrays.asList(
                "default",
                "jimblo"
        );
    }
}
