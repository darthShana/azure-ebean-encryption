package com.darthShana.rest;

import com.darthShana.model.Customer;
import com.darthShana.model.query.QCustomer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/customer")
public class CustomerResource {

    @GET
      @Path("/{customerName}")
      @Produces(MediaType.APPLICATION_JSON)
    public Customer getCustomer(@PathParam("customerName") String customerName){
        return new QCustomer().name.eq(customerName).findOne();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void putCustomer(Customer customer){
        System.out.println(customer);
        customer.save();
    }
}
