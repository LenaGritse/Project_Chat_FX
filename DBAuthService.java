package server;

import java.sql.*;

public class DBAuthService implements AuthService {
    private static Connection connection;
    private static Statement stmt;

    public DBAuthService() throws ClassNotFoundException, SQLException {
        connect();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) throws SQLException {

        PreparedStatement psSelect = connection.prepareStatement
                ("SELECT nick FROM users WHERE  login = ? AND password = ?;");
        psSelect.setString(1, login);
        psSelect.setString(2, password);
        ResultSet rs = psSelect.executeQuery();
        if (rs.next()){
            return rs.getString("nick");
        } else {
            return null;
        }
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return false;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
        stmt = connection.createStatement();
    }

    public void disconnect(){
        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}