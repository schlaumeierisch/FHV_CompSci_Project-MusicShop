package infrastructure;

import sharedrmi.application.dto.CustomerDTO;

import java.util.List;

public interface CustomerRepository {
    List<CustomerDTO> findCustomersByName(String name);
    List<CustomerDTO> findCustomersByExactName(String givenName, String familyName);
    List<CustomerDTO> findCustomersByGivenName(String givenName);
    List<CustomerDTO> findCustomersByFamilyName(String familyName);
}
