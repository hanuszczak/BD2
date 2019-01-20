package com.example.test.rental;


import com.example.test.backend.rentalAgencyData.Rental;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Back-end service interface for retrieving and updating product data.
 */
public abstract class MyDataService2 implements Serializable {

    public abstract Collection<Rental> getAllRentals();

    public static MyDataService2 get() {
        return RentalDataService.getInstance();
    }

}
