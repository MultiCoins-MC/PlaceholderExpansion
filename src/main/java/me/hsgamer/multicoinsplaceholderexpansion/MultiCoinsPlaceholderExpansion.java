package me.hsgamer.multicoinsplaceholderexpansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.hsgamer.multicoins.MultiCoins;
import me.hsgamer.multicoins.object.CoinHolder;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Optional;

public final class MultiCoinsPlaceholderExpansion extends PlaceholderExpansion {
    private final String version = getClass().getPackage().getImplementationVersion();

    @Override
    public @NotNull String getIdentifier() {
        return "multicoins";
    }

    @Override
    public @NotNull String getAuthor() {
        return "HSGamer";
    }

    @Override
    public @NotNull String getVersion() {
        return version;
    }

    @Override
    public @NotNull String getRequiredPlugin() {
        return "MultiCoins";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        MultiCoins instance = JavaPlugin.getPlugin(MultiCoins.class);
        String[] split = params.split(";");
        String name = split[0].trim();
        String type = split.length > 1 ? split[1].toLowerCase(Locale.ROOT).trim() : "";
        Optional<CoinHolder> optionalCoinHolder = instance.getCoinManager().getHolder(name);
        if (!optionalCoinHolder.isPresent()) return null;
        CoinHolder coinHolder = optionalCoinHolder.get();
        switch (type) {
            case "currency":
                double amount;
                if (split.length > 2) {
                    try {
                        amount = Double.parseDouble(split[2].trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                } else {
                    amount = coinHolder.getBalance(player.getUniqueId());
                }
                return coinHolder.getCoinFormatter().getCurrency(amount);
            case "value_raw":
                return String.valueOf(coinHolder.getBalance(player.getUniqueId()));
            case "value":
            default:
                return coinHolder.getCoinFormatter().format(coinHolder.getBalance(player.getUniqueId()));
        }
    }
}
