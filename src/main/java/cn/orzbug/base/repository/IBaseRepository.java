package cn.orzbug.base.repository;


import cn.orzbug.base.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * @author: SQJ
 * @data: 2018/4/27 11:36
 * @version:
 */
@NoRepositoryBean
public interface IBaseRepository<T extends BaseEntity<I>, I extends Serializable> extends JpaRepository<T, I> {

    @Transactional
    T saveOrUpdate(T t);

    @Transactional
    T update(T t);

    T get(I id);

    Page<T> findAll(T t, Pageable pageable);

    List<T> findAll(T t);

    Page<T> findAllEntities(Specification<T> specification, Pageable pageable);


}


