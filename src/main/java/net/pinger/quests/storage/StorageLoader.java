package net.pinger.quests.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;

public class StorageLoader {

    /**
     * This method initializes the database by reading the resource file.
     *
     * @param database the database
     * @param stream the resource file stream
     */

    public static void init(Storage database, InputStream stream) {
        final String contents;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            contents = reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to read contents from storage.sql", e);
            return;
        }

        // Each query ends with a ";" character
        // So we can split the queries by that character
        // And execute the queries individually
        final String[] queries = contents.split(";");
        for (final String query : queries) {
            try (final Connection conn = database.getConnection()) {
                try (final PreparedStatement stat = conn.prepareStatement(query)) {
                    stat.execute();
                }
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to execute a query", e);
            }
        }
    }

}
