package com.multics.meatandmeet.models;


public class Order
{
    public Item item;
    public int quantity;
    public double extendedPrice = 0.0;

    public Order(Item item, int quantity)
    {
        this.item = item;
        this.quantity = quantity;
       // this.extendedPrice = item.getPrice() * quantity;
    }
}
