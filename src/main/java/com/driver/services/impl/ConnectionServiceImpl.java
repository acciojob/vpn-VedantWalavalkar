package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        Optional<User> optionalUser = userRepository2.findById(userId);
        User user = optionalUser.get();

        if(user.getConnected())
            throw new Exception("Already connected");

        String cName = countryName.toUpperCase();

        if(user.getCountry().getCountryName().toString().equals(cName))
            return user;

        List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
        if(serviceProviderList.size() == 0)
            throw new Exception("Unable to connect");
        ServiceProvider selectedServiceProvider = new ServiceProvider();
        Country selectedCountry = new Country();
        for(ServiceProvider sp : serviceProviderList)
        {
            List<Country> countryList = sp.getCountryList();
            for(Country cn : countryList){
                if(cn.getCountryName().toString().equals(cName))
                {
                    selectedServiceProvider = sp;
                    selectedCountry = cn;
                    break;
                }
            }
        }

        if(selectedServiceProvider == null)
            throw new Exception("Unable to connect");

        Connection connection = new Connection();
        connection.setServiceProvider(selectedServiceProvider);
        connection.setUser(user);

        user.setConnected(true);
        String maskedId = selectedCountry.getCode().toString() + "." + selectedServiceProvider.getId() + "." + user.getId();
        user.setMaskedIP(maskedId);
        Country country = user.getCountry();
        country.setCountryName(selectedCountry.getCountryName());
        country.setCode(selectedCountry.getCode());
        user.setCountry(country);
        user.getConnectionList().add(connection);

        selectedServiceProvider.getConnectionList().add(connection);

        connectionRepository2.save(connection);
        serviceProviderRepository2.save(selectedServiceProvider);
        User savedUser = userRepository2.save(user);

        return savedUser;
    }
    @Override
    public User disconnect(int userId) throws Exception
    {
        Optional<User> optionalUser = userRepository2.findById(userId);
        User user = optionalUser.get();

        if(!user.getConnected())
            throw new Exception("Already disconnected");

        user.setConnected(false);
        user.setMaskedIP(null);

        // set country
        String originalIP = user.getOriginalIP().substring(0,3);
        CountryName originalCountryName = null;
        String countryCode = null;
        if(originalIP.equals("001")) {
            originalCountryName = CountryName.IND;
            countryCode = originalCountryName.toCode();
        }
        else if(originalIP.equals("002"))
        {
            originalCountryName = CountryName.USA;
            countryCode = originalCountryName.toCode();
        }
        else if(originalIP.equals("003"))
        {
            originalCountryName = CountryName.AUS;
            countryCode = originalCountryName.toCode();
        }
        else if(originalIP.equals("004"))
        {
            originalCountryName = CountryName.CHI;
            countryCode = originalCountryName.toCode();
        }
        else
        {
            originalCountryName = CountryName.JPN;
            countryCode = originalCountryName.toCode();
        }
        Country country = user.getCountry();
        country.setCode(countryCode);
        country.setCountryName(originalCountryName);

        return userRepository2.save(user);
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        Optional<User> optionalSender = userRepository2.findById(senderId);
        if(!optionalSender.isPresent())
            throw new Exception("Cannot establish communication");
        User sender = optionalSender.get();

        Optional<User> optionalReceiver = userRepository2.findById(receiverId);
        if(!optionalReceiver.isPresent())
            throw new Exception("Cannot establish communication");
        User receiver = optionalReceiver.get();

        Country senderCountry = sender.getCountry();
        Country receiverCountry = receiver.getCountry();

        if(senderCountry.getCountryName() == receiverCountry.getCountryName())
            return sender;

        // connect sender to vpn of receiver's country
        try {
             connect(senderId, receiverCountry.getCountryName().toString());
        }
        catch (Exception e) {
            throw new Exception("Cannot establish communication");
        }
        return sender;
    }
}
