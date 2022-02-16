package connexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;

public final class ConnGen {

    private String user;
    private String pwd;
    private String url;
    private String driver;
    private Connection conn;

    public ConnGen() throws Exception {
        try (InputStream input = ConnGen.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();

            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            prop.load(input);
            setUser(prop.getProperty("db.user"));
            setPwd(prop.getProperty("db.password"));
            setUrl("jdbc:postgresql:" + prop.getProperty("db.database"));
        }

        
        setDriver("org.postgresql.Driver");
        setConn();
    }

    public ConnGen(String user, String pwd) throws Exception {
        setUser(user);
        setPwd(pwd);
        setUrl("jdbc:oracle:thin:@localhost:1521:XE");
        setDriver("oracle.jdbc.driver.OracleDriver");
        setConn();
    }

    public ConnGen(String url, String driver, String user, String pwd) throws Exception {
        setUser(user);
        setPwd(pwd);
        setUrl(url);
        setDriver(driver);
        setConn();
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection val) {
        conn = val;

    }

    public void setConn() throws Exception {
        Class.forName(getDriver());
        Connection newConn = DriverManager.getConnection(getUrl(), getUser(), getPwd());
        newConn.setAutoCommit(false);
        setConn(newConn);
    }

    public void setDriver(String val) {
        driver = val;
    }

    public void setUrl(String val) {
        url = val;
    }

    public void setUser(String val) {
        user = val;
    }

    public void setPwd(String val) {
        pwd = val;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    public ResultSet getResultSet(String req) throws Exception {
        if (getConn() == null) {
            throw new Exception("Erreur: Connexion non etablie");
        }
        Statement state = getConn().createStatement();
        ResultSet val = state.executeQuery(req);

        return val;
    }

    public int insertResult(String req) throws Exception {
        if (getConn() == null) {
            throw new Exception("Erreur: connexion non etablie");
        }
        Statement state = getConn().createStatement();

        int val = state.executeUpdate(req);

        state.close();
        return val;
    }

    public int[] multipleInsert(String[] req) throws Exception {
        if (getConn() == null) {
            throw new Exception("Erreur: connexion non etablie");
        }
        Statement state = getConn().createStatement();
        for (int iR = 0; iR < req.length; iR++) {
            state.addBatch(req[iR]);
        }
        int[] result = state.executeBatch();
        state.close();
        return result;
    }

    public int[] multipleInsert(String req) throws Exception {
        String[] allReq = req.split(";");
        return multipleInsert(allReq);
    }

    public void close() throws Exception {
        getConn().close();
    }
}
