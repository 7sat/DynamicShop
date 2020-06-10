package me.sat7.dynamicshop.commands;

import co.aikar.commands.*;
import me.sat7.dynamicshop.DynamicShop;
import me.sat7.dynamicshop.utilities.LangUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class HelpFormatter extends CommandHelpFormatter {
    private final PaperCommandManager manager;

    public HelpFormatter(PaperCommandManager manager) {
        super(manager);
        this.manager = manager;
    }

    @Override
    public void printHelpCommand(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
        String formatted = this.manager.formatMessage(issuer, MessageType.HELP, MessageKeys.HELP_FORMAT, this.getEntryFormatReplacements(help, entry));
        sendHelpMessage(issuer, entry, formatted);
    }

    @Override
    public void printDetailedHelpCommand(CommandHelp help, CommandIssuer issuer, HelpEntry entry) {
        String formatted = this.manager.formatMessage(issuer, MessageType.HELP, MessageKeys.HELP_DETAILED_COMMAND_FORMAT, this.getEntryFormatReplacements(help, entry));
        sendHelpMessage(issuer, entry, formatted);
    }

    private void sendHelpMessage(CommandIssuer issuer, HelpEntry entry, String formatted) {
        // Logs all the command names for use in LangUtil on help command
        // DynamicShop.plugin.getLogger().log(Level.WARNING, entry.getCommand().replace(" ", "_").toUpperCase());

        ArrayList<String> messages = new ArrayList<>();
        String command = entry.getCommand().replace(" ", "_").toUpperCase();
        Arrays.stream(NEWLINE.split(formatted)).forEach(string -> {
            messages.add(string);
            if (LangUtil.ccLang.get().isConfigurationSection("HELP." + command)) {
                LangUtil.ccLang.get().getConfigurationSection("HELP." + command).getKeys(false).forEach(key -> {
                    messages.add(
                            "  §d- §f" +
                                    LangUtil.ccLang.get().getString("HELP." + command + "." + key, "MISSING_STRING")
                                            .replace("{IRREVERSIBLE}", LangUtil.ccLang.get().getString("IRREVERSIBLE"))
                                            .replace("{HELP.PRICE}", LangUtil.ccLang.get().getString("HELP.PRICE"))
                                            .replace("{HELP.INF_STATIC}", LangUtil.ccLang.get().getString("HELP.INF_STATIC"))
                    );
                });
            }
        });
        for (String msg : messages) {
            issuer.sendMessageInternal(ACFUtil.rtrim(msg));
        }
    }

    private static final Pattern NEWLINE = Pattern.compile("\n");
}