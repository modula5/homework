package io.fourfinanceit.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public abstract class BaseRepository <E> {
	
	@Autowired 
	private SessionFactory sessionFactory;

	private Class<E> entityClass;
	
	public BaseRepository(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	@Transactional
	public E loadByKey(Long key) {
		return (E) sessionFactory.getCurrentSession().get(entityClass, key);
	}
	
	@Transactional
	public void persist(E entity) {
		sessionFactory.getCurrentSession().persist(entity);
	}
	
	@Transactional
	public List<E> listAll() {
		return sessionFactory.getCurrentSession().createCriteria(entityClass).list();
	}
	
	protected Criteria criteria() {
		return sessionFactory.getCurrentSession().createCriteria(entityClass);
	}
	
	protected Session session() {
		return sessionFactory.getCurrentSession();
	}

}
