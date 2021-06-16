package com.lunarpatriots.waypoints.commands;

import com.lunarpatriots.waypoints.api.exceptions.DatabaseException;
import com.lunarpatriots.waypoints.api.model.Waypoint;
import com.lunarpatriots.waypoints.api.repository.WaypointRepository;
import com.lunarpatriots.waypoints.enums.Region;
import com.lunarpatriots.waypoints.util.LogUtil;
import com.lunarpatriots.waypoints.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By: theLANister Date created: 06/13/2021 Date Modified: 6/14/2021
 */

public final class WaypointsCommand implements TabExecutor {

    private final WaypointRepository repository;

    public WaypointsCommand(final WaypointRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean onCommand(final CommandSender commandSender,
                             final Command command,
                             final String string,
                             final String[] strings) {
        final Player player = (Player) commandSender;

        try {
            final String keyword = strings[0];
            final World world = player.getWorld();
            final Region chosenRegion = Region.getRegion(keyword);
            final Region currentRegion = Region.getRegion(world.getEnvironment());
            final String currentWorldName = world.getName();

            final String queryWorld;
            if (currentRegion == chosenRegion) {
                queryWorld = currentWorldName;
            } else {
                queryWorld = StringUtils.isNotBlank(currentRegion.getSuffix())
                    ? currentWorldName.replace(currentRegion.getSuffix(), chosenRegion.getSuffix())
                    : currentWorldName.concat(chosenRegion.getSuffix());
            }

            listWaypoints(queryWorld, player, chosenRegion);
        } catch (final Exception ex) {
            LogUtil.error(ex.getMessage());
        }

        return true;
    }

    private void listWaypoints(final String queryWorld,
                               final Player player,
                               final Region chosenRegion) throws DatabaseException {
        final List<Waypoint> waypoints = repository.getWaypoints(queryWorld);

        if (waypoints.size() > 0) {
            final String waypointsString = waypoints.stream()
                .map(Waypoint::getName)
                .collect(Collectors.joining("\n -"));

            final String msg = String.format(
                "%s %sWaypoints:\n -%s",
                chosenRegion.getDisplayValue(),
                ChatColor.GREEN,
                waypointsString);
            MessageUtil.success(player, msg);
        } else {
            MessageUtil.fail(player,
                "There are no waypoints in the " + chosenRegion.getKeyword() + " region!");
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender commandSender,
                                      final Command command,
                                      final String string,
                                      final String[] strings) {

        return null;
    }
}