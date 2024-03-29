package communication.rest;

import communication.rest.api.RestLoginService;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class RestLoginServiceImpl implements RestLoginService {

    @Override
    public boolean checkCredentials(String emailAddress, String password) {
        boolean matchingPassword = false;

        Properties env = new Properties();
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.0.40.162:389");

        String userType = getUserType(emailAddress, env);

        if (userType.isBlank())
            return false;

        try {
            InitialDirContext ctx = new InitialDirContext(env);

            String filter = "(&(objectClass=inetOrgPerson)(uid=" + emailAddress + "))";

            String[] attrIDs = {"userpassword"};

            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String base = "ou=" + userType + ",dc=openmicroscopy,dc=org";
            NamingEnumeration<SearchResult> resultList = ctx.search(base, filter, searchControls);

            while (resultList.hasMore()) {
                SearchResult result = resultList.next();
                String ldapPass = new String((byte[]) result.getAttributes().get("userPassword").get());
                byte[] ldapDecoded = Base64.getDecoder().decode(ldapPass.split("}")[1]);
                String ldapHex = String.format("%040x", new BigInteger(1, ldapDecoded));

                if (ldapHex.equals(encryptSHA512(password))) {
                    matchingPassword = true;
                }
            }

            ctx.close();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }

        return matchingPassword;
    }

    @Override
    public List<Role> getRole(String emailAddress) {
        List<Role> roles = new LinkedList<>();

        Properties env = new Properties();
        env.put(Context.SECURITY_AUTHENTICATION, "none");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://10.0.40.162:389");

        String userType = getUserType(emailAddress, env);

        if (userType.isBlank())
            return Collections.emptyList();

        try {
            InitialDirContext ctx = new InitialDirContext(env);

            String base = "ou=roles,dc=openmicroscopy,dc=org";
            String filter = "(&(objectClass=organizationalrole)(roleoccupant=uid=" + emailAddress + ",ou=" + userType + ",dc=openmicroscopy,dc=org))";

            String[] attrIDs = {"*"};

            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> resultList = ctx.search(base, filter, searchControls);

            while (resultList.hasMore()) {
                SearchResult result = resultList.next();
                String ldapRole = (String) result.getAttributes().get("cn").get();
                roles.add(Role.valueOf(ldapRole.toUpperCase()));
            }

            ctx.close();
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        return roles;
    }

    private String encryptSHA512(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashText = new StringBuilder(no.toString(16));

            // Add preceding 0s to make it 32 bit
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }

            // return the HashText
            return hashText.toString();
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUserType(String emailAddress, Properties env) {

        try {
            InitialDirContext ctx = new InitialDirContext(env);

            String base = "dc=openmicroscopy,dc=org";
            String filter = "(&(objectClass=inetOrgPerson)(uid=" + emailAddress + "))";

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> resultList = ctx.search(base, filter, searchControls);

            if (resultList.hasMore()) {
                SearchResult result = resultList.next();
                String name = result.getName();
                ctx.close();

                if (name.matches("(.*)ou=customer(.*)"))
                    return "customer";
                if (name.matches("(.*)ou=licensee(.*)"))
                    return "licensee";
            }
        } catch (NamingException ex) {
            ex.printStackTrace();
        }

        return "";
    }
}
