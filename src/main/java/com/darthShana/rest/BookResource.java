package com.darthShana.rest;

import com.darthShana.model.Book;
import com.darthShana.model.Customer;
import com.darthShana.model.query.QBook;
import com.darthShana.model.query.QCustomer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/book")
public class BookResource {

    @GET
      @Path("/{bookName}")
      @Produces(MediaType.APPLICATION_JSON)
    public Book getBook(@PathParam("bookName") String bookName){
        return new QBook().bookName.eq(bookName).findOne();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void putCustomer(Book book){
        System.out.println(book);
        book.save();
    }
}
