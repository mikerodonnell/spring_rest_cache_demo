package demo.chat.dao;


public interface Dao<T> {
	
	T save(T entity);

}
