package us.mytheria.placeholders;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.google.common.base.Charsets;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.dablakbandit.bank.api.BankAPI;
import net.milkbowl.vault.economy.Economy;

public class Money extends PlaceholderExpansion {
	  
	  public boolean persist() {
	    return true;
	  }
		Economy econ;
		@Override
		public boolean canRegister() {
			return Bukkit.getPluginManager().getPlugin("Vault") != null;
		}
		@Override
		public boolean register() {
			if (!canRegister()) return false;
			RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp == null) return false;
			econ = rsp.getProvider();
			return PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
		}
	  
	  public String getAuthor() {
	    return "promorrom";
	  }
	  
	  public String getIdentifier() {
	    return "mmoney";
	  }
		@Override
		public String getPlugin() {
			return "Vault";
		}
	  public String getVersion() {
	    return "1.0";
	  }
	  
	  public static String chat(String s) {
	    if (s == null)
	      return ChatColor.translateAlternateColorCodes('&', "&cConfig Missing Text"); 
	    byte[] temp = s.getBytes(Charsets.UTF_8);
	    return ChatColor.translateAlternateColorCodes('&', new String(temp, Charsets.UTF_8));
	  }
	  
	  public String onPlaceholderRequest(Player p, String identifier) {
			if (p == null && econ == null) return "$0";
			if (identifier.contains("balance")) {
				boolean million = false;
				double money = ((double)((int) (((double)econ.getBalance(p))/0.01)))/100D;
				if (identifier.equals("balancehalf")) money = ((double)((int) (((double)money)/0.02)))/100D;
				if (money >= 1000000) {
					million = true;
					money = ((double) ((int) (money/10000)))/100D;
				}
				String result = "$";
				String[] i = String.valueOf(money).replace(".", "/").split("/")[0].split("");
				for (int in = 0; in < i.length; in++) {
					result += i[in];
					int pos = i.length - 1 - in;
					if (pos != 0 && pos % 3 == 0) result += ",";
				}
				result += "." + String.valueOf(money).replace(".", "/").split("/")[1];
				if (String.valueOf(money).replace(".", "/").split("/")[1].toCharArray().length == 1) result += "0";
				if (million) {
					if (money == 1) result += " Millon";
					else result += " Millones";
				}
				return result;
			}
			return null;
		}
	}