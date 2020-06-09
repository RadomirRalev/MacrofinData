package com.currencyconverter.demo.repositories.implementations;

import com.currencyconverter.demo.models.mvcmodels.Currency;
import com.currencyconverter.demo.repositories.contracts.CurrencyRepository;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;


@Repository
@Transactional
public class CurrencyRepositoryImpl implements CurrencyRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public CurrencyRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Currency getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Currency> query = session.createQuery("from Currency " +
                    "where id = :id", Currency.class);
            query.setParameter("id", id);
            return query.list().get(0);
        }
    }

    @Override
    public Currency getByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            Query<Currency> query = session.createQuery("from Currency " +
                    " where code = :code", Currency.class);
            query.setParameter("code", code);
            return query.list().get(0);
        }
    }

    @Override
    public void updateRecords(HashMap<String, String> codesAndRates) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (String key : codesAndRates.keySet()) {
                String value = codesAndRates.get(key);
                session.createQuery("update Currency " +
                        " set value = :value," +
                        " updatedOn = current_timestamp" +
                        " where code = :key")
                        .setParameter("key", key)
                        .setParameter("value", value)
                        .executeUpdate();
            }
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Currency> getAllCurrencies() {
        try (Session session = sessionFactory.openSession()) {
            Query<Currency> query = session.createQuery("from Currency where code != null", Currency.class);
            return query.list();
        }
    }
}
