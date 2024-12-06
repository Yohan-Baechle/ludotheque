package fr.eni.ludotheque.services;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T> {
	void add(T entity);

	public List<T> findAll();

	Optional<T> findById(int id);

	void update(T entity);

	void save(T entity);

	void delete(int id);

}
