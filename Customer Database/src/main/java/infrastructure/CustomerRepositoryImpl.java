package infrastructure;

import sharedrmi.application.dto.CustomerDTO;
import sharedrmi.domain.valueobjects.Address;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public List<CustomerDTO> findCustomersByName(String name) {

        List<CustomerDTO> customerDTOs = new LinkedList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            name = "%"+name+"%";
            @SuppressWarnings("SqlDialectInspection")
            String fetchquery = "select * from customer where lower(\"givenName\") like lower('" + name + "') OR lower(\"familyName\") like lower('" + name + "')";

            ResultSet rs = st.executeQuery(fetchquery);

            while (rs.next()) {
                customerDTOs.add(new CustomerDTO(rs.getString("givenName"),
                                                 rs.getString("familyName"),
                                                 rs.getString("email"),
                                                 Address.builder()
                                                         .addressCountry(rs.getString("address.addressCountry"))
                                                         .addressLocality(rs.getString("address.addressLocality"))
                                                         .postalCode(rs.getString("address.postalCode"))
                                                         .streetAddress(rs.getString("address.streetAddress"))
                                                         .build()
                        ));
            }

            st.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return customerDTOs;
    }

    @Override
    public List<CustomerDTO> findCustomersByExactName(String givenName, String familyName) {

        List<CustomerDTO> customerDTOs = new LinkedList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            givenName = "%"+givenName+"%";
            familyName = "%"+familyName+"%";
            @SuppressWarnings("SqlDialectInspection")
            String fetchquery = "select * from customer where lower(\"givenName\") like lower('" + givenName + "') AND lower(\"familyName\") like lower('" + familyName + "')";

            ResultSet rs = st.executeQuery(fetchquery);

            while (rs.next()) {
                customerDTOs.add(new CustomerDTO(rs.getString("givenName"),
                        rs.getString("familyName"),
                        rs.getString("email"),
                        Address.builder()
                                .addressCountry(rs.getString("address.addressCountry"))
                                .addressLocality(rs.getString("address.addressLocality"))
                                .postalCode(rs.getString("address.postalCode"))
                                .streetAddress(rs.getString("address.streetAddress"))
                                .build()
                ));
            }

            st.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return customerDTOs;
    }

    @Override
    public List<CustomerDTO> findCustomersByGivenName(String givenName) {

        List<CustomerDTO> customerDTOs = new LinkedList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            givenName = "%"+givenName+"%";
            @SuppressWarnings("SqlDialectInspection")
            String fetchquery = "select * from customer where lower(\"givenName\") like lower('" + givenName + "')";

            ResultSet rs = st.executeQuery(fetchquery);

            while (rs.next()) {
                customerDTOs.add(new CustomerDTO(rs.getString("givenName"),
                        rs.getString("familyName"),
                        rs.getString("email"),
                        Address.builder()
                                .addressCountry(rs.getString("address.addressCountry"))
                                .addressLocality(rs.getString("address.addressLocality"))
                                .postalCode(rs.getString("address.postalCode"))
                                .streetAddress(rs.getString("address.streetAddress"))
                                .build()
                ));
            }

            st.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return customerDTOs;
    }

    @Override
    public List<CustomerDTO> findCustomersByFamilyName(String familyName) {

        List<CustomerDTO> customerDTOs = new LinkedList<>();

        try {
            Class.forName("org.postgresql.Driver");

            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "dbadmin!2020";

            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();

            familyName = "%"+familyName+"%";
            @SuppressWarnings("SqlDialectInspection")
            String fetchquery = "select * from customer where lower(\"familyName\") like lower('" + familyName + "')";

            ResultSet rs = st.executeQuery(fetchquery);

            while (rs.next()) {
                customerDTOs.add(new CustomerDTO(rs.getString("givenName"),
                        rs.getString("familyName"),
                        rs.getString("email"),
                        Address.builder()
                                .addressCountry(rs.getString("address.addressCountry"))
                                .addressLocality(rs.getString("address.addressLocality"))
                                .postalCode(rs.getString("address.postalCode"))
                                .streetAddress(rs.getString("address.streetAddress"))
                                .build()
                ));
            }

            st.close();
            con.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return customerDTOs;
    }

}
