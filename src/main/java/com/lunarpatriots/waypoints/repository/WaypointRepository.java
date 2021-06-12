package com.lunarpatriots.waypoints.repository;

import com.lunarpatriots.waypoints.MainApp;
import com.lunarpatriots.waypoints.model.Waypoint;
import com.lunarpatriots.waypoints.util.LogUtil;
import com.lunarpatriots.waypoints.util.WaypointsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By: lunarpatriots@gmail.com
 * Date created: 06/08/2021
 */
public class WaypointRepository {

    public List<Waypoint> getWaypoints(final String worldName) {
        final List<Waypoint> waypoints = WaypointsUtil.data;

        return waypoints.stream()
            .filter(waypoint -> worldName.equals(waypoint.getWorld()))
            .collect(Collectors.toList());
    }

    public void saveWaypoint(final Waypoint waypoint) {
        WaypointsUtil.data.add(waypoint);
    }

    public void updateWaypoint(final Waypoint waypoint) {
        final List<Waypoint> waypoints = WaypointsUtil.data;

        waypoints.stream()
            .filter(current -> waypoint.getUuid().equals(current.getUuid()))
            .findFirst()
            .ifPresent(current -> {
                final int index = waypoints.indexOf(current);
                waypoints.set(index, waypoint);
            });
    }

    public void deleteWaypoint(final Waypoint waypoint) {
        final List<Waypoint> waypoints = WaypointsUtil.data;
        waypoints.remove(waypoint);
    }
}
