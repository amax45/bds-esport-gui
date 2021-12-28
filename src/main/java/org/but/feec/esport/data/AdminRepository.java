package org.but.feec.esport.data;

import org.but.feec.esport.api.*;
import org.but.feec.esport.config.DataSourceConfig;
import org.but.feec.esport.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {
    public AdminAuthView findAdminByEmail(String email) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, password" +
                             " FROM admin.admin a" +
                             " WHERE a.email = ?")
        ) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToAdminAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find admin by ID with addresses failed.", e);
        }
        return null;
    }


    public AdminDetailView findAdminDetailedView(Long adminId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT admin_id, nickname, given_name, family_name, email" +
                             " FROM admin.admin " +
                      "WHERE admin.admin_id = ?"
             )
        ) {
            preparedStatement.setLong(1, adminId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToAdminDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find admin by ID with addresses failed.", e);
        }
        return null;
    }

    public List<AdminBasicView> getAdminsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT a.admin_id, a.given_name, a.nickname, a.family_name, a.email" +
                             " FROM admin.admin a ");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<AdminBasicView> adminBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                PreparedStatement preparedStatement2 = connection.prepareStatement(
                        "SELECT m.time_of_match" +
                                " FROM admin.match m WHERE admin_id=? ");
                AdminBasicView x = mapToAdminBasicView(resultSet);
                preparedStatement2.setLong(1,x.getId());
                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()) {
                    x.addTime_of_match(resultSet2.getTimestamp("time_of_match"));
                }

                adminBasicViews.add(x);
            }
            return adminBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Admins basic view could not be loaded.", e);
        }

    }

    public void createAdmin(AdminCreateView adminCreateView) {
        String insertAdminSQL = "INSERT INTO admin.admin (email, given_name, nickname, password, family_name) VALUES (?,?,?,?,?)";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertAdminSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setString(1, adminCreateView.getEmail());
            preparedStatement.setString(2, adminCreateView.getGivenName());
            preparedStatement.setString(3, adminCreateView.getNickname());
            preparedStatement.setString(4, String.valueOf(adminCreateView.getPassword()));
            preparedStatement.setString(5, adminCreateView.getFamilyName());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows==0) {
                throw new DataAccessException("Creating admin failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating admin failed operation on the database failed.");
        }
    }

    public void editAdmin(AdminEditView adminEditView) {
        String insertPersonSQL = "UPDATE admin.admin a SET email = ?, given_name = ?, nickname = ?, family_name = ? WHERE a.admin_id = ?";
        String checkIfExists = "SELECT email FROM admin.admin a WHERE a.admin_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setString(1, adminEditView.getEmail());
            preparedStatement.setString(2, adminEditView.getGivenName());
            preparedStatement.setString(3, adminEditView.getNickname());
            preparedStatement.setString(4, adminEditView.getFamilyName());
            preparedStatement.setLong(5, adminEditView.getId());

            try {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, adminEditView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This admin for edit do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows==0) {
                    throw new DataAccessException("Creating person failed, no rows affected.");
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating person failed operation on the database failed.");
        }
    }

    public void deleteAdmin(AdminBasicView adminBasicView) throws SQLException {
        String delete = "DELETE FROM admin.admin WHERE admin_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(delete, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, adminBasicView.getId());
            preparedStatement.execute();
        }
        catch (SQLException e) {
            throw new DataAccessException("Deleting admin failed.");
        }
    }

    private AdminAuthView mapToAdminAuth(ResultSet rs) throws SQLException {
        AdminAuthView admin = new AdminAuthView();
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        return admin;
    }

    private AdminBasicView mapToAdminBasicView(ResultSet rs) throws SQLException {
        AdminBasicView adminBasicView = new AdminBasicView();
        adminBasicView.setAdminId(rs.getLong("admin_id"));
        adminBasicView.setEmail(rs.getString("email"));
        adminBasicView.setGivenName(rs.getString("given_name"));
        adminBasicView.setFamilyName(rs.getString("family_name"));
        adminBasicView.setNickname(rs.getString("nickname"));
        return adminBasicView;
    }

    private AdminDetailView mapToAdminDetailView(ResultSet rs) throws SQLException {
        AdminDetailView adminDetailView = new AdminDetailView();
        adminDetailView.setId(rs.getLong("admin_id"));
        adminDetailView.setEmail(rs.getString("email"));
        adminDetailView.setGivenName(rs.getString("given_name"));
        adminDetailView.setFamilyName(rs.getString("family_name"));
        adminDetailView.setNickname(rs.getString("nickname"));
        return adminDetailView;
    }
}
