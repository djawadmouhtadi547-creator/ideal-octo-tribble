package com.thelastplayer;

import net.minecraftforge.common.ForgeConfigSpec;

public class EchoConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue AI_ENABLED;
    public static final ForgeConfigSpec.ConfigValue<String> AI_ENDPOINT;
    public static final ForgeConfigSpec.ConfigValue<String> AI_API_KEY;
    public static final ForgeConfigSpec.ConfigValue<String> AI_MODEL;
    public static final ForgeConfigSpec.IntValue AI_TIMEOUT;
    public static final ForgeConfigSpec.IntValue AI_COOLDOWN;
    public static final ForgeConfigSpec.IntValue AI_MAX_CHARS;

    public static final ForgeConfigSpec.BooleanValue MUSIC_ENABLED;
    public static final ForgeConfigSpec.BooleanValue HINT_SIGNAL;
    public static final ForgeConfigSpec.BooleanValue SACRIFICE_INVENTORY;
    public static final ForgeConfigSpec.IntValue FORCE_DAY_TICKS;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("ECHO's chat brain (side conversations only. Main story scenes are always scripted).").push("ai");
        AI_ENABLED = b.comment("Enable AI replies. If false or if the API fails, ECHO uses pre-written fallback lines.")
                .define("enabled", true);
        AI_ENDPOINT = b.comment("Any OpenAI-compatible /chat/completions endpoint (OpenAI, OpenRouter, Groq, Ollama: http://localhost:11434/v1/chat/completions ...)")
                .define("endpoint", "https://api.openai.com/v1/chat/completions");
        AI_API_KEY = b.comment("API key. Leave empty to read the ECHO_API_KEY environment variable instead (recommended).")
                .define("apiKey", "");
        AI_MODEL = b.define("model", "gpt-4o-mini");
        AI_TIMEOUT = b.comment("Seconds before giving up on the API.").defineInRange("timeoutSeconds", 20, 3, 120);
        AI_COOLDOWN = b.comment("Minimum seconds between two AI replies.").defineInRange("cooldownSeconds", 3, 0, 60);
        AI_MAX_CHARS = b.comment("Max characters of an AI reply shown in chat.").defineInRange("maxChars", 220, 40, 600);
        b.pop();

        b.push("story");
        MUSIC_ENABLED = b.define("music", true);
        HINT_SIGNAL = b.comment("Show a faint directional hint to the secret room before the story starts.")
                .define("hintSignal", true);
        SACRIFICE_INVENTORY = b.comment("If true the final sacrifice takes the whole inventory (except the 3 story items). If false it only takes XP and hunger.")
                .define("sacrificeInventory", true);
        FORCE_DAY_TICKS = b.comment("A story day is force-advanced after this many ticks even if the player never sleeps.")
                .defineInRange("forceDayTicks", 36000, 12000, 240000);
        b.pop();

        SPEC = b.build();
    }

    public static String apiKey() {
        String k = AI_API_KEY.get();
        if (k == null || k.trim().isEmpty()) {
            String env = System.getenv("ECHO_API_KEY");
            return env == null ? "" : env.trim();
        }
        return k.trim();
    }
}
