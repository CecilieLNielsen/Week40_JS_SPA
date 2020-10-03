/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import entities.Address;
import entities.Person;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author cecilie
 */
public class Main {

    public static void main(String[] args) {
        Person p1 = new Person("Peter", "Larsen", "23098675", new Date(), new Date());
        Person p2 = new Person("Signe", "Larsen", "23346576", new Date(), new Date());
        Address a1 = new Address("Gammelkongevej 14", 4228, "Kongstrup");
        p1.setAddress(a1);
        Address a2 = new Address("Gammelkongevej 15", 4228, "Kongstrup");
        p2.setAddress(a2);
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
