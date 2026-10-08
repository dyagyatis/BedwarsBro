package com.dimchig.bedwarsbro.commands;

import com.dimchig.bedwarsbro.ChatSender;
import com.dimchig.bedwarsbro.Main;
import com.dimchig.bedwarsbro.gui.BedDefenseHighlighter;
import com.dimchig.bedwarsbro.gui.CustomHUDManager;
import com.dimchig.bedwarsbro.gui.TargetHUDManager;
import com.dimchig.bedwarsbro.stuff.AdvancedSoundAlerts;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandBWFork extends CommandBase {

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public String getCommandName() {
        return "bwfork";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/bwfork [cps|armor|stats|target|bed3d|soundalerts]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            String str = "&8<===============================================>\n";
            str += "              &cBedwars&fBro &aFork v3.1\n\n";
            str += "  &b/bwfork cps &7- Переключить отображение CPS\n";
            str += "  &b/bwfork armor &7- Переключить статус прочности брони\n";
            str += "  &b/bwfork stats &7- Переключить оверлей статистики сессии\n";
            str += "  &b/bwfork target &7- Переключить Target HUD цели\n";
            str += "  &b/bwfork bed3d &7- Переключить 3D подсветку защиты кровати\n";
            str += "  &b/bwfork soundalerts &7- Переключить звуковые оповещения\n";
            str += "  &b/bwstats &7- Показать текущую статистику сессии\n";
            str += "  &b/bwstats reset &7- Сбросить статистику сессии\n";
            str += "&8<===============================================>";
            ChatSender.addText(str);
            return;
        }

        String sub = args[0].toLowerCase();
        if (sub.equals("cps")) {
            CustomHUDManager.showCPS = !CustomHUDManager.showCPS;
            ChatSender.addText(Main.PREF + "Отображение CPS: " + (CustomHUDManager.showCPS ? "&aВключено" : "&cВыключено"));
        } else if (sub.equals("armor")) {
            CustomHUDManager.showArmorStatus = !CustomHUDManager.showArmorStatus;
            ChatSender.addText(Main.PREF + "Статус брони: " + (CustomHUDManager.showArmorStatus ? "&aВключен" : "&cВыключен"));
        } else if (sub.equals("stats")) {
            CustomHUDManager.showSessionStats = !CustomHUDManager.showSessionStats;
            ChatSender.addText(Main.PREF + "Оверлей статистики: " + (CustomHUDManager.showSessionStats ? "&aВключен" : "&cВыключен"));
        } else if (sub.equals("target")) {
            TargetHUDManager.enabled = !TargetHUDManager.enabled;
            ChatSender.addText(Main.PREF + "Target HUD цели: " + (TargetHUDManager.enabled ? "&aВключен" : "&cВыключен"));
        } else if (sub.equals("bed3d")) {
            BedDefenseHighlighter.enabled = !BedDefenseHighlighter.enabled;
            ChatSender.addText(Main.PREF + "3D Подсветка защиты кровати: " + (BedDefenseHighlighter.enabled ? "&aВключена" : "&cВыключена"));
        } else if (sub.equals("soundalerts")) {
            AdvancedSoundAlerts.enabled = !AdvancedSoundAlerts.enabled;
            ChatSender.addText(Main.PREF + "Звуковые оповещения: " + (AdvancedSoundAlerts.enabled ? "&aВключены" : "&cВыключены"));
        } else {
            ChatSender.addText(Main.PREF + "&cИспользование: /bwfork [cps|armor|stats|target|bed3d|soundalerts]");
        }
    }
}
