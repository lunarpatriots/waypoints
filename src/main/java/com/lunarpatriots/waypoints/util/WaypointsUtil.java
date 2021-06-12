package com.lunarpatriots.waypoints.util;

import com.google.gson.Gson;
import com.lunarpatriots.waypoints.MainApp;
import com.lunarpatriots.waypoints.exceptions.DataFileException;
import com.lunarpatriots.waypoints.model.Waypoint;
import com.lunarpatriots.waypoints.model.WaypointsList;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: lunarpatriots@gmail.com
 * Date created: 06/08/2021
 */
public class WaypointsUtil {

    public static List<Waypoint> data = new ArrayList<>();

    private WaypointsUtil() {
    }

    public static void loadFromFile(final MainApp plugin) throws DataFileException {
        final File dataFile = getDataFile(plugin);
        final Gson gson = new Gson();

        try (final Reader fileReader = new FileReader(dataFile)) {
            final WaypointsList list = gson.fromJson(fileReader, WaypointsList.class);
            data = list.getData();
        } catch(final IOException ex) {
            LogUtil.error(ex.getMessage());
            throw new DataFileException("Failed to read data file!", ex);
        }
    }

    public static void saveToFile(final MainApp plugin) throws DataFileException {
        final File dataFile = getDataFile(plugin);
        final Gson gson = new Gson();

        try(final Writer writer = new FileWriter(dataFile)) {
            final WaypointsList list = new WaypointsList();
            list.setData(data);
            gson.toJson(list, writer);
        } catch (final IOException ex) {
            LogUtil.error(ex.getMessage());
            throw new DataFileException("Failed to write to data file!");
        }
    }

    private static File getDataFile(final MainApp plugin) throws DataFileException {
        final File baseDirectory = plugin.getDataFolder();

        if (!baseDirectory.exists()) {
            throw new DataFileException("Base directory not found!");
        }

        final File dataDirectory = new File(baseDirectory, "data");

        if (!dataDirectory.exists()) {
            throw new DataFileException("Data directory not found!");
        }

        final File dataFile = new File(dataDirectory, "waypoints.json");

        if (!dataFile.exists()) {
            throw new DataFileException("Data file not found!");
        }

        return dataFile;
    }
}
