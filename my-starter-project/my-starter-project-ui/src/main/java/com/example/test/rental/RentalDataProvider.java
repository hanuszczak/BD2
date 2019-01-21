package com.example.test.rental;

import com.example.test.backend.MyDataService;
import com.example.test.backend.rentalAgencyData.Rental;
import com.example.test.backend.rentalAgencyData.User;
import com.vaadin.flow.data.provider.ListDataProvider;

import java.util.Locale;
import java.util.Objects;

public class RentalDataProvider extends ListDataProvider<Rental> {

    /** Text filter that can be changed separately. */
    private String filterText = "";

    public RentalDataProvider() {
        super(MyDataService2.get().getAllRentals());
    }


    @Override
    public Integer getId(Rental rental) {
        Objects.requireNonNull(rental,
                "Cannot provide an id for a null user.");

        return rental.getRentalId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}
