package fr.eni.ludotheque.dal;

import java.util.List;
import java.util.Optional;

public interface ICrudRepository<T> {
	void add(T entity);

	public List<T> findAll();

	Optional<T> findById(int id);

	void update(T entity);

	void delete(int id);
}
