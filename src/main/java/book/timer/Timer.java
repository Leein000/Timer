package book.timer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Timer extends JavaPlugin {

    @Override
    public void onEnable() {

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {

                if(bossbar != null) {

                    double progress = 0;
                    double timeLeft = addedTime + time * 100 - System.currentTimeMillis() / 10;
                    progress = timeLeft / time;

                    if(timeLeft < 0) {
                        bossbar.setVisible(false);
                        bossbar = null;
                        return;
                    }

                    for(Player p : Bukkit.getOnlinePlayers()) {

                        bossbar.addPlayer(p);
                    }

                    bossbar.setProgress(progress);
                    bossbar.setTitle(ChatColor.BOLD + "" + timeLeft + "초 남았습니다.");
                }
            }
        }, 0, 0);
    }

    @Override
    public void onDisable() {

    }

    int time = 0;
    BossBar bossbar = null;
    double addedTime = 0;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if(label.equalsIgnoreCase("timer")) {

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("remove")) {

                    if(bossbar == null) {
                        p.sendMessage("진행중인 타이머가 없습니다!");
                    } else {

                        bossbar.setVisible(false);
                        bossbar = null;
                        time = 0;
                        addedTime = 0;

                        p.sendMessage("타이머를 삭제했습니다!");
                    }
                } else {
                    p.sendMessage("잘못된 명령입니다!");
                }

            } else if(args.length == 2) {

                if(args[0].equalsIgnoreCase("add")) {

                    if (bossbar != null) {
                        p.sendMessage("이미 " + bossbar.getTitle() + "타이머가 진행 중입니다!");
                        return false;

                    } else {

                        bossbar = Bukkit.createBossBar("timer", BarColor.BLUE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);

                        time = Integer.parseInt(args[1]);
                        addedTime = System.currentTimeMillis() / 10;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            bossbar.addPlayer(player);
                        }
                        bossbar.setVisible(true);
                        p.sendMessage("타이머를 생성하였습니다!");
                    }
                } else if(args[0].equalsIgnoreCase("set")) {

                    if(bossbar == null) {
                        p.sendMessage("진행중인 타이머가 없습니다!");
                    } else {
                        time = Integer.parseInt(args[1]);
                        addedTime = System.currentTimeMillis() / 1000;

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            bossbar.addPlayer(player);
                        }

                        p.sendMessage("타이머를 " + time + "초로 설정했습니다!");
                    }
                }

            } else {
                p.sendMessage("잘못된 명령입니다!");
            }
        }
            return false;
    }
}
