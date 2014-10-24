package info.faceland.dossier;

import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionPool {

    private final String username;
    private final String password;
    private final String connectURI;
    private DataSource dataSource;

    public MySQLConnectionPool(String host, String port, String database, String username, String password) {
        this.username = username;
        this.password = password;
        this.connectURI = "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public void connect() {
        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, username, password);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
        ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        poolableConnectionFactory.setPool(connectionPool);
        dataSource = new PoolingDataSource<>(connectionPool);
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("must have connected first");
        }
        return dataSource.getConnection();
    }

}
