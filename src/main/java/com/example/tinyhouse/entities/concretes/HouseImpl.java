package com.example.tinyhouse.entities.concretes;

import java.util.List;

public class HouseImpl extends House {
    public HouseImpl(int id, String title, String description, double price, String location, String status, User host, List<Reservation> reservationList, List<Comment> commentList) {
        super(id, title, description, price, location, status, host, reservationList, commentList);
    }
}
