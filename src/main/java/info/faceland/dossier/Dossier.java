package info.faceland.dossier;

import info.faceland.api.FacePlugin;
import info.faceland.facecore.shade.nun.ivory.config.VersionedIvoryConfiguration;
import info.faceland.facecore.shade.nun.ivory.config.VersionedIvoryYamlConfiguration;
import info.faceland.facecore.shade.nun.ivory.config.settings.IvorySettings;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class Dossier extends FacePlugin {

    private static Dossier instance;
    private VersionedIvoryYamlConfiguration configYAML;
    private IvorySettings settings;
    private MySQLConnectionPool pool;

    public Dossier() {
        instance = this;
    }

    public static Dossier getInstance() {
        return instance;
    }

    @Override
    public void preEnable() {
        configYAML = new VersionedIvoryYamlConfiguration(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                                                         VersionedIvoryConfiguration.VersionUpdateType.BACKUP_AND_UPDATE);
        if (configYAML.update()) {
            getLogger().info("Updating config.yml");
        }

        settings = IvorySettings.loadFromFiles(configYAML);

        pool = new MySQLConnectionPool(settings.getString("host", "localhost"), settings.getString("port", "3306"),
                                       settings.getString("database", "localdata"), settings.getString("username", "localuser"),
                                       settings.getString("password", "localpass"));
    }

    @Override
    public void enable() {
        pool.connect();
    }

    @Override
    public void postEnable() {

    }

    @Override
    public void preDisable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void postDisable() {
        pool = null;
        settings = null;
        configYAML = null;
    }

    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

}
