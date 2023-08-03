package com.driver.transformers;

import com.driver.model.Country;
import com.driver.model.CountryName;

public class CountryTransformer {
    public static Country countryNameToCountry(String cName){

        Country country = new Country();
        CountryName formattedName = null;
        if(cName.equals("IND"))
            formattedName = CountryName.IND;
        else if (cName.equals("AUS"))
            formattedName  = CountryName.AUS;
        else if(cName.equals("USA"))
            formattedName = CountryName.USA;
        else if(cName.equals("CHI"))
            formattedName = CountryName.CHI;
        else
            formattedName = CountryName.JPN;

        country.setCountryName(formattedName);
        country.setCode(formattedName.toCode());

        return country;
    }
}
