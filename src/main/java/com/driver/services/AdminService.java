package com.driver.services;

import com.driver.model.Admin;
import com.driver.model.ServiceProvider;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface AdminService{
    public Admin register(String username, String password);

    public Admin addServiceProvider(int adminId, String providerName);

    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception;

}