package de.cuzim1tigaaa.findmystation.command;

import de.cuzim1tigaaa.findmystation.FindMyStation;
import de.cuzim1tigaaa.findmystation.data.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorCommand implements SubCommand {

	private final FindMyStation plugin;

	public ColorCommand(FindMyStation plugin) {
		this.plugin = plugin;
	}

	@Override
	public String getCommand() {
		return "color";
	}

	@Override
	public List<String> getAliases() {
		return Collections.emptyList();
	}

	@Override
	public String getUsage() {
		return "color [#XXXXXX]";
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player player))
			return;

		if(!sender.hasPermission(Paths.PERMISSION_COMMAND_COLOR)) {
			sender.sendMessage(Config.getMessage(Paths.MESSAGE_NO_PERMISSION));
			return;
		}

		Data.PlayerData data = plugin.getData().getData(player.getUniqueId());

		if(args.length < 2) {
			player.sendMessage(Config.getMessage(Paths.MESSAGE_COMMAND_CURRENT_COLOR, "COLOR",
					ChatColor.of(data.getHexColor()) + "#" + String.format("%08X", java.awt.Color.decode(data.getHexColor()).getRGB()).substring(2)));
			return;
		}

		String newColor = args[1];

		if(newColor.startsWith("#")) {
			try {
				ChatColor.of(newColor);
				data.setHexColor(newColor);
				player.sendMessage(Config.getMessage(Paths.MESSAGE_COMMAND_NEW_COLOR, "COLOR",
						ChatColor.of(data.getHexColor()) + "#" + String.format("%08X", java.awt.Color.decode(data.getHexColor()).getRGB()).substring(2)));
			}catch(IllegalArgumentException ignored) {
				player.sendMessage(Config.getMessage(Paths.MESSAGE_COMMAND_INVALID_COLOR, "COLOR", newColor));
			}
			return;
		}

		if(newColor.startsWith("&") && newColor.length() == 2) {
			Matcher matcher = cc.matcher(newColor);
			if(!matcher.find()) {
				player.sendMessage(Config.getMessage(Paths.MESSAGE_COMMAND_INVALID_COLOR, "COLOR", newColor));
				return;
			}
			ChatColor color = ChatColor.getByChar(newColor.charAt(1));
			data.setHexColor("#" + String.format("%08X", color.getColor().getRGB()).substring(2));
			player.sendMessage(Config.getMessage(Paths.MESSAGE_COMMAND_NEW_COLOR, "COLOR",
					ChatColor.of(data.getHexColor()) + "#" + String.format("%08X", java.awt.Color.decode(data.getHexColor()).getRGB()).substring(2)));
			return;
		}
		player.sendMessage(Config.getMessage(Paths.MESSAGE_COMMAND_INVALID_COLOR, "COLOR", newColor));
	}

	private final Pattern cc = Pattern.compile("&[0-9a-fA-F]");

	@Override
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return Collections.emptyList();
	}
}