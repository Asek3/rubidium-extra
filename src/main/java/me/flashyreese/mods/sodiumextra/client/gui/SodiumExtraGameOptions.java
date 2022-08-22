package me.flashyreese.mods.sodiumextra.client.gui;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

public class SodiumExtraGameOptions {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Identifier.class, new Identifier.Serializer())
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .create();
    public final AnimationSettings animationSettings = new AnimationSettings();
    public final ParticleSettings particleSettings = new ParticleSettings();
    public final DetailSettings detailSettings = new DetailSettings();
    public final RenderSettings renderSettings = new RenderSettings();
    public final ExtraSettings extraSettings = new ExtraSettings();
    public final NotificationSettings notificationSettings = new NotificationSettings();
    private File file;
    private boolean suggestedRSO;

    public static SodiumExtraGameOptions load(File file) {
        SodiumExtraGameOptions config;

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                config = gson.fromJson(reader, SodiumExtraGameOptions.class);
            } catch (IOException e) {
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            config = new SodiumExtraGameOptions();
        }

        config.file = file;
        config.suggestedRSO = false;
        config.writeChanges();

        return config;
    }

    public void writeChanges() {
        File dir = this.file.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("Could not create parent directories");
            }
        } else if (!dir.isDirectory()) {
            throw new RuntimeException("The parent file is not a directory");
        }

        try (FileWriter writer = new FileWriter(this.file)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not save configuration file", e);
        }
    }

    public boolean hasSuggestedRSO() {
        return this.suggestedRSO;
    }

    public void setSuggestedRSO(boolean suggestedRSO) {
        this.suggestedRSO = suggestedRSO;
    }

    public enum OverlayCorner implements TextProvider {
        TOP_LEFT("sodiumextra.option.overlay_corner.top_left"),
        TOP_RIGHT("sodiumextra.option.overlay_corner.top_right"),
        BOTTOM_LEFT("sodiumextra.option.overlay_corner.bottom_left"),
        BOTTOM_RIGHT("sodiumextra.option.overlay_corner.bottom_right");

        private final Text text;

        OverlayCorner(String text) {
            this.text = new TranslatableText(text);
        }

        @Override
        public String getLocalizedName() {
            return this.text.getString();
        }
    }

    public enum TextContrast implements TextProvider {
        NONE("sodiumextra.option.text_contrast.none"),
        BACKGROUND("sodiumextra.option.text_contrast.background"),
        SHADOW("sodiumextra.option.text_contrast.shadow");

        private final Text text;

        TextContrast(String text) {
            this.text = new TranslatableText(text);
        }

        @Override
        public String getLocalizedName() {
            return this.text.getString();
        }
    }

    public enum VerticalSyncOption implements TextProvider {
        OFF("options.off"),
        ON("options.on"),
        ADAPTIVE("sodium-extra.option.use_adaptive_sync.name", GLFW.glfwExtensionSupported("GLX_EXT_swap_control_tear") || GLFW.glfwExtensionSupported("WGL_EXT_swap_control_tear"));

        private final Text name;
        private final boolean supported;

        VerticalSyncOption(String name) {
            this(name, true);
        }

        VerticalSyncOption(String name, boolean supported) {
            this.name = new TranslatableText(name);
            this.supported = supported;
        }

        public static VerticalSyncOption[] getAvailableOptions() {
            return Arrays.stream(VerticalSyncOption.values()).filter((o) -> o.supported).toArray(VerticalSyncOption[]::new);
        }

        @Override
        public String getLocalizedName() {
            return this.name.getString();
        }
    }

    public static class AnimationSettings {
        public boolean animation;
        public boolean water;
        public boolean lava;
        public boolean fire;
        public boolean portal;
        public boolean blockAnimations;

        public AnimationSettings() {
            this.animation = true;
            this.water = true;
            this.lava = true;
            this.fire = true;
            this.portal = true;
            this.blockAnimations = true;
        }
    }

    public static class ParticleSettings {
        public boolean particles;
        public boolean rainSplash;
        public boolean blockBreak;
        public boolean blockBreaking;
        @SerializedName("other")
        public Map<Identifier, Boolean> otherMap;

        public ParticleSettings() {
            this.particles = true;
            this.rainSplash = true;
            this.blockBreak = true;
            this.blockBreaking = true;
            this.otherMap = new Object2BooleanArrayMap<>();
        }
    }

    public static class DetailSettings {
        public boolean sky;
        public boolean sunMoon;
        public boolean stars;
        public boolean rainSnow;
        public boolean biomeColors;
        public boolean skyColors;

        public DetailSettings() {
            this.sky = true;
            this.sunMoon = true;
            this.stars = true;
            this.rainSnow = true;
            this.biomeColors = true;
            this.skyColors = true;
        }
    }

    public static class RenderSettings {
        public int fogDistance;
        public boolean lightUpdates;
        public boolean itemFrame;
        public boolean armorStand;
        public boolean painting;
        public boolean piston;
        public boolean beaconBeam;
        public boolean enchantingTableBook;
        public boolean itemFrameNameTag;
        public boolean playerNameTag;

        public RenderSettings() {
            this.fogDistance = 0;
            this.lightUpdates = true;
            this.itemFrame = true;
            this.armorStand = true;
            this.painting = true;
            this.piston = true;
            this.beaconBeam = true;
            this.enchantingTableBook = true;
            this.itemFrameNameTag = true;
            this.playerNameTag = true;
        }
    }

    public static class ExtraSettings {
        public OverlayCorner overlayCorner;
        public TextContrast textContrast;
        public boolean showFps;
        public boolean showFPSExtended;
        public boolean showCoords;
        public boolean reduceResolutionOnMac;
        public boolean useAdaptiveSync;
        public int cloudHeight;
        public boolean toasts;
        public boolean instantSneak;
        public boolean preventShaders;
        public boolean useFastRandom;

        public ExtraSettings() {
            this.overlayCorner = OverlayCorner.TOP_LEFT;
            this.textContrast = TextContrast.NONE;
            this.showFps = false;
            this.showFPSExtended = true;
            this.showCoords = false;
            this.reduceResolutionOnMac = true;
            this.useAdaptiveSync = false;
            this.cloudHeight = 128;
            this.toasts = true;
            this.instantSneak = false;
            this.preventShaders = false;
            this.useFastRandom = true;
        }
    }

    public static class NotificationSettings {
        public boolean hideRSORecommendation;

        public NotificationSettings() {
            this.hideRSORecommendation = false;
        }
    }
}
