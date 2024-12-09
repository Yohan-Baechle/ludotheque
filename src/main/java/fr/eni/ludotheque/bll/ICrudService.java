package fr.eni.ludotheque.bll;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T> {
	void create(T entity);

	public List<T> findAll();

	Optional<T> findById(int id);

	void saveOrUpdate(T entity);

	void delete(int id);

}
