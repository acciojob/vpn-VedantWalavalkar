package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.driver.transformers.CountryTransformer.countryNameToCountry;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        adminRepository1.save(admin);

        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);

        Admin admin= adminRepository1.findById(adminId).get();

        serviceProvider.setAdmin(admin);
        admin.getServiceProviders().add(serviceProvider);

        adminRepository1.save(admin);

        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        String cName = countryName.toUpperCase();
//        ind, aus, usa, chi, jpn
        if(!(cName.equals("IND") || cName.equals("AUS") || cName.equals("USA") || cName.equals("CHI") || cName.equals("JPN")))
            throw new Exception("Country not found");
        Country country = countryNameToCountry((cName));

        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository1.findById(serviceProviderId);
        ServiceProvider serviceProvider = optionalServiceProvider.get();
        country.setServiceProvider(serviceProvider);

        serviceProvider.getCountryList().add(country);

        ServiceProvider savedServiceProvider = serviceProviderRepository1.save(serviceProvider);

        return savedServiceProvider;
    }

}
