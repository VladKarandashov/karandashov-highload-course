package com.karandashov.monolith.repository.impl;

import com.karandashov.monolith.entity.UserEntity;
import com.karandashov.monolith.exception.BaseException;
import com.karandashov.monolith.exception.Error;
import com.karandashov.monolith.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

    private static final String CREATE_SQL = """
            INSERT INTO users
            ("id", "passwordHash", "firstName", "lastName", "birthDate", "biography", "city") VALUES
            (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT "id", "passwordHash", "firstName", "lastName", "birthDate", "biography", "city"
            FROM users WHERE id = ?
            """;

    private static final String SEARCH_SQL = """
            SELECT "id", "passwordHash", "firstName", "lastName", "birthDate", "biography", "city"
            FROM users
            WHERE "firstName" LIKE ? AND "lastName" LIKE ?
            ORDER BY id
            """;

    private static final String DELETE_ALL_SQL = "DELETE FROM users";

    private final DataSource dataSource;

    @Override
    public void save(UserEntity entity) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(CREATE_SQL)) {
            ps.setObject(1, entity.getId());
            ps.setString(2, entity.getPasswordHash());
            ps.setString(3, entity.getFirstName());
            ps.setString(4, entity.getLastName());
            ps.setObject(5, entity.getBirthDate().toOffsetDateTime());
            ps.setString(6, entity.getBiography());
            ps.setString(7, entity.getCity());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BaseException(Error.SQL_FAILED_SAVE_USER, e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID userId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setObject(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapSingleUser(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new BaseException(Error.SQL_FAILED_GET_USER_BY_ID, e);
        }
    }

    @Override
    public List<UserEntity> search(String firstName, String lastName) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, firstName + "%");
            ps.setString(2, lastName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<UserEntity> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapSingleUser(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new BaseException(Error.SQL_FAILED_GET_USER_BY_ID, e);
        }
    }

    @Override
    public void deleteAll() {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement(DELETE_ALL_SQL)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new BaseException(Error.SQL_FAILED_DELETE_USER, e);
        }
    }

    private UserEntity mapSingleUser(ResultSet rs) throws SQLException {
        UUID id = (UUID) rs.getObject("id");
        String passwordHash = rs.getString("passwordHash");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        OffsetDateTime birthDate = rs.getObject("birthDate", OffsetDateTime.class);
        String biography = rs.getString("biography");
        String city = rs.getString("city");
        return new UserEntity(id, passwordHash, firstName, lastName, birthDate.toZonedDateTime(), biography, city);
    }
}
