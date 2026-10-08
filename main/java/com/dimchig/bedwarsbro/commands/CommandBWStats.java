package com.dimchig.bedwarsbro.commands;

import com.dimchig.bedwarsbro.ChatSender;
import com.dimchig.bedwarsbro.Main;
import com.dimchig.bedwarsbro.stuff.SessionStatsTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandBWStats extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "bwstats";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bwstats [reset]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 0 && args[0].equalsIgnoreCase("reset")) {
            SessionStatsTracker.reset();
            ChatSender.addText(Main.PREF + "&aСтатистика текущей сессии Bedwars сброшена!");
            return;
        }

        ChatSender.addText("&8<===============================================>");
        ChatSender.addText("&cBedwarsBro Fork &7- Статистика текущей сессии:");
        ChatSender.addText(SessionStatsTracker.getFormattedStats());
        ChatSender.addText("&8<===============================================>");
    }
}
