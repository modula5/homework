package io.fourfinanceit.repository;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.fourfinanceit.domain.BaseEntity;

@SuppressWarnings("unchecked")
@Repository
public class EntityRepository {
	
	@Autowired private SessionFactory sessionFactory;
	
	@Transactional
	public <E> E get(Long key, Class<E> clazz) {
		return (E) sessionFactory.getCurrentSession().get(clazz, key);
	}
	
	@Transactional
	public void persist(BaseEntity entity) {
		sessionFactory.getCurrentSession().persist(entity);
	}
	
	@Transactional
	public <E> List<E> listAll(Class<E> clazz) {
		return sessionFactory.getCurrentSession().createCriteria(clazz).list();
	}

}
