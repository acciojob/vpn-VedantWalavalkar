package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

        Admin savedAdmin = adminRepository1.save(admin);

        return savedAdmin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setName(providerName);

        Optional<Admin> optionalAdmin = adminRepository1.findById(adminId);
        Admin admin = optionalAdmin.get();

        serviceProvider.setAdmin(admin);
        admin.getServiceProviderList().add(serviceProvider);

        Admin savedAdmin = adminRepository1.save(admin);


        return savedAdmin;

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
