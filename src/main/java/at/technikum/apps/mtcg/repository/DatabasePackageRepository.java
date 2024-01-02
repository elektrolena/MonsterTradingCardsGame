package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Package;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DatabasePackageRepository {
    private final String SAVE_PACKAGE_SQL = "INSERT INTO packages(id, price) VALUES (?, ?)";
    private final String GET_PACKAGE_SQL = "SELECT * FROM packages LIMIT 1";
    private final String DELETE_PACKAGE_SQL = "DELETE FROM packages WHERE id = ?";

    private final Database database = new Database();

    public void savePackage(Package cardPackage) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_PACKAGE_SQL)
        ) {
            pstmt.setString(1, cardPackage.getId());
            pstmt.setInt(2, cardPackage.getPrice());

            pstmt.execute();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
    public Optional<Package> getPackage() {
        Optional<Package> cardPackage = Optional.empty();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(GET_PACKAGE_SQL)
        ) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Package foundPackage = mapResultSetToPackage(rs);
                    cardPackage = Optional.of(foundPackage);
                }
            }
        } catch (SQLException e) {

        }
        return cardPackage;
    }

    public void deletePackage(String packageId) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(DELETE_PACKAGE_SQL)
        ) {
            pstmt.setString(1, packageId);

            pstmt.execute();
        } catch (SQLException e) {

        }
    }

    private Package mapResultSetToPackage(ResultSet rs) throws SQLException {
        Package cardPackage = new Package();
        cardPackage.setId(rs.getString("id"));
        cardPackage.setPrice(rs.getInt("price"));
        return cardPackage;
    }
}
