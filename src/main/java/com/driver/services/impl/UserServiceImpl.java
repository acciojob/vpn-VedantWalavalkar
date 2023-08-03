package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.driver.transformers.CountryTransformer.countryNameToCountry;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        String cName = countryName.toUpperCase();
//        ind, aus, usa, chi, jpn
        if(!(cName.equals("IND") || cName.equals("AUS") || cName.equals("USA") || cName.equals("CHI") || cName.equals("JPN")))
            throw new Exception("Country not found");
        Country country = countryNameToCountry(cName);
        country.setUser(user);

//        User savedUser = userRepository3.save(user);
        String orgId = country.getCode() +"."+ user.getId();
        user.setOriginalIp(orgId);
        user.setConnected(false);
        user.setMaskedIp(null);
        user.setOriginalCountry(country);

        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        Optional<User> optionalUser = userRepository3.findById(userId);
        User user = optionalUser.get();

        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository3.findById(serviceProviderId);
        ServiceProvider serviceProvider = optionalServiceProvider.get();

        user.getServiceProviders().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        ServiceProvider savedServiceProvider = serviceProviderRepository3.save(serviceProvider);

        List<User> userList = savedServiceProvider.getUsers();

        return user;
    }
}
