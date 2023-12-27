package impl;

import java.security.InvalidParameterException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interfaces.ILoyaltyCardOwner;

/**
 * This class represents loyalty card owners.
 *
 */
public class LoyaltyCardOwner implements ILoyaltyCardOwner {

    private String email;
    private String name;

    public LoyaltyCardOwner(String email, String name) throws InvalidParameterException {
        // Checks if email and/or name are null or blank
        if (email == null || email.isBlank())
            throw new InvalidParameterException("Email: " + email);
        if (name == null || name.isBlank())
            throw new InvalidParameterException("Name: " + name);

        // Checks if the email matches email regex

        // -----------------

        // REGEX Pattern taken from the W3C HTML SPEC
        // (http://www.w3.org/TR/html5/forms.html#valid-e-mail-address)

        Pattern pattern = Pattern.compile(
                "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
       
        // -----------------
        
        Matcher matcher = pattern.matcher(email);

        if (!matcher.find())
            throw new InvalidParameterException("Email: " + email);

        this.email = email;
        this.name = name;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
