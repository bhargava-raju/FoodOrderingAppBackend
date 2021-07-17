package com.upgrad.FoodOrderingApp.service.businness;



import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.regex.Pattern;



@Service
public class CustomerService {


    @Autowired private CustomerAuthDao cusAuthDao;

    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = cusAuthDao.getCustomerAuthByToken(accessToken);

    @Autowired
    private CustomerAuthDao customerAuthDao;

    @Autowired
    private CustomerDao customerDao;

    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDao.getCustomerAuthByToken(accessToken);

        if (customerAuthEntity != null) {

            if (customerAuthEntity.getLogoutAt() != null) {
                throw new AuthorizationFailedException(
                        "ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }

            if (ZonedDateTime.now().isAfter(customerAuthEntity.getExpiresAt())) {
                throw new AuthorizationFailedException(
                        "ATHR-003", "Your session is expired. Log in again to access this endpoint.");
            }
            return customerAuthEntity.getCustomer();
        } else {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
    }


//    ----------------------

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String username, String password)
            throws AuthenticationFailedException {

        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(username);
        System.out.println(customerEntity);
        if (customerEntity == null) {
            throw new AuthenticationFailedException(
                    "ATH-001", "This contact number has not been registered!");
        }
        final String encryptedPassword =
                PasswordCryptographyProvider.encrypt(password, customerEntity.getSalt());

        System.out.println(encryptedPassword);
        System.out.println("customer password" + customerEntity.getPassword());

//        if (!encryptedPassword.equals(customerEntity.getPassword())) {
//            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
//        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
        customerAuthEntity.setUuid(UUID.randomUUID().toString());
        customerAuthEntity.setCustomer(customerEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        customerAuthEntity.setLoginAt(now);
        customerAuthEntity.setExpiresAt(expiresAt);
        String accessToken = jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt);
        customerAuthEntity.setAccessToken(accessToken);
        customerAuthDao.createCustomerAuthToken(customerAuthEntity);
        return customerAuthEntity;
    }


    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional
    public CustomerEntity getCustomerById(final Integer customerId) {
        return customerDao.getCustomerById(customerId);
    }

    @Transactional
    public CustomerEntity saveCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException {

        // Regular expression for email format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);

        // Throws SignUpRestrictedException if any field is empty except lastname
        if (customerEntity.getFirstName() == null || customerEntity.getEmail() == null ||
                customerEntity.getContactNumber() == null || customerEntity.getPassword() == null) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
            // Throws SignUpRestrictedException for invalid email format
        } else if (!pattern.matcher(customerEntity.getEmail()).matches()) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
            // Throws SignUpRestrictedException if contactNumber is not numbers of length is not 10 digits
        } else if(!customerEntity.getContactNumber().matches("[0-9]+") || customerEntity.getContactNumber().length() != 10) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
            // Throws SignUpRestrictedException if password length is less than 8 characters
            // or if it does not contain at least one digit or if it does not contain at least one uppercase character
            // or if it does not contain any of the mentioned special characters
        } else if(customerEntity.getPassword().length() < 8
                || !customerEntity.getPassword().matches(".*[0-9]{1,}.*")
                || !customerEntity.getPassword().matches(".*[A-Z]{1,}.*")
                || !customerEntity.getPassword().matches(".*[#@$%&*!^]{1,}.*")) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
            // Throws SignUpRestrictedException if a customer with the same contact number is already registered
        } else if (customerDao.getCustomerByContactNumber(customerEntity.getContactNumber()) != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }

        // Encryption of password
        String[] encryptedText = cryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        // Called customerDao to insert new customer record in the database
        return customerDao.createCustomer(customerEntity);
    }


    @Transactional
    public CustomerAuthEntity authenticate(final String contactNumber , final String password) throws AuthenticationFailedException {
        //Call the getCustomerByContactNumber method in CustomerDao class for CustomerDao object and pass contactNumber as argument
        // Receive the value returned by getCustomerByContactNumber() method in CustomerEntity type object(name it as customerEntity)
        CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
        if(customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        //After that you have got customerEntity from customer table, we need to compare the password entered by the customer
        // with the password stored in the database
        //Since the password stored in the database is encrypted, so we also encrypt the password entered by the customer
        // using the Salt attribute in the database
        //Call the encrypt() method in PasswordCryptographyProvider class for CryptographyProvider object

        final String encryptedPassword = cryptographyProvider.encrypt(password, customerEntity.getSalt());
        //Now encryptedPassword contains the password entered by the customer in encrypted form
        //And customerEntity.getPassword() gives the password stored in the database in encrypted form
        //We compare both the passwords (Note that in this Assessment we are assuming that the credentials are correct)
        if(encryptedPassword.equals(customerEntity.getPassword())) {
            //Implementation of JwtTokenProvider class
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            //Now CustomerAuthEntity type object is to be persisted in a database
            //Declaring an object customerAuthEntity of type CustomerAuthEntity and setting its attributes
            CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
            customerAuthEntity.setCustomer(customerEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerAuthEntity.getUuid(), now, expiresAt));

            customerAuthEntity.setLoginAt(now);
            customerAuthEntity.setExpiresAt(expiresAt);
            customerAuthEntity.setUuid(customerEntity.getUuid());

            //Call the createAuthToken() method in CustomerDao class for customerDao
            //Pass customerAuthEntity as an argument
            customerDao.createAuthToken(customerAuthEntity);

            //updateCustomer() method in CustomerDao class calls the merge() method to update the customerEntity
            customerDao.updateCustomer(customerEntity);
            return customerAuthEntity;

        }
        else{
            //throw exception
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }

    }

    @Transactional
    public CustomerEntity updateCustomer (CustomerEntity updatedCustomerEntity, final String authorizationToken)
            throws AuthorizationFailedException, UpdateCustomerException {

        //get the customerAuthToken details from customerDao
        CustomerAuthEntity customerAuthEntity = getCustomer(authorizationToken);

        // Validates the provided access token
        validateAccessToken(authorizationToken);

        //get the customer Details using the customerUuid
        CustomerEntity customerEntity =  customerDao.getCustomerByUuid(customerAuthEntity.getUuid());

        // Throws UpdateCustomerException if firstname is updated to null
        if (updatedCustomerEntity.getFirstName() == null) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }

        // Now set the updated firstName and lastName and attach it to the customerEntity
        customerEntity.setFirstName(updatedCustomerEntity.getFirstName());
        customerEntity.setLastName(updatedCustomerEntity.getLastName());

        //called customerDao to merge the content and update in the database
        customerDao.updateCustomer(customerEntity);
        return customerEntity;
    }

    @Transactional
    public CustomerEntity updateCustomerPassword (final String oldPassword, final String newPassword, final String authorizationToken)
            throws AuthorizationFailedException, UpdateCustomerException {

        // Gets the current time
        final ZonedDateTime now = ZonedDateTime.now();

        //get the customerAuthToken details from customerDao
        CustomerAuthEntity customerAuthEntity = getCustomer(authorizationToken);

        // Validates the provided access token
        validateAccessToken(authorizationToken);

        //get the customer Details using the customerUuid
        CustomerEntity customerEntity =  customerDao.getCustomerByUuid(customerAuthEntity.getUuid());

        // Throws UpdateCustomerException if either old password or new password is null
        if (oldPassword == null || newPassword ==  null) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }

        // Since the password stored in the database is encrypted, so we also encrypt the password entered by the customer
        // using the Salt attribute in the database
        // Call the encrypt() method in PasswordCryptographyProvider class for CryptographyProvider object
        final String encryptedPassword = cryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());

        // Throws UpdateCustomerException if old password provided is incorrect
        if(!encryptedPassword.equals(customerEntity.getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
            // Throws UpdateCustomerException if password length is less than 8 characters
            // or if it does not contain at least one digit or if it does not contain at least one uppercase character
            // or if it does not contain any of the mentioned special characters
        } else if (newPassword.length() < 8
                || !newPassword.matches(".*[0-9]{1,}.*")
                || !newPassword.matches(".*[A-Z]{1,}.*")
                || !newPassword.matches(".*[#@$%&*!^]{1,}.*")) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }

        // Now set the updated password and attach it to the customerEntity
        customerEntity.setPassword(newPassword);

        // Encryption of new password
        String[] encryptedText = cryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);

        // Calls customerDao to merge the content and update in the database
        customerDao.updateCustomer(customerEntity);
        return customerEntity;
    }

    @Transactional
    public CustomerAuthEntity logout (final String authorizationToken) throws AuthorizationFailedException {

        // Gets the customerAuthEntity with the provided authorizationToken
        CustomerAuthEntity customerAuthEntity = getCustomer(authorizationToken);

        // Gets the current time
        final ZonedDateTime now = ZonedDateTime.now();

        // Validates the provided access token
        validateAccessToken(authorizationToken);

        // Sets the logout time to now
        customerAuthEntity.setLogoutAt(now);

        // Calls customerDao to update the customer logout details in the database
        customerDao.updateCustomerAuth(customerAuthEntity);
        return customerAuthEntity;
    }

    @Transactional
    public void validateAccessToken(final String authorizationToken) throws AuthorizationFailedException{

        //get the customerAuthToken details from customerDao
        CustomerAuthEntity customerAuthEntity = customerDao.getCustomerAuthToken(authorizationToken);

        // Gets the current time
        final ZonedDateTime now = ZonedDateTime.now();

        // Throw AuthorizationFailedException if the customer is not authorized
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
            // Throw AuthorizationFailedException if the customer is logged out
        } else if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            // Throw AuthorizationFailedException if the customer session is expired
        } else if (now.isAfter(customerAuthEntity.getExpiresAt()) ) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }

    }

    @Transactional
    public CustomerAuthEntity getCustomer(final String accessToken) {
        // Calls customerDao to get the access token of the customer from the database
        return customerDao.getCustomerAuthToken(accessToken);
    }



}
