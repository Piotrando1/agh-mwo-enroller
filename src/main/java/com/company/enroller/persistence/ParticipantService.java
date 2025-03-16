package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

	public Participant findByLogin(String login) {
		String hql = "FROM Participant WHERE login = :login";
		Query query = connector.getSession().createQuery(hql);
		query.setParameter("login", login);
		return (Participant) query.uniqueResult();
	}

	public void add(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(participant);
		transaction.commit();
	}

	public void update(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().update(participant);
		transaction.commit();
	}

	public Collection<Participant> getAllSortedByLogin(String sortOrder) {
		String hql = "FROM Participant ORDER BY login " + ("DESC".equalsIgnoreCase(sortOrder) ? "DESC" : "ASC");
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}
}
